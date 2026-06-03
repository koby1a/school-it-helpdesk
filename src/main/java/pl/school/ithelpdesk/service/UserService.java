package pl.school.ithelpdesk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.school.ithelpdesk.entity.Role;
import pl.school.ithelpdesk.entity.User;
import pl.school.ithelpdesk.repository.UserRepository;
import pl.school.ithelpdesk.dto.CreateUserRequest;
import pl.school.ithelpdesk.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request) {

        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole().name()
        );
    }
}