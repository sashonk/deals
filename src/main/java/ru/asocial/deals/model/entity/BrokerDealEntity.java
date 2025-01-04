package ru.asocial.deals.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "broker_deal")
public class BrokerDealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "broker_product_type_id")
    private Integer brokerProductTypeId;

    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "trade_number")
    private String tradeNumber;

    @Column(name = "date_form")
    private LocalDate dateForm;

    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "security_code")
    private String securityCode;

    @Column(name = "securities_quantity")
    private BigDecimal securitiesQuantity;

    @Column(name = "deal_date")
    private LocalDate dealDate;

    @Column(name = "executed_at")
    private LocalDateTime executedAt;

    @Column
    private String reason;

    @Column(name = "partition_id")
    private Integer partitionId;

    @Column
    private Long offset;

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Integer getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(Integer partitionId) {
        this.partitionId = partitionId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getBrokerProductTypeId() {
        return brokerProductTypeId;
    }

    public void setBrokerProductTypeId(Integer brokerProductTypeId) {
        this.brokerProductTypeId = brokerProductTypeId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getTradeNumber() {
        return tradeNumber;
    }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public BigDecimal getSecuritiesQuantity() {
        return securitiesQuantity;
    }

    public void setSecuritiesQuantity(BigDecimal securitiesQuantity) {
        this.securitiesQuantity = securitiesQuantity;
    }

    public LocalDate getDateForm() {
        return dateForm;
    }

    public void setDateForm(LocalDate dateForm) {
        this.dateForm = dateForm;
    }

    public LocalDate getDealDate() {
        return dealDate;
    }

    public void setDealDate(LocalDate dealDate) {
        this.dealDate = dealDate;
    }
}
