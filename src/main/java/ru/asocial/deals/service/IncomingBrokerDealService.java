package ru.asocial.deals.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.asocial.deals.model.entity.BrokerDealEntity;
import ru.asocial.deals.model.entity.BrokerDealStatus;
import ru.asocial.deals.repo.BrokerDealRepository;

import java.time.LocalDateTime;

@Component
public class IncomingBrokerDealService {

    private static final Logger log = LoggerFactory.getLogger(IncomingBrokerDealService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BrokerDealRepository brokerDealRepository;

    @KafkaListener(topics = "broker-deals.in", groupId = "new-deals-consumers")
    @Transactional
    public void incomingBrokerDeal(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws Exception {
        String rawMessage = record.value();
        log.debug("received message: " + rawMessage);
        BrokerDealEntity entity = objectMapper.readValue(rawMessage, BrokerDealEntity.class);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setStatusId(BrokerDealStatus.NEW.getId());
        entity.setPartitionId(record.partition());
        entity.setOffset(record.offset());
        if (entity.getClientCode() == null) {
            throw new IllegalArgumentException("investorCode==null");
        }
        brokerDealRepository.save(entity);
        acknowledgment.acknowledge();
        log.debug("saved new deal (ON_CHECKING)");
    }
}
