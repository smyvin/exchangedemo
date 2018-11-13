package ru.smyvin.exchangedemo.service;

import org.springframework.context.ApplicationEventPublisher;
import ru.smyvin.exchangedemo.domain.Order;
import ru.smyvin.exchangedemo.event.TransactionEvent;

import java.util.*;

import static ru.smyvin.exchangedemo.domain.Order.Type.BUY;

public class OrderBook {

    private final String symbol;
    private Map<Integer, List<Order>> buy = new TreeMap<>();
    private Map<Integer, List<Order>> sell = new TreeMap<>();

    private ApplicationEventPublisher eventPublisher;

    public OrderBook(String symbol, ApplicationEventPublisher eventPublisher) {
        this.symbol = symbol;
        this.eventPublisher = eventPublisher;
    }

    public void process(Order newOrder){
        Map<Integer, List<Order>> ordersMap = newOrder.getType() == BUY ?  buy : sell;
        Map<Integer, List<Order>> oppositeOrdersMap = newOrder.getType() == BUY ?  sell : buy;

        List<Order> oppositeOrders = oppositeOrdersMap.getOrDefault(newOrder.getPrice(), new ArrayList<>());
        Optional<Order> mayBeOppositeOrder = match(newOrder, oppositeOrders);

        if(mayBeOppositeOrder.isPresent()){
            Order oppositeOrder = mayBeOppositeOrder.get();
            TransactionEvent transactionEvent = new TransactionEvent(Arrays.asList(newOrder, oppositeOrder));
            oppositeOrders.remove(oppositeOrder);
            eventPublisher.publishEvent(transactionEvent);
        }else{
            List<Order> sameOrders = ordersMap.getOrDefault(newOrder.getPrice(), new ArrayList<>());
            sameOrders.add(newOrder);
            ordersMap.put(newOrder.getPrice(), sameOrders);
        }

    }

    private Optional<Order> match(Order newOrder, List<Order> oppositeOrders){
        return oppositeOrders.stream()
                .filter(order -> order.getQty().equals(newOrder.getQty()) && !order.getAccountId().equals(newOrder.getAccountId()))
                .min(Comparator.comparing(Order::getTimestamp));
    }
}
