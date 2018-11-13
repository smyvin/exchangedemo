package ru.smyvin.exchangedemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.smyvin.exchangedemo.domain.Account;
import ru.smyvin.exchangedemo.domain.Order;
import ru.smyvin.exchangedemo.event.TransactionEvent;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AccountStorage implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, Account> accounts = new LinkedHashMap<>();

    @Value("${data.client}")
    private Resource clientsFile;

    @Value("${data.symbol:A,B,C,D}")
    private String[] symbols;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("AccountStorage init");
        try (Stream<String> stream = Files.lines(Paths.get(clientsFile.getURI()))) {
            stream.forEach(line -> addAccount(new Account(line, symbols)));
        }
    }

    @EventListener
    public void onEvent(TransactionEvent event) {
        for(Order order: event.getOrders()){
            String accountId = order.getAccountId();
            Account account = accounts.get(accountId);
            account.setBalanceDollar(order.getQty() * order.getPrice() * order.getType().getDollarMultiplier());
            account.setBalance(order.getSymbol(), order.getQty() * order.getType().getMultiplier());
        }
    }

    public void addAccount(Account account){
        accounts.put(account.getName(), account);
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public List<String> toFile(){
        return this.accounts.values().stream().map(Account::toString).collect(Collectors.toList());
    }

}
