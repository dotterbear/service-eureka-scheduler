package dotterbear.service.eureka.scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceSchedulerApplicationTests {

	@MockBean
	private ApplicationReadyEvent applicationReadyEvent;

	@Test
	public void contextLoads() {
	}

}
