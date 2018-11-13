package ru.smyvin.exchangedemo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.smyvin.exchangedemo.domain.Account;
import ru.smyvin.exchangedemo.service.AccountStorage;
import ru.smyvin.exchangedemo.service.OrderService;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"data.order=classpath:data/orders-test-2.txt"})
@ActiveProfiles("test")
public class ExchangeDemoApplicationBuySelfTests {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OrderService orderService;

	@Autowired
	private AccountStorage accountStorage;

	@Before
	public void init() throws Exception {
		orderService.processOrders();
	}

	@After
	public void clean() throws Exception{
		orderService.clean();
	}

	@Test
	public void balances(){

		Account c1 = accountStorage.getAccounts().get("C1");
		Account c2 = accountStorage.getAccounts().get("C2");
		Account c3 = accountStorage.getAccounts().get("C3");

		assertThat(c1.getBalanceDollar()).isEqualTo(960);
		assertThat(c2.getBalanceDollar()).isEqualTo(1000);
		assertThat(c3.getBalanceDollar()).isEqualTo(1040);

		assertThat(c1.getBalance("A")).isEqualTo(104);
		assertThat(c1.getBalance("B")).isEqualTo(100);
		assertThat(c1.getBalance("C")).isEqualTo(100);
		assertThat(c1.getBalance("D")).isEqualTo(100);

		assertThat(c2.getBalance("A")).isEqualTo(100);
		assertThat(c2.getBalance("B")).isEqualTo(100);
		assertThat(c2.getBalance("C")).isEqualTo(100);
		assertThat(c2.getBalance("D")).isEqualTo(100);

		assertThat(c3.getBalance("A")).isEqualTo(96);
		assertThat(c3.getBalance("B")).isEqualTo(100);
		assertThat(c3.getBalance("C")).isEqualTo(100);
		assertThat(c3.getBalance("D")).isEqualTo(100);
	}

}
