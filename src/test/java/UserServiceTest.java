import fontys.demo.Domain.UserDomain.*;
import fontys.demo.Persistence.Entity.UserEntity;
import fontys.demo.Persistence.impl.UserJPARepository;
import fontys.demo.business.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserJPARepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {
        // Arrange
        long userId = 1L;
        UserEntity userEntity = new UserEntity(userId, "testuser", "test@example.com", "ROLE_USER", "encrypted_password");
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Act
        GetUserResponse response = userService.getUserById(userId);

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());

    }


    @Test
    void testGetAllUsers() {
        UserEntity user1 = new UserEntity(1L, "user1", "user1@example.com", "ROLE_USER", "encrypted_password");
        UserEntity user2 = new UserEntity(2L, "user2", "user2@example.com", "ROLE_USER", "encrypted_password");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<GetUserResponse> responses = userService.getAllUsers();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("user1", responses.get(0).getUsername());
        assertEquals("user2", responses.get(1).getUsername());
    }

    @Test
    void testCreateUser() {
        CreateUserRequest request = new CreateUserRequest("newuser", "newuser@example.com", "NewPassword123!", "ROLE_USER");
        UserEntity savedEntity = new UserEntity(1L, "newuser", "newuser@example.com", "ROLE_USER", "encrypted_password");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encrypted_password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

        CreateUserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("User created successfully", response.getMessage());
        assertEquals(1L, response.getUserId());
    }

    @Test
    void testUpdateUser() {
        UserEntity existingUser = new UserEntity(1L, "olduser", "olduser@example.com", "ROLE_USER", "old_password");
        UpdateUserRequest request = new UpdateUserRequest(1L, "updateduser", "UpdatedPassword123!", "updated@example.com", "ROLE_USER");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encrypted_password");

        UpdateUserResponse response = userService.updateUser(1L, request);

        assertNotNull(response);
        assertEquals("User updated successfully", response.getMessage());
        assertEquals("updateduser", response.getUsername());
        assertEquals("updated@example.com", response.getEmail());
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }


}
