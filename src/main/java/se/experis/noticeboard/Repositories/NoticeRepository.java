package se.experis.noticeboard.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.noticeboard.models.Notice;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    public List<Notice> findAllByOrderByTimestampDesc();
}
