package fontys.demo.business;

import fontys.demo.Persistence.Entity.SubscriptionEntity;
import fontys.demo.Persistence.Entity.UserEntity;
import fontys.demo.Persistence.impl.SubscriptionRepository;
import fontys.demo.Persistence.impl.UserJPARepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserJPARepository userRepository;
    private final NotificationService notificationService;



    public void subscribe(Long userId, Long ptId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity pt = userRepository.findById(ptId).orElseThrow(() -> new RuntimeException("PT not found"));
        if (subscriptionRepository.existsByUserAndPt(user, pt)) {
            throw new RuntimeException("Already subscribed");
        }
        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .user(user)
                .pt(pt)
                .build();
        subscriptionRepository.save(subscription);

        notificationService.sendSubscriptionNotification(userId, ptId);
    }

    @Transactional
    public void unsubscribe(Long userId, Long ptId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity pt = userRepository.findById(ptId).orElseThrow(() -> new RuntimeException("PT not found"));
        subscriptionRepository.deleteByUserAndPt(user, pt);

        notificationService.sendUnsubscriptionNotification(userId, ptId);
    }

    public List<SubscriptionEntity> listSubscriptions(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return subscriptionRepository.findByUser(user);
    }
}
