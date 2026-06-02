package pl.school.ithelpdesk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.school.ithelpdesk.entity.User;
import pl.school.ithelpdesk.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(
            @RequestParam String username,
            @RequestParam String password
    ) {
        return userService.createUser(username, password);
    }
}