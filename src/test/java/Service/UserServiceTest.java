package Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fontys.demo.Domain.Login.LoginRequest;
import fontys.demo.Domain.Login.LoginResponse;
import fontys.demo.Security.Token.AccessToken;
import fontys.demo.Security.Token.impl.AccessTokenImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import fontys.demo.Domain.UserDomain.*;
import fontys.demo.Persistence.Entity.UserEntity;
import fontys.demo.Persistence.impl.UserJPARepository;
import fontys.demo.Security.Token.AccessTokenEncoder;
import fontys.demo.business.UserService;

class UserServiceTest {

    @Mock
    private UserJPARepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

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
    void testGetUserById_NotFound() {
        // Arrange
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(userId);
        });
        assertEquals("User not found with id: 1", thrown.getMessage());
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
    void testCreateUser_Failure() {
        CreateUserRequest request = new CreateUserRequest("newuser", "newuser@example.com", "NewPassword123!", "ROLE_USER");

        when(passwordEncoder.encode(request.getPassword())).thenThrow(new RuntimeException("Encryption failed"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.createUser(request);
        });
        assertEquals("Encryption failed", thrown.getMessage());
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
    void testUpdateUser_NotFound() {
        UpdateUserRequest request = new UpdateUserRequest(1L, "updateduser", "UpdatedPassword123!", "updated@example.com", "ROLE_USER");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L, request);
        });
        assertEquals("User not found with id: 1", thrown.getMessage());
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(1L);
        });
        assertEquals("User not found with id: 1", thrown.getMessage());
    }


    @Test
    void testLogin_UserNotFound() {
        LoginRequest request = new LoginRequest("testuser", "password123");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.login(request);
        });
        assertEquals("Invalid username or password", thrown.getMessage());
    }

    @Test
    void testLogin_PasswordMismatch() {
        LoginRequest request = new LoginRequest("testuser", "wrongpassword");
        UserEntity userEntity = new UserEntity(1L, "testuser", "test@example.com", "ROLE_USER", passwordEncoder.encode("password123"));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("wrongpassword", userEntity.getPassword())).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.login(request);
        });
        assertEquals("Invalid username or password", thrown.getMessage());
    }

    // New Test: Update User Password
    @Test
    void testUpdateUserPassword() {
        long userId = 1L;
        UserEntity existingUser = new UserEntity(userId, "olduser", "olduser@example.com", "ROLE_USER", "old_password");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword123")).thenReturn("new_encrypted_password");

        UpdateUserRequest request = new UpdateUserRequest(userId, "olduser", "newpassword123", "olduser@example.com", "ROLE_USER");

        UpdateUserResponse response = userService.updateUser(userId, request);

        assertNotNull(response);
        assertEquals("User updated successfully", response.getMessage());
        assertEquals("olduser", response.getUsername());
        assertEquals("olduser@example.com", response.getEmail());
        verify(userRepository).save(existingUser);
        assertEquals("new_encrypted_password", existingUser.getPassword());
    }
}
