package se.experis.noticeboard.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.experis.noticeboard.Repositories.CommentRepository;
import se.experis.noticeboard.Repositories.NoticeRepository;
import se.experis.noticeboard.models.Comment;
import se.experis.noticeboard.models.DisplayNoticeDTO;
import se.experis.noticeboard.models.Notice;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")
public class NoticeController { // Kanske lägga till response med statuskod på samtliga etc.. ?

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/newNotice")
    public ResponseEntity<Boolean> createNotice(@RequestBody Notice notice) {

        noticeRepository.save(notice);
        System.out.println("Created notice");
        HttpStatus res = HttpStatus.OK;

        return new ResponseEntity<>(true, res);
    }

    @GetMapping("/notices")
    public ResponseEntity<List<Notice>> getAllNotices() {

        var notices = noticeRepository.findAll();
        System.out.println("Gathered all notices");
        HttpStatus res = HttpStatus.OK;

        return new ResponseEntity<>(notices, res);
    }

    @GetMapping("/notices/:{id}")
    public ResponseEntity<DisplayNoticeDTO> getNotice(@PathVariable long id) {

        DisplayNoticeDTO displayNotice = new DisplayNoticeDTO();
        HttpStatus res;

        if (noticeRepository.existsById(id)) {
            Optional<Notice> noticeRepo = noticeRepository.findById(id);
            Notice notice = noticeRepo.get();

            displayNotice.setNoticeTitle(notice.getTitle());
            displayNotice.setNoticeUserName(notice.getAccount().getUserName());
            displayNotice.setNoticeContent(notice.getContent());
            displayNotice.setNoticeTimestamp(notice.getTimestamp());
            displayNotice.setComments(notice.getComments());

            System.out.println("Gathered notice with id: "+notice.getId());
            res = HttpStatus.OK;

        } else {
            res = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(displayNotice, res);
    }

    @PatchMapping("/notices/:{id}")
    public ResponseEntity<Boolean> changeNotice(@PathVariable long id, @RequestBody Notice changedNotice) {

        HttpStatus res;

        if (noticeRepository.existsById(id)) {
            Optional<Notice> noticeRepo = noticeRepository.findById(id);
            Notice notice = noticeRepo.get();

            if (changedNotice.getTitle() != null) {
                notice.setTitle(changedNotice.getTitle());
            }
            if (changedNotice.getContent() != null) {
                notice.setContent(changedNotice.getContent());
            }
            notice.setEditedTimestamp(new Date());

            noticeRepository.save(notice);
            System.out.println("Updated notice");
            res = HttpStatus.OK;
        } else {
            System.out.println("Could not find notice");
            res = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(true, res);
    }

    @PostMapping("/notices/:{id}/comments")
    public ResponseEntity<Boolean> createComment(@RequestBody Comment comment, @PathVariable long id) {
        HttpStatus res;
        if (noticeRepository.existsById(id)) {
            commentRepository.save(comment);
            System.out.println("Created notice");
            res = HttpStatus.CREATED;
        } else {
            System.out.println("Could not find notice");
            res = HttpStatus.NOT_FOUND;
        }


        return new ResponseEntity<>(true, res);
    }

    @PatchMapping("/notices/:{noticeId}/comments/:{commentId}")
    public ResponseEntity<Boolean> changeComment(@RequestBody Comment changedComment, @PathVariable long noticeId, @PathVariable long commentId) {

        HttpStatus res;

        if (noticeRepository.existsById(noticeId) && commentRepository.existsById(commentId)) {
            Optional<Comment> commentRepo = commentRepository.findById(commentId);
            Comment comment = commentRepo.get();

            if (changedComment.getContent() != null) {
                comment.setContent(changedComment.getContent());
            }
            comment.setEditedTimestamp(new Date());

            commentRepository.save(comment);
            System.out.println("Updated comment");
            res = HttpStatus.OK;
        } else {
            System.out.println("Could not find comment");
            res = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(true, res);
    }

    @DeleteMapping("/notices/:{id}")
    public ResponseEntity<Boolean> deleteNotice(@PathVariable long id) {

        HttpStatus res;

        if (noticeRepository.existsById(id)) {
            noticeRepository.deleteById(id);
            System.out.println("Deleted notice");
            res = HttpStatus.OK;
        } else {
            System.out.println("Could not delete notice");
            res = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(true, res);
    }

    @DeleteMapping("/notices/:{noticeId}/comments/:{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable long noticeId, @PathVariable long commentId) {

        HttpStatus res;

        if (noticeRepository.existsById(noticeId) && commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            System.out.println("Deleted comment");
            res = HttpStatus.OK;
        } else {
            System.out.println("Could not delete comment");
            res = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(true, res);
    }
}
