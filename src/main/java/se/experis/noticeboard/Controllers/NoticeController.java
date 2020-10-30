package se.experis.noticeboard.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.experis.noticeboard.Repositories.AccountRepository;
import se.experis.noticeboard.Repositories.CommentRepository;
import se.experis.noticeboard.Repositories.NoticeRepository;
import se.experis.noticeboard.Utils.SessionKeeper;
import se.experis.noticeboard.models.Comment;
import se.experis.noticeboard.models.DisplayNoticeDTO;
import se.experis.noticeboard.models.Notice;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/notices")
    public ResponseEntity<Void> createNotice(@RequestBody Notice notice, HttpSession session) {

        HttpStatus status;

        if (SessionKeeper.getInstance().isLoggedIn(session.getId())) {
            notice.setAccount(accountRepository.findById(SessionKeeper.getInstance().getSessionAccountId(session.getId())).orElse(null)); // set author to the account in the current session
            try {
                notice.setEditedTimestamp(null);
                Notice newNotice = noticeRepository.save(notice);
                status = newNotice != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
            } catch (Exception e) { // if body is in wrong format
                status = HttpStatus.BAD_REQUEST;
            }
        } else { // if not logged in
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status);
    }

    @GetMapping("/notices")
    public ResponseEntity<List<Notice>> getAllNotices() {
        return new ResponseEntity<>(noticeRepository.findAllByOrderByTimestampDesc(), HttpStatus.OK);
    }

    @GetMapping("/notices/{id}")
    public ResponseEntity<DisplayNoticeDTO> getNotice(@PathVariable long id) {

        DisplayNoticeDTO displayNotice = new DisplayNoticeDTO();
        HttpStatus status;

        if (noticeRepository.existsById(id)) {
            Optional<Notice> noticeRepo = noticeRepository.findById(id);
            Notice notice = noticeRepo.orElse(null);

            displayNotice.setNoticeTitle(notice.getTitle());
            displayNotice.setNoticeUserName(notice.getAccount().getUserName());
            displayNotice.setAccountId(notice.getAccount().getId());
            displayNotice.setNoticeContent(notice.getContent());
            displayNotice.setNoticeTimestamp(notice.getTimestamp());
            displayNotice.setEditedNoticeTimestamp(notice.getEditedTimestamp());
            displayNotice.setComments(notice.getComments());

            status = HttpStatus.OK;

        } else { // if notice does not exist
            displayNotice = null;
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(displayNotice, status);
    }

    @PatchMapping("/notices/{id}")
    public ResponseEntity<Void> changeNotice(@PathVariable long id, @RequestBody Notice changedNotice, HttpSession session) {

        HttpStatus status;

        if (SessionKeeper.getInstance().isLoggedIn(session.getId())) {
            if (noticeRepository.existsById(id)) {
                Optional<Notice> noticeRepo = noticeRepository.findById(id);
                Notice notice = noticeRepo.orElse(null);

                if (notice.getAccount() ==
                        accountRepository.findById(SessionKeeper.getInstance().getSessionAccountId(session.getId())).orElse(null)) { // checks if the account that created the notice is the same as the one in session

                    if (changedNotice.getTitle() != null) {
                        notice.setTitle(changedNotice.getTitle());
                    }

                    if (changedNotice.getContent() != null) {
                        notice.setContent(changedNotice.getContent());
                    }
                    notice.setEditedTimestamp(new Date());

                    try {
                        Notice newNotice = noticeRepository.save(notice);
                        status = newNotice != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
                    } catch (Exception e) { // if body is in wrong format
                        status = HttpStatus.BAD_REQUEST;
                    }
                } else { // if notice does not belong to account in session
                    status = HttpStatus.UNAUTHORIZED;
                }
            } else { // in notice is not found
                status = HttpStatus.NOT_FOUND;
            }
        } else { // if not logged in
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(status);
    }

    @PostMapping("/notices/{id}/comments")
    public ResponseEntity<Void> createComment(@RequestBody Comment comment, @PathVariable long id, HttpSession session) {
        HttpStatus status;

        if (SessionKeeper.getInstance().isLoggedIn(session.getId())) {
            if (noticeRepository.existsById(id)) {
                comment.setAccount(accountRepository.findById(SessionKeeper.getInstance().getSessionAccountId(session.getId())).orElse(null)); // Set author to account that is currently in the session
                comment.setNotice(noticeRepository.findById(id).orElse(null)); // Relate comment to the chosen notice

                try {
                    comment.setEditedTimestamp(null);
                    Comment newComment = commentRepository.save(comment);
                    status = newComment != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
                } catch (Exception e) { // if body is in wrong format
                    status = HttpStatus.BAD_REQUEST;
                }
            } else { // if notice is not found
                status = HttpStatus.NOT_FOUND;
            }
        } else { // if not logged in
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status);
    }

    @PatchMapping("/notices/{noticeId}/comments/{commentId}")
    public ResponseEntity<Void> changeComment(@RequestBody Comment changedComment, @PathVariable long noticeId, @PathVariable long commentId, HttpSession session) {

        HttpStatus status;

        if (SessionKeeper.getInstance().isLoggedIn(session.getId())) {
            if (noticeRepository.existsById(noticeId) && commentRepository.existsById(commentId)) {
                Optional<Comment> commentRepo = commentRepository.findById(commentId);
                Comment comment = commentRepo.orElse(null);

                if (comment.getAccount() ==
                        accountRepository.findById(SessionKeeper.getInstance().getSessionAccountId(session.getId())).orElse(null)) { // checks if the account that created the comment is the same as the one in session

                    if (changedComment.getContent() != null) { // if body contains content
                        comment.setContent(changedComment.getContent());
                        comment.setEditedTimestamp(new Date());
                    }

                    try {
                        Comment newComment = commentRepository.save(comment);
                        status = newComment != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
                    } catch (Exception e) { // if body is in wrong format
                        status = HttpStatus.BAD_REQUEST;
                    }
                } else { // if comment does not belong to account in session
                    status = HttpStatus.UNAUTHORIZED;
                }
            } else { // if comment is not found
                status = HttpStatus.NOT_FOUND;
            }
        } else { // if not logged in
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status);
    }

    @DeleteMapping("/notices/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable long id, HttpSession session) {

        HttpStatus status;

        if (SessionKeeper.getInstance().isLoggedIn(session.getId())){
            if (noticeRepository.existsById(id)) {
                if (noticeRepository.findById(id).orElse(null).getAccount() ==
                        accountRepository.findById(SessionKeeper.getInstance().getSessionAccountId(session.getId())).orElse(null)) { // checks if the account that created the notice is the same as the one in session

                    noticeRepository.deleteById(id);
                    status = HttpStatus.OK;
                } else { // if notice author is not the same as the account in session
                    status = HttpStatus.UNAUTHORIZED;
                }
            } else { // if notice is not found
                status = HttpStatus.NOT_FOUND;
            }
        } else { // if not logged in
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status);
    }

    @DeleteMapping("/notices/{noticeId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long noticeId, @PathVariable long commentId, HttpSession session) {

        HttpStatus status;

        if (SessionKeeper.getInstance().isLoggedIn(session.getId())){
            if (noticeRepository.existsById(noticeId) && commentRepository.existsById(commentId)) {
                if (commentRepository.findById(commentId).orElse(null).getAccount() ==
                        accountRepository.findById(SessionKeeper.getInstance().getSessionAccountId(session.getId())).orElse(null)) { // checks if the account that created the comment is the same as the one in session

                    commentRepository.deleteById(commentId);
                    status = HttpStatus.OK;
                } else { // if comment author is not the same as the account in session
                    status = HttpStatus.UNAUTHORIZED;
                }
            } else { // if comment is not found
                status = HttpStatus.NOT_FOUND;
            }
        } else { // if not logged in
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status);
    }
}
