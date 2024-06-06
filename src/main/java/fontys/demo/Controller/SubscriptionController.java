package fontys.demo.Controller;

import fontys.demo.Domain.Subscription.SubscriptionRequest;
import fontys.demo.Persistence.Entity.SubscriptionEntity;
import fontys.demo.business.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subscriptions")
@AllArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody SubscriptionRequest request) {
        subscriptionService.subscribe(request.getUserId(), request.getPtId());
        return ResponseEntity.ok("Subscribed successfully");
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestBody SubscriptionRequest request) {
        subscriptionService.unsubscribe(request.getUserId(), request.getPtId());
        return ResponseEntity.ok("Unsubscribed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Long>> listSubscriptions(@RequestParam Long userId) {
        List<SubscriptionEntity> subscriptions = subscriptionService.listSubscriptions(userId);
        List<Long> ptIds = subscriptions.stream()
                .map(subscription -> subscription.getPt().getId())
                .collect(Collectors.toList());
        return ResponseEntity.ok(ptIds);
    }
}
