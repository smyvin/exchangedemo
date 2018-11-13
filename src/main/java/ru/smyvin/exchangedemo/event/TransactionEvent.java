package ru.smyvin.exchangedemo.event;

import ru.smyvin.exchangedemo.domain.Order;

import java.util.List;

public class TransactionEvent {

    private List<Order> orders;

    public TransactionEvent(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
