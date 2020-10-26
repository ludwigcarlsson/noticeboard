package se.experis.noticeboard.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.noticeboard.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
