package se.experis.noticeboard.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.noticeboard.models.Account;

public interface UserRepository extends JpaRepository<Account, Long> {
}
