package se.experis.noticeboard.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.experis.noticeboard.Repositories.CommentRepository;
import se.experis.noticeboard.Repositories.NoticeRepository;
import se.experis.noticeboard.models.Comment;
import se.experis.noticeboard.models.Notice;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1")
public class NoticeController { // Kanske lägga till response med statuskod på samtliga etc.. ?

    @Autowired
    private NoticeRepository nRepo;

    @Autowired
    private CommentRepository cRepo;

    @PostMapping("/newNotice")
    public boolean createNotice(@RequestBody Notice notice) {

        nRepo.save(notice);
        System.out.println("Created notice");

        return true;
    }

    @GetMapping("/notices")
    public List<Notice> getAllNotices() {
        var temp = nRepo.findAll();
        System.out.println("Gathered all notices");

        return temp;
    }

//    @GetMapping("/notices/:{id}")
//    public NoticeView getNotice(@PathVariable int id) {
//
//
//        return NoticeView;
//    }

    @PatchMapping("/notices/:{id}")
    public boolean changeNotice(@PathVariable long id, @RequestBody Notice changedNotice) {

        if (nRepo.existsById(id)) {
            Optional<Notice> noticeRepo = nRepo.findById(id);
            Notice notice = noticeRepo.get();

            if (changedNotice.getTitle() != null) {
                notice.setTitle(changedNotice.getTitle());
            }
            if (changedNotice.getContent() != null) {
                notice.setContent(changedNotice.getContent());
            }
            notice.setEditedTimestamp(new Date());

            nRepo.save(notice);
            System.out.println("Updated notice");
        } else {
            System.out.println("Could not find notice");
        }

        return true;
    }

    @PostMapping("/notices/:{id}/comments")
    public boolean createComment(@RequestBody Comment comment, @PathVariable long id) {

        cRepo.save(comment);
        System.out.println("Created notice");

        return true;
    }

    @PatchMapping("/notices/:{noticeId}/comments/:{commentId}")
    public boolean changeComment(@RequestBody Comment changedComment, @PathVariable long noticeId, @PathVariable long commentId) {

        if (nRepo.existsById(noticeId) && cRepo.existsById(commentId)) {
            Optional<Comment> commentRepo = cRepo.findById(commentId);
            Comment comment = commentRepo.get();

            if (changedComment.getContent() != null) {
                comment.setContent(changedComment.getContent());
            }
            comment.setEditedTimestamp(new Date());

            cRepo.save(comment);
            System.out.println("Updated comment");
        } else {
            System.out.println("Could not find comment");
        }

        return true;
    }

    @DeleteMapping("/notices/:{id}")
    public boolean deleteNotice(@PathVariable long id) {

        if (nRepo.existsById(id)) {
            nRepo.deleteById(id);
            System.out.println("Deleted notice");
        } else {
            System.out.println("Could not delete notice");
        }

        return true;
    }

    @DeleteMapping("/notices/:{noticeId}/comments/:{commentId}")
    public boolean deleteComment(@PathVariable long noticeId, @PathVariable long commentId) {

        if (nRepo.existsById(noticeId) && cRepo.existsById(commentId)) {
            cRepo.deleteById(commentId);
            System.out.println("Deleted comment");
        } else {
            System.out.println("Could not delete comment");
        }

        return true;
    }
}
