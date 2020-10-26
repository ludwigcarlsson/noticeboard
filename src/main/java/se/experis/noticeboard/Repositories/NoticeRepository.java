package se.experis.noticeboard.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.noticeboard.models.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
