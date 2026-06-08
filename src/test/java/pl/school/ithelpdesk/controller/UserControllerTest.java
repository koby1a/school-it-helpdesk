package pl.school.ithelpdesk.controller;

import org.junit.jupiter.api.Test;
import pl.school.ithelpdesk.dto.CreateUserRequest;
import pl.school.ithelpdesk.dto.UserResponse;
import pl.school.ithelpdesk.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private final UserService userService = mock(UserService.class);
    private final UserController userController = new UserController(userService);

    @Test
    void shouldCreateUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("john");
        request.setPassword("password123");

        UserResponse expectedResponse = new UserResponse(
                1L,
                "john",
                "USER"
        );

        when(userService.createUser(request)).thenReturn(expectedResponse);

        UserResponse response = userController.createUser(request);

        assertEquals(1L, response.getId());
        assertEquals("john", response.getUsername());
        assertEquals("USER", response.getRole());

        verify(userService).createUser(request);
    }
}