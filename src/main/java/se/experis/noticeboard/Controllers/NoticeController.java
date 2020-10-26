package se.experis.noticeboard.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    private NoticeRepository repo;

    @PostMapping("/newNotice")
    public boolean createNotice(@RequestBody Notice noticeBody) {

        repo.save(noticeBody);
        System.out.println("Created notice");

        return true;
    }

    @GetMapping("/notices")
    public List<Notice> getAllNotices() {
        var temp = repo.findAll();
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
    public boolean changeNotice(@PathVariable int id, @RequestBody Notice changedNotice) {

        if (repo.existsById(id)) {
            Optional<Notice> noticeRepo = repo.findById(id);
            Notice notice = noticeRepo.get();

            if (changedNotice.getTitle() != null) {
                notice.setTitle(changedNotice.getTitle());
            }
            if (changedNotice.getContent() != null) {
                notice.setContent(changedNotice.getContent());
            }
            notice.setEditedTimestamp(new Date());

            repo.save(notice);
            System.out.println("Updated notice");
        } else {
            System.out.println("Could not find notice");
            return false;
        }

        return true;
    }

    @PostMapping("/notices/:{id}/comments")
    public boolean createComment(@RequestBody Comment comment, @PathVariable int id) {
        return true;
    }

    @PatchMapping("/notices/:{noticeId}/comments/:{commentId}")
    public boolean changeComment(@RequestBody Comment comment, @PathVariable int noticeId, @PathVariable int commentId) {

        return true;
    }

    @DeleteMapping("/notices/:{id}")
    public boolean deleteNotice(@PathVariable int id) {

        if (repo.existsById(id)) {
            repo.deleteById(id);
            System.out.println("Deleted notice");
        } else {
            System.out.println("Could not delete notice");
            return false;
        }
        return true;
    }

    @DeleteMapping("/notices/:{id}/comments/:{id}")
    public boolean deleteComment(@PathVariable int id) {

        return true;
    }
}
