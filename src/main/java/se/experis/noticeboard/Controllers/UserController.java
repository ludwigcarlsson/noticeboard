package se.experis.noticeboard.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.experis.noticeboard.Repositories.UserRepository;
import se.experis.noticeboard.models.User;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserRepository repo;

    @PostMapping("/create")
    public boolean createUser(@RequestBody User user) {

        user = repo.save(user);

        return true;
    }
}
