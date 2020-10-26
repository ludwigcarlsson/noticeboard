package se.experis.noticeboard.Controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.experis.noticeboard.Repositories.AccountRepository;
import se.experis.noticeboard.models.Account;

@RestController
@RequestMapping(value = "/api/v1/account")
public class AccountController {

    private static String sessionId = "sessionId";

    @Autowired
    private AccountRepository repo;

    @PostMapping("/create")
    public ResponseEntity<Void> createAccount(@RequestBody Account account, HttpSession session) {
        HttpStatus status;
        status = repo.save(account) != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        session.setAttribute(sessionId, session.getId());
        return new ResponseEntity<>(status);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Account account, HttpSession session) {
        HttpStatus status;
        
        Account acc = repo.findByUserNameAndPassword(account.getUserName(), account.getPassword());

        if(acc != null) {
            session.setAttribute(sessionId, session.getId());
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }
    
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.removeAttribute(sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
  
    @GetMapping("/loginStatus")
    public ResponseEntity<Boolean> loginStatus(HttpSession session) {
        return new ResponseEntity<>(session.getAttribute(sessionId) != null, HttpStatus.OK);
    }

}
