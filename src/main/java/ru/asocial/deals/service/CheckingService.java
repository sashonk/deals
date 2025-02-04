package ru.asocial.deals.service;

import jakarta.transaction.Transactional;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.DefaultApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.asocial.deals.model.entity.BrokerDealEntity;
import ru.asocial.deals.model.entity.BrokerDealStatus;
import ru.asocial.deals.repo.BrokerDealRepository;

@Component
public class CheckingService {

    private static final Logger log = LoggerFactory.getLogger(CheckingService.class);

    @Autowired
    private BrokerDealRepository brokerDealRepository;

    private DefaultApi defaultApi;

    public CheckingService(@Value("${deals.checker.rest.url}") String dealsCheckerRestUrl) {
        ApiClient apiClient = new ApiClient(new RestTemplate());
        apiClient.setBasePath(dealsCheckerRestUrl);
        defaultApi = new DefaultApi(apiClient);
    }

    @Transactional
    public void process() {
        log.debug("query new deals");
        for (BrokerDealEntity brokerDealEntity: brokerDealRepository.findByStatusId(BrokerDealStatus.NEW.getId())) {
            processBrokerDeal(brokerDealEntity);
        }
    }

    private void processBrokerDeal(BrokerDealEntity entity) {
        log.debug("checking deal: " + entity.getId());
        String result = defaultApi.checkDeal(entity.getDealDate(), entity.getClientCode(), entity.getSecurityCode());
        if ("OK".equals(result)) {
            entity.setStatusId(BrokerDealStatus.CHECKED.getId());
        }
        else {
            entity.setStatusId(BrokerDealStatus.ERROR.getId());
            entity.setReason(result);
        }
        log.debug("deal check status: " + result);
    }
}
