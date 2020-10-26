package se.experis.noticeboard.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.noticeboard.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
  public Account findByUserNameAndPassword(String userName, String password);
  public Account findByUserName(String userName);
}
