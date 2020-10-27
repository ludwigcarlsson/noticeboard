package se.experis.noticeboard.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.experis.noticeboard.Repositories.AccountRepository;
import se.experis.noticeboard.Repositories.CommentRepository;
import se.experis.noticeboard.Repositories.NoticeRepository;
import se.experis.noticeboard.Utils.SessionKeeper;
import se.experis.noticeboard.models.Account;
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
            Optional<Account> accountRepo = accountRepository.findById(SessionKeeper.getInstance().getSessionAccountId(session.getId()));
            Account account = accountRepo.get();
            notice.setAccount(account);
            noticeRepository.save(notice);
            System.out.println("Created notice");
            status = HttpStatus.CREATED;
        } else {
            System.out.println("Not logged in");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status);
    }

    @GetMapping("/notices")
    public ResponseEntity<List<Notice>> getAllNotices() {

        var notices = noticeRepository.findAll();
        System.out.println("Gathered all notices");
        HttpStatus status = HttpStatus.OK;

        return new ResponseEntity<>(notices, status);
    }

    @GetMapping("/notices/{id}")
    public ResponseEntity<DisplayNoticeDTO> getNotice(@PathVariable long id) {

        DisplayNoticeDTO displayNotice = new DisplayNoticeDTO();
        HttpStatus status;

        if (noticeRepository.existsById(id)) {
            Optional<Notice> noticeRepo = noticeRepository.findById(id);
            Notice notice = noticeRepo.get();

            displayNotice.setNoticeTitle(notice.getTitle());
            displayNotice.setNoticeUserName(notice.getAccount().getUserName());
            displayNotice.setNoticeContent(notice.getContent());
            displayNotice.setNoticeTimestamp(notice.getTimestamp());
            displayNotice.setComments(notice.getComments());

            System.out.println("Gathered notice with id: "+notice.getId());
            status = HttpStatus.OK;

        } else {
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
                Notice notice = noticeRepo.get();

                Optional<Account> accountRepo = accountRepository.findById(SessionKeeper.getInstance().getSessionAccountId(session.getId()));
                Account account = accountRepo.get();
                if (notice.getAccount() == account) {
                    if (changedNotice.getTitle() != null) {
                        notice.setTitle(changedNotice.getTitle());
                    }
                    if (changedNotice.getContent() != null) {
                        notice.setContent(changedNotice.getContent());
                    }
                    notice.setEditedTimestamp(new Date());

                    noticeRepository.save(notice);
                    System.out.println("Updated notice");
                    status = HttpStatus.OK;
                } else {
                    System.out.println("Not your notice");
                    status = HttpStatus.UNAUTHORIZED;
                }
            } else {
                System.out.println("Could not find notice");
                status = HttpStatus.NOT_FOUND;
            }
        } else {
            System.out.println("Not logged in");
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(status);
    }

    @PostMapping("/notices/{id}/comments")
    public ResponseEntity<Void> createComment(@RequestBody Comment comment, @PathVariable long id, HttpSession session) {
        HttpStatus status;

        if (SessionKeeper.getInstance().isLoggedIn(session.getId())) {
            if (noticeRepository.existsById(id)) {
                Optional<Account> accountRepo = accountRepository.findById(SessionKeeper.getInstance().getSessionAccountId(session.getId()));
                Account account = accountRepo.get();
                comment.setAccount(account); // Set author to account that is currently in the session

                Optional<Notice> noticeRepo = noticeRepository.findById(id);
                Notice notice = noticeRepo.get();
                comment.setNotice(notice); // Relate comment to the chosen notice

                commentRepository.save(comment);
                System.out.println("Created comment");
                status = HttpStatus.CREATED;
            } else {
                System.out.println("Could not find comment");
                status = HttpStatus.NOT_FOUND;
            }
        } else {
            System.out.println("Not logged in");
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
                Comment comment = commentRepo.get();

                Optional<Account> accountRepo = accountRepository.findById(SessionKeeper.getInstance().getSessionAccountId(session.getId()));
                Account account = accountRepo.get();

                if (comment.getAccount() == account){
                    if (changedComment.getContent() != null) {
                        comment.setContent(changedComment.getContent());
                    }
                    comment.setEditedTimestamp(new Date());

                    commentRepository.save(comment);
                    System.out.println("Updated comment");
                    status = HttpStatus.OK;
                } else {
                    System.out.println("Not your comment");
                    status = HttpStatus.UNAUTHORIZED;
                }
            } else {
                System.out.println("Could not find comment");
                status = HttpStatus.NOT_FOUND;
            }
        } else {
            System.out.println("Not logged in");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status);
    }

    @DeleteMapping("/notices/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable long id, HttpSession session) {

        HttpStatus status;

        if (SessionKeeper.getInstance().isLoggedIn(session.getId())){
            if (noticeRepository.existsById(id)) {
                Optional<Notice> noticeRepo = noticeRepository.findById(id);
                Notice notice = noticeRepo.get();

                Optional<Account> accountRepo = accountRepository.findById(SessionKeeper.getInstance().getSessionAccountId(session.getId()));
                Account account = accountRepo.get();
                if (notice.getAccount() == account) {
                    noticeRepository.deleteById(id);
                    System.out.println("Deleted notice");
                    status = HttpStatus.OK;
                } else {
                    System.out.println("Not your notice");
                    status = HttpStatus.UNAUTHORIZED;
                }
            } else {
                System.out.println("Could not delete notice");
                status = HttpStatus.NOT_FOUND;
            }
        } else {
            System.out.println("Not logged in");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status);
    }

    @DeleteMapping("/notices/{noticeId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long noticeId, @PathVariable long commentId, HttpSession session) {

        HttpStatus status;

        if (SessionKeeper.getInstance().isLoggedIn(session.getId())){
            if (noticeRepository.existsById(noticeId) && commentRepository.existsById(commentId)) {
                Optional<Comment> commentRepo = commentRepository.findById(commentId);
                Comment comment = commentRepo.get();

                Optional<Account> accountRepo = accountRepository.findById(SessionKeeper.getInstance().getSessionAccountId(session.getId()));
                Account account = accountRepo.get();
                if (comment.getAccount() == account) {
                    commentRepository.deleteById(commentId);
                    System.out.println("Deleted comment");
                    status = HttpStatus.OK;
                } else {
                    System.out.println("Not your comment");
                    status = HttpStatus.UNAUTHORIZED;
                }
            } else {
                System.out.println("Could not delete comment");
                status = HttpStatus.NOT_FOUND;
            }
        } else {
            System.out.println("Not logged in");
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status);
    }
}
