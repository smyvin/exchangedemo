package ru.smyvin.exchangedemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.smyvin.exchangedemo.domain.Order;
import ru.smyvin.exchangedemo.util.FileWriter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class OrderService implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private AccountStorage accountStorage;

    @Autowired
    private FileWriter resultFileWriter;

    @Value("${data.symbol:A,B,C,D}")
    private String[] symbols;

    @Value("${data.order}")
    private Resource orders;

    private Map<String, OrderBook> orderBooks = new HashMap<>();

    private int[] timestamp = {0};

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("OrderService init");


        initOrderBooks();
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner start(){
        return args -> {
            processOrders();
        };
    }

    public void processOrders() throws Exception {
        logger.info("Start");

        int[] timestamp = {0};
        try (Stream<String> stream = Files.lines(Paths.get(orders.getURI()))) {
            stream.forEach(line -> {
                try {
                    Order order = new Order(line, timestamp[0]++);
                    orderBooks.get(order.getSymbol()).process(order);
                }catch (ArrayIndexOutOfBoundsException e){
                    logger.error("Order line parse error: " + line);
                }
            });
        }

        resultFileWriter.write();

        logger.info("End");
        logger.info("Orders processed:" + timestamp[0]);
    }

    public void clean() throws Exception {
        timestamp[0] = 0;
        accountStorage.afterPropertiesSet();
        initOrderBooks();
    }

    private void initOrderBooks(){
        for (String symbol : symbols) {
            orderBooks.put(symbol, new OrderBook(symbol, eventPublisher));
        }
    }
}
