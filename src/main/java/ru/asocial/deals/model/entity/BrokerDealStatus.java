package ru.asocial.deals.model.entity;

public enum BrokerDealStatus {

    NEW(2),
    CHECKED(3),
    REGISTERED(4),
    EXECUTED(5),
    ERROR(6);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private BrokerDealStatus(int id) {
        this.id = id;
    }

    private int id;
}
