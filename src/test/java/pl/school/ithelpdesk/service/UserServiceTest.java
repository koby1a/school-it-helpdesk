package pl.school.ithelpdesk.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.school.ithelpdesk.dto.CreateUserRequest;
import pl.school.ithelpdesk.dto.UserResponse;
import pl.school.ithelpdesk.entity.Role;
import pl.school.ithelpdesk.entity.User;
import pl.school.ithelpdesk.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserWithUserRoleAndEncodedPassword() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("john");
        request.setPassword("password123");

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserResponse response = userService.createUser(request);

        assertEquals(1L, response.getId());
        assertEquals("john", response.getUsername());
        assertEquals("USER", response.getRole());

        verify(passwordEncoder).encode("password123");

        verify(userRepository).save(argThat(user ->
                user.getUsername().equals("john")
                        && user.getPassword().equals("encodedPassword")
                        && user.getRole() == Role.USER
        ));
    }
}