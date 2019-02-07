package dotterbear.service.eureka.scheduler.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dotterbear.service.erueka.scheduler.entity.EurekaScheduler;
import dotterbear.service.eureka.scheduler.repository.EurekaSchedulerRepository;

@Component
public class EurekaSchedulerManager {

	@Autowired
	private EurekaSchedulerRepository eurekaSchedulerRepository;

	@Value("${scheduler.config.name}")
	private String schedulerConfigName;

	@EventListener(ApplicationReadyEvent.class)
	public Optional<EurekaScheduler> reload() {
		System.out.println("Start");
		Optional<EurekaScheduler> config = eurekaSchedulerRepository.findById(schedulerConfigName);
		List<String> taskList = Optional.ofNullable(config.get()).map(EurekaScheduler::getTaskList)
				.orElse(new ArrayList<String>());
		// TODO: config
		return null;
	}

}
