package ru.asocial.deals.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.asocial.deals.model.entity.BrokerDealEntity;
import ru.asocial.deals.model.entity.BrokerDealStatus;
import ru.asocial.deals.repo.BrokerDealRepository;

@Component
public class RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BrokerDealRepository brokerDealRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void process() throws Exception {
        log.debug("query checked deals");
        for (BrokerDealEntity brokerDealEntity: brokerDealRepository.findByStatusId(BrokerDealStatus.CHECKED.getId())) {
            processBrokerDeal(brokerDealEntity);
        }
    }

    private void processBrokerDeal(BrokerDealEntity entity) throws Exception {
        String message = objectMapper.writeValueAsString(entity);
        entity.setStatusId(BrokerDealStatus.REGISTERED.getId());
        kafkaTemplate.send("broker-deals.registered", entity.getClientCode(), message);
        log.debug("deal {} was sent for execution", entity.getId());
    }
}
