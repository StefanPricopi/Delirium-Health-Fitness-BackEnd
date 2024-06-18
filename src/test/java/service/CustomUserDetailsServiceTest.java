package service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fontys.demo.business.exceptions.PTNotFoundException;
import fontys.demo.business.exceptions.UnauthorizedAccessException;
import fontys.demo.persistence.entity.UserEntity;
import fontys.demo.persistence.impl.UserJPARepository;
import fontys.demo.business.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

 class CustomUserDetailsServiceTest {

    @Mock
    private UserJPARepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testLoadUserByUsername_Success() {
        // Given
        String username = "testuser";
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername(username);
        userEntity.setPassword("password");
        userEntity.setRoles("ROLE_USER");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Then
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
     void testLoadUserByUsername_UserNotFound() {
        // Given
        String username = "nonexistentuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(username));

        assertEquals("User not found with username: " + username, exception.getMessage());
    }
     @Test
      void testUnauthorizedAccessException() {
         String errorMessage = "You do not have permission to modify this workout plan.";
         UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
             throw new UnauthorizedAccessException(errorMessage);
         });
         assertEquals(errorMessage, exception.getMessage());
     }
     @Test
      void testPTNotFoundException() {
         String errorMessage = "PT not found with ID: 1";
         PTNotFoundException exception = assertThrows(PTNotFoundException.class, () -> {
             throw new PTNotFoundException(errorMessage);
         });
         assertEquals(errorMessage, exception.getMessage());
     }
}
