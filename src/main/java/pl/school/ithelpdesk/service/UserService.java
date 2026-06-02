package pl.school.ithelpdesk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.school.ithelpdesk.entity.Role;
import pl.school.ithelpdesk.entity.User;
import pl.school.ithelpdesk.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String username, String password) {

        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setRole(Role.USER);

        return userRepository.save(user);
    }
}