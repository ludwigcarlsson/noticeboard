package se.experis.noticeboard.Controllers;

import javax.servlet.http.HttpSession;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.experis.noticeboard.Repositories.AccountRepository;
import se.experis.noticeboard.Utils.SessionKeeper;
import se.experis.noticeboard.models.Account;

@RestController
@RequestMapping(value = "/api/v1/account")
public class AccountController {

    @Autowired
    private AccountRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<Void> createAccount(@RequestBody Account account, HttpSession session) {
        HttpStatus status;

        try {
            String hashedPassword = passwordEncoder.encode(account.getPassword());
            account.setPassword(hashedPassword);

            Account newAccount = repo.save(account);
            status = newAccount != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
            SessionKeeper.getInstance().addSession(session.getId(), newAccount.getId());
        } catch(DataIntegrityViolationException | IllegalArgumentException e) {
            // dealing with nested exception to return correct error code
            if(e.getCause() instanceof PropertyValueException || e.getCause() == null) {
                status = HttpStatus.BAD_REQUEST;
            } else if(e.getCause() instanceof ConstraintViolationException) {
                status = HttpStatus.CONFLICT;
            } else {
                status = HttpStatus.I_AM_A_TEAPOT; // should not get here
            }
        } catch(Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(status);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Account account, HttpSession session) {
        HttpStatus status;
        
        // finds account by username and matches the typed password with the hashed account password
        try {
            Account foundAccount = repo.findByUserName(account.getUserName());
            if(foundAccount != null && passwordEncoder.matches(account.getPassword(), foundAccount.getPassword())) {
                SessionKeeper.getInstance().addSession(session.getId(), foundAccount.getId());
                status = HttpStatus.OK;
            } else {
                status = HttpStatus.UNAUTHORIZED;
            }
        } catch(Exception e) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }
    
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        SessionKeeper.getInstance().removeSession(session.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping("/loginStatus")
    public ResponseEntity<Boolean> loginStatus(HttpSession session) {
        return new ResponseEntity<>( SessionKeeper.getInstance().isLoggedIn(session.getId()), HttpStatus.OK);
    }
    
    
    @GetMapping("/id")
    public ResponseEntity<Long> accountId(HttpSession session) {
        Long accountId = SessionKeeper.getInstance().getSessionAccountId(session.getId());
        return new ResponseEntity<>(accountId, accountId != null ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }
}
