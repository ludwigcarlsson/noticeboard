package se.experis.noticeboard.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.experis.noticeboard.Repositories.UserRepository;
import se.experis.noticeboard.models.Account;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    private UserRepository repo;

    @PostMapping("/create")
    public ResponseEntity<Boolean> createUser(@RequestBody Account account) {

        account = repo.save(account);
        HttpStatus res = HttpStatus.OK;

        return new ResponseEntity<>(true, res);
    }
}
