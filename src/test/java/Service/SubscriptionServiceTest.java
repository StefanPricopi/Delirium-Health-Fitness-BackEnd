package Service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fontys.demo.Persistence.Entity.SubscriptionEntity;
import fontys.demo.Persistence.Entity.UserEntity;
import fontys.demo.Persistence.impl.SubscriptionRepository;
import fontys.demo.Persistence.impl.UserJPARepository;
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

public class SubscriptionServiceTest {

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
    public void testSubscribe_Success() {
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
    public void testSubscribe_UserNotFound() {
        // Arrange
        Long userId = 1L;
        Long ptId = 2L;

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> subscriptionService.subscribe(userId, ptId));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testSubscribe_AlreadySubscribed() {
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
    public void testUnsubscribe_Success() {
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
    public void testUnsubscribe_UserNotFound() {
        // Arrange
        Long userId = 1L;
        Long ptId = 2L;

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> subscriptionService.unsubscribe(userId, ptId));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testListSubscriptions_Success() {
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
    public void testListSubscriptions_UserNotFound() {
        // Arrange
        Long userId = 1L;

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> subscriptionService.listSubscriptions(userId));

        assertEquals("User not found", exception.getMessage());
    }
}
