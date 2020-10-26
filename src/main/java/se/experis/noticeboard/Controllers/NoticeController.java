package se.experis.noticeboard.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
    public boolean createNotice(@RequestBody Notice notice) {

        noticeRepository.save(notice);
        System.out.println("Created notice");

        return true;
    }

    @GetMapping("/notices")
    public List<Notice> getAllNotices() {
        var notices = noticeRepository.findAll();
        System.out.println("Gathered all notices");

        return notices;
    }

    @GetMapping("/notices/:{id}")
    public DisplayNoticeDTO getNotice(@PathVariable long id) {

        DisplayNoticeDTO displayNotice = new DisplayNoticeDTO();

        if (noticeRepository.existsById(id)) {
            Optional<Notice> noticeRepo = noticeRepository.findById(id);
            Notice notice = noticeRepo.get();

            displayNotice.setNoticeTitle(notice.getTitle());
            displayNotice.setNoticeUserName(notice.getUser().getUserName());
            displayNotice.setNoticeContent(notice.getContent());
            displayNotice.setNoticeTimestamp(notice.getTimestamp());
            displayNotice.setComments(notice.getComments());

            System.out.println("Gathered notice with id: "+notice.getId());

        }
        return displayNotice;
    }

    @PatchMapping("/notices/:{id}")
    public boolean changeNotice(@PathVariable long id, @RequestBody Notice changedNotice) {

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
        } else {
            System.out.println("Could not find notice");
        }

        return true;
    }

    @PostMapping("/notices/:{id}/comments")
    public boolean createComment(@RequestBody Comment comment, @PathVariable long id) {

        commentRepository.save(comment);
        System.out.println("Created notice");

        return true;
    }

    @PatchMapping("/notices/:{noticeId}/comments/:{commentId}")
    public boolean changeComment(@RequestBody Comment changedComment, @PathVariable long noticeId, @PathVariable long commentId) {

        if (noticeRepository.existsById(noticeId) && commentRepository.existsById(commentId)) {
            Optional<Comment> commentRepo = commentRepository.findById(commentId);
            Comment comment = commentRepo.get();

            if (changedComment.getContent() != null) {
                comment.setContent(changedComment.getContent());
            }
            comment.setEditedTimestamp(new Date());

            commentRepository.save(comment);
            System.out.println("Updated comment");
        } else {
            System.out.println("Could not find comment");
        }

        return true;
    }

    @DeleteMapping("/notices/:{id}")
    public boolean deleteNotice(@PathVariable long id) {

        if (noticeRepository.existsById(id)) {
            noticeRepository.deleteById(id);
            System.out.println("Deleted notice");
        } else {
            System.out.println("Could not delete notice");
        }

        return true;
    }

    @DeleteMapping("/notices/:{noticeId}/comments/:{commentId}")
    public boolean deleteComment(@PathVariable long noticeId, @PathVariable long commentId) {

        if (noticeRepository.existsById(noticeId) && commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            System.out.println("Deleted comment");
        } else {
            System.out.println("Could not delete comment");
        }

        return true;
    }
}
