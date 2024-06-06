package fontys.demo.business;

import fontys.demo.Persistence.Entity.SubscriptionEntity;
import fontys.demo.Persistence.Entity.UserEntity;
import fontys.demo.Persistence.impl.SubscriptionRepository;
import fontys.demo.Persistence.impl.UserJPARepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubscriptionService {


    private SubscriptionRepository subscriptionRepository;


    private UserJPARepository userRepository;

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
    }

    public void unsubscribe(Long userId, Long ptId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity pt = userRepository.findById(ptId).orElseThrow(() -> new RuntimeException("PT not found"));
        subscriptionRepository.deleteByUserAndPt(user, pt);
    }

    public List<SubscriptionEntity> listSubscriptions(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return subscriptionRepository.findByUser(user);
    }
}
