package dotterbear.service.eureka.scheduler.manager;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dotterbear.service.eureka.scheduler.entity.EurekaScheduler;
import dotterbear.service.eureka.scheduler.repository.EurekaSchedulerRepository;

@Component
public class EurekaSchedulerManager {

  @Autowired private EurekaSchedulerRepository eurekaSchedulerRepository;

  @Value("${scheduler.config.name}")
  private String schedulerConfigName;

  public Optional<EurekaScheduler> reload() {
    return Optional.empty();
  }

  public Optional<EurekaScheduler> getConfig() {
    return eurekaSchedulerRepository.findById(schedulerConfigName);
  }
}
