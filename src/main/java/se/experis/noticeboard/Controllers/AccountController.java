package se.experis.noticeboard.Controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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

        String hashedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(hashedPassword);

        try {
            Account newAccount = repo.save(account);
            status = newAccount != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
            SessionKeeper.getInstance().addSession(session.getId(), newAccount.getId());
        } catch(Exception e) {
            status = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(status);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Account account, HttpSession session) {
        HttpStatus status;
        
        // finds account by username and matches the typed password with the hashed account password
        Account foundAccount = repo.findByUserName(account.getUserName());
        if(foundAccount != null && passwordEncoder.matches(account.getPassword(), foundAccount.getPassword())) {
            SessionKeeper.getInstance().addSession(session.getId(), foundAccount.getId());
            // long tempId = SessionKeeper.getInstance().getSessionAccountId(session.getId());
            // System.out.println(tempId);
            
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.UNAUTHORIZED;
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
        return new ResponseEntity<>(SessionKeeper.getInstance().isLoggedIn(session.getId()), HttpStatus.OK);
    }

}
