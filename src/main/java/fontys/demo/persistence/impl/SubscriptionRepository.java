package fontys.demo.persistence.impl;


import fontys.demo.persistence.entity.SubscriptionEntity;
import fontys.demo.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByUser(UserEntity user);

    void deleteByUserAndPt(UserEntity user, UserEntity pt);
    boolean existsByUserAndPt(UserEntity user, UserEntity pt);
}
