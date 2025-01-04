package ru.asocial.deals.scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.asocial.deals.service.CheckingService;
import ru.asocial.deals.service.RegistrationService;

@Component
public class SchedulingService {

    private static final Logger log = LoggerFactory.getLogger(SchedulingService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CheckingService checkingService;

    @Autowired
    private RegistrationService registrationService;

    @Scheduled(cron = "*/5 * * * * *")
    public void processOnChecking() {
        checkingService.process();
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void processChecked() throws Exception {
        registrationService.process();
    }

}
