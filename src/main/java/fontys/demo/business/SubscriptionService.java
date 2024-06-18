package fontys.demo.business;

import fontys.demo.business.exceptions.AlreadySubscribedException;
import fontys.demo.business.exceptions.PTNotFoundException;
import fontys.demo.business.exceptions.UserNotFoundException;
import fontys.demo.persistence.entity.SubscriptionEntity;
import fontys.demo.persistence.entity.UserEntity;
import fontys.demo.persistence.impl.SubscriptionRepository;
import fontys.demo.persistence.impl.UserJPARepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubscriptionService {

    private static final String USER_NOT_FOUND_MSG = "User not found with ID: ";

    private final SubscriptionRepository subscriptionRepository;
    private final UserJPARepository userRepository;
    private final NotificationService notificationService;

    public void subscribe(Long userId, Long ptId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MSG + userId));
        UserEntity pt = userRepository.findById(ptId).orElseThrow(() -> new PTNotFoundException("PT not found with ID: " + ptId));
        if (subscriptionRepository.existsByUserAndPt(user, pt)) {
            throw new AlreadySubscribedException("Already subscribed");
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
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MSG + userId));
        UserEntity pt = userRepository.findById(ptId).orElseThrow(() -> new PTNotFoundException("PT not found with ID: " + ptId));
        subscriptionRepository.deleteByUserAndPt(user, pt);

        notificationService.sendUnsubscriptionNotification(userId, ptId);
    }

    public List<SubscriptionEntity> listSubscriptions(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MSG + userId));
        return subscriptionRepository.findByUser(user);
    }
}
