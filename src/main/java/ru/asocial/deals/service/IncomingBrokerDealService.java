package ru.asocial.deals.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.DefaultApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
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

    private DefaultApi defaultApi;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public IncomingBrokerDealService(@Value("${deals.checker.rest.url}") String dealsCheckerRestUrl) {
        ApiClient apiClient = new ApiClient(new RestTemplate());
        apiClient.setBasePath(dealsCheckerRestUrl);
        defaultApi = new DefaultApi(apiClient);
    }

    @KafkaListener(topics = "broker-deals.in", groupId = "new-deals-consumers")
    @Transactional
    public void incomingBrokerDeal(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws Exception {
        String rawMessage = record.value();
        log.debug("received message: " + rawMessage);
        BrokerDealEntity entity = objectMapper.readValue(rawMessage, BrokerDealEntity.class);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setStatusId(BrokerDealStatus.NEW.getId());
        entity.setPartitionId(record.partition());
        if (entity.getClientCode() == null) {
            throw new IllegalArgumentException("investorCode==null");
        }
        brokerDealRepository.save(entity);
        log.debug("saved new deal (ON_CHECKING)");
        log.debug("checking deal: " + entity.getId());
        String result = defaultApi.checkDeal(entity.getDealDate(), entity.getClientCode(), entity.getSecurityCode());
        boolean stop = false;
        if ("OK".equals(result)) {
            entity.setStatusId(BrokerDealStatus.CHECKED.getId());
        }
        else {
            entity.setStatusId(BrokerDealStatus.ERROR.getId());
            entity.setReason(result);
            stop = true;
        }
        log.debug("deal check status: " + result);
        if (stop) {
            acknowledgment.acknowledge();
            return;
        }

        String message = objectMapper.writeValueAsString(entity);
        entity.setStatusId(BrokerDealStatus.REGISTERED.getId());
        kafkaTemplate.send("broker-deals.registered", record.partition(),  entity.getClientCode(), message);
        log.debug("deal {} was sent for execution", entity.getId());

        acknowledgment.acknowledge();
    }
}
