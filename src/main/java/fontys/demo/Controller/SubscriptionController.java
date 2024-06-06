package fontys.demo.Controller;


import fontys.demo.Domain.Subscription.SubscriptionRequest;
import fontys.demo.Domain.Subscription.SubscriptionResponse;
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
    public ResponseEntity<SubscriptionResponse> subscribe(@RequestBody SubscriptionRequest request) {
        subscriptionService.subscribe(request.getUserId(), request.getPtId());
        SubscriptionResponse response = new SubscriptionResponse(null, request.getUserId(), request.getPtId(), "Subscribed successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<SubscriptionResponse> unsubscribe(@RequestBody SubscriptionRequest request) {
        subscriptionService.unsubscribe(request.getUserId(), request.getPtId());
        SubscriptionResponse response = new SubscriptionResponse(null, request.getUserId(), request.getPtId(), "Unsubscribed successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<SubscriptionResponse>> listSubscriptions(@RequestParam Long userId) {
        List<SubscriptionEntity> subscriptions = subscriptionService.listSubscriptions(userId);
        List<SubscriptionResponse> response = subscriptions.stream()
                .map(sub -> new SubscriptionResponse(sub.getId(), sub.getUser().getId(), sub.getPt().getId(), null))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
