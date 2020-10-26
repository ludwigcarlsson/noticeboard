package se.experis.noticeboard.Controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.experis.noticeboard.Repositories.AccountRepository;
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
        // hashes password
        String hashedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(hashedPassword);

        status = repo.save(account) != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        session.setAttribute(session.getId(), session.getId());
        return new ResponseEntity<>(status);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Account account, HttpSession session) {
        HttpStatus status;

        // finds account by username and matches the typed password with the hashed account password
        Account found = repo.findByUserName(account.getUserName());
        boolean matches = passwordEncoder.matches(account.getPassword(), found.getPassword());
        
        if(matches) {
            session.setAttribute(session.getId(), session.getId());
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(status);
    }
    
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.removeAttribute(session.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
  
    @GetMapping("/loginStatus")
    public ResponseEntity<Boolean> loginStatus(HttpSession session) {
        return new ResponseEntity<>(session.getAttribute(session.getId()) != null, HttpStatus.OK);
    }

}
