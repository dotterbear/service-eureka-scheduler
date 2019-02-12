package dotterbear.service.eureka.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dotterbear.service.eureka.scheduler.config.SchedulerPropertiesConfig;

@Service
public class PropertiesService {

	private final SchedulerPropertiesConfig properties;

	@Autowired
	public PropertiesService(SchedulerPropertiesConfig properties) {
		this.properties = properties;
	}

	public SchedulerPropertiesConfig getSchedulerPropertiesConfig() {
		return properties;
	}

}
