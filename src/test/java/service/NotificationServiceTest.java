package service;

import static org.mockito.Mockito.*;

import fontys.demo.business.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

 class NotificationServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testSendSubscriptionNotification() {
        // Arrange
        Long userId = 1L;
        Long ptId = 2L;
        String expectedMessage = "User " + userId + " subscribed to PT " + ptId;

        // Act
        notificationService.sendSubscriptionNotification(userId, ptId);

        // Assert
        verify(messagingTemplate, times(1)).convertAndSend("/topic/subscribe", expectedMessage);
    }

    @Test
     void testSendUnsubscriptionNotification() {
        // Arrange
        Long userId = 1L;
        Long ptId = 2L;
        String expectedMessage = "User " + userId + " unsubscribed from PT " + ptId;

        // Act
        notificationService.sendUnsubscriptionNotification(userId, ptId);

        // Assert
        verify(messagingTemplate, times(1)).convertAndSend("/topic/unsubscribe", expectedMessage);
    }
}
