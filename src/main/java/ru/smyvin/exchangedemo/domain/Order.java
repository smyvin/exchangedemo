package ru.smyvin.exchangedemo.domain;

public class Order {

    private Type type;
    private String accountId;
    private String symbol;
    private Integer price;
    private Integer qty;
    private Integer timestamp;

    public Order(String data, Integer timestamp){
        String[] dataFields = data.split("\t");
        accountId = dataFields[0];
        type = Type.fromShortName(dataFields[1]);
        symbol = dataFields[2];
        price = Integer.valueOf(dataFields[3]);
        qty = Integer.valueOf(dataFields[4]);
        this.timestamp = timestamp;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public enum Type {
        SELL("s", 1, -1), BUY("b", -1, 1);

        private String shortName;
        private Integer dollarMultiplier;
        private Integer multiplier;

        Type(String shortName, Integer dollarMultiplier, Integer multiplier) {
            this.shortName = shortName;
            this.dollarMultiplier = dollarMultiplier;
            this.multiplier = multiplier;
        }

        public Integer getDollarMultiplier() {
            return dollarMultiplier;
        }

        public Integer getMultiplier() {
            return multiplier;
        }

        public static Type fromShortName(String shortName) {
            for (Type type : Type.values()) {
                if (type.shortName.equals(shortName)) {
                    return type;
                }
            }
            return null;
        }
    }
}
