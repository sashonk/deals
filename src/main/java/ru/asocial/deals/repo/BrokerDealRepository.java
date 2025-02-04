package ru.asocial.deals.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.asocial.deals.model.entity.BrokerDealEntity;

import java.util.List;

@Repository
public interface BrokerDealRepository extends JpaRepository<BrokerDealEntity, Long> {

    @Query(nativeQuery = true, value = "select * from broker_deal where status_id = :statusId for update limit 100")
    List<BrokerDealEntity> findByStatusId(Integer statusId);
}
