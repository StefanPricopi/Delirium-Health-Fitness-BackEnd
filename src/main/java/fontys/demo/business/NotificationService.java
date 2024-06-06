package fontys.demo.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendSubscriptionNotification(Long userId, Long ptId) {
        messagingTemplate.convertAndSend("/topic/subscribe", "User " + userId + " subscribed to PT " + ptId);
    }

    public void sendUnsubscriptionNotification(Long userId, Long ptId) {
        messagingTemplate.convertAndSend("/topic/unsubscribe", "User " + userId + " unsubscribed from PT " + ptId);
    }
}
