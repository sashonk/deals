package ru.asocial.deals.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.asocial.deals.model.entity.BrokerDealEntity;
import ru.asocial.deals.model.entity.BrokerDealStatus;
import ru.asocial.deals.model.entity.ExecutionResult;
import ru.asocial.deals.repo.BrokerDealRepository;

import java.time.LocalDateTime;

@Component
public class ExecutionResultProcessor {

    private static final Logger log = LoggerFactory.getLogger(ExecutionResultProcessor.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BrokerDealRepository brokerDealRepository;

    @Transactional
    @KafkaListener(topics = "execution.results", groupId = "execution-results-consumers")
    public void onMessage(String executionResultMsg, Acknowledgment acknowledgment) throws Exception {
        ExecutionResult executionResult = objectMapper.readValue(executionResultMsg, ExecutionResult.class);
        BrokerDealEntity deal = brokerDealRepository.findById(executionResult.getBrokerDealId()).orElseThrow();
        if ("SUCCESS".equals(executionResult.getResult())) {
            deal.setStatusId(BrokerDealStatus.EXECUTED.getId());
            log.debug("deal executed successfully: " + deal.getId());
        }
        else {
            deal.setStatusId(BrokerDealStatus.ERROR.getId());
            deal.setReason(executionResult.getResult());
            log.debug("deal executed with error: " + deal.getId());
        }
        deal.setExecutedAt(LocalDateTime.now());
        acknowledgment.acknowledge();
    }
}
