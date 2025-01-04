package ru.asocial.deals.model.entity;

import java.time.LocalDateTime;

public class ExecutionResult {

    private Long id;

    private Long brokerDealId;

    private String result;

    private LocalDateTime executedAt;

    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBrokerDealId() {
        return brokerDealId;
    }

    public void setBrokerDealId(Long brokerDealId) {
        this.brokerDealId = brokerDealId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }
}
