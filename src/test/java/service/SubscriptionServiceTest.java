package service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fontys.demo.business.exceptions.PTNotFoundException;
import fontys.demo.business.exceptions.UserNotFoundException;
import fontys.demo.persistence.entity.SubscriptionEntity;
import fontys.demo.persistence.entity.UserEntity;
import fontys.demo.persistence.impl.SubscriptionRepository;
import fontys.demo.persistence.impl.UserJPARepository;
import fontys.demo.business.NotificationService;
import fontys.demo.business.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

 class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepositoryMock;

    @Mock
    private UserJPARepository userRepositoryMock;

    @Mock
    private NotificationService notificationServiceMock;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testSubscribe_Success() {
        // Arrange
        Long userId = 1L;
        Long ptId = 2L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        UserEntity pt = new UserEntity();
        pt.setId(ptId);

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));
        when(userRepositoryMock.findById(ptId)).thenReturn(Optional.of(pt));
        when(subscriptionRepositoryMock.existsByUserAndPt(user, pt)).thenReturn(false);

        // Act
        subscriptionService.subscribe(userId, ptId);

        // Assert
        verify(subscriptionRepositoryMock, times(1)).save(any(SubscriptionEntity.class));
        verify(notificationServiceMock, times(1)).sendSubscriptionNotification(userId, ptId);
    }

     @Test
     void testSubscribe_UserNotFound() {
         // Arrange
         Long userId = 1L;
         Long ptId = 2L;

         when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

         // Act & Assert
         UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> subscriptionService.subscribe(userId, ptId));

         assertEquals("User not found with ID: " + userId, exception.getMessage());
     }
    @Test
     void testSubscribe_AlreadySubscribed() {
        // Arrange
        Long userId = 1L;
        Long ptId = 2L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        UserEntity pt = new UserEntity();
        pt.setId(ptId);

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));
        when(userRepositoryMock.findById(ptId)).thenReturn(Optional.of(pt));
        when(subscriptionRepositoryMock.existsByUserAndPt(user, pt)).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> subscriptionService.subscribe(userId, ptId));

        assertEquals("Already subscribed", exception.getMessage());
    }

    @Test
     void testUnsubscribe_Success() {
        // Arrange
        Long userId = 1L;
        Long ptId = 2L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        UserEntity pt = new UserEntity();
        pt.setId(ptId);

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));
        when(userRepositoryMock.findById(ptId)).thenReturn(Optional.of(pt));

        // Act
        subscriptionService.unsubscribe(userId, ptId);

        // Assert
        verify(subscriptionRepositoryMock, times(1)).deleteByUserAndPt(user, pt);
        verify(notificationServiceMock, times(1)).sendUnsubscriptionNotification(userId, ptId);
    }

     @Test
     void testUnsubscribe_UserNotFound() {
         // Arrange
         Long userId = 1L;
         Long ptId = 2L;

         when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

         // Act & Assert
         UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> subscriptionService.unsubscribe(userId, ptId));

         assertEquals("User not found with ID: " + userId, exception.getMessage());
     }

    @Test
     void testListSubscriptions_Success() {
        // Arrange
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setUser(user);
        List<SubscriptionEntity> subscriptions = Collections.singletonList(subscription);

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));
        when(subscriptionRepositoryMock.findByUser(user)).thenReturn(subscriptions);

        // Act
        List<SubscriptionEntity> result = subscriptionService.listSubscriptions(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0).getUser());
    }
     @Test
     void testSubscribe_PTNotFound() {
         // Arrange
         Long userId = 1L;
         Long ptId = 2L;

         UserEntity user = new UserEntity();
         user.setId(userId);

         when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));
         when(userRepositoryMock.findById(ptId)).thenReturn(Optional.empty());

         // Act & Assert
         PTNotFoundException exception = assertThrows(PTNotFoundException.class, () -> {
             subscriptionService.subscribe(userId, ptId);
         });

         assertEquals("PT not found with ID: " + ptId, exception.getMessage());
     }

     @Test
     void testListSubscriptions_UserNotFound() {
         // Arrange
         Long userId = 1L;

         when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

         // Act & Assert
         UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> subscriptionService.listSubscriptions(userId));

         assertEquals("User not found with ID: " + userId, exception.getMessage());
     }
}
