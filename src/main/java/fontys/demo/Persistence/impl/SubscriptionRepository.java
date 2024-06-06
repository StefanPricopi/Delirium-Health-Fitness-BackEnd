package fontys.demo.Persistence.impl;


import fontys.demo.Domain.UserDomain.User;
import fontys.demo.Persistence.Entity.SubscriptionEntity;
import fontys.demo.Persistence.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByUser(UserEntity user);
    List<SubscriptionEntity> findByPt(UserEntity pt);
    void deleteByUserAndPt(UserEntity user, UserEntity pt);
    boolean existsByUserAndPt(UserEntity user, UserEntity pt);
}
