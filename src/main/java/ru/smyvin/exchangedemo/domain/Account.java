package ru.smyvin.exchangedemo.domain;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Account {
    private String name;
    private Integer balanceDollar;
    private Map<String,Integer> balances;
    private final String TAB_DELIMITER = "\t";

    public Account(String data, String[] symbols) {
        String[] dataFields = data.split("\t");
        name = dataFields[0];
        balanceDollar = Integer.valueOf(dataFields[1]);
        balances = new LinkedHashMap<>();

        int i = 2;
        for (String symbol : symbols) {
            this.balances.put(symbol, Integer.valueOf(dataFields[i++]));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBalanceDollar() {
        return balanceDollar;
    }

    public void setBalanceDollar(Integer amount) {
        this.balanceDollar = balanceDollar + amount;
    }

    public Integer getBalance(String symbol) {
        return balances.get(symbol);
    }

    public void setBalance(String symbol, Integer amount) {
        this.balances.compute(symbol, (k,v) -> v + amount);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name + TAB_DELIMITER + balanceDollar + TAB_DELIMITER);
        balances.values().forEach(balance -> builder.append(balance).append(TAB_DELIMITER));
        return builder.toString();
    }
}
