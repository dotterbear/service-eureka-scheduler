package dotterbear.service.eureka.scheduler.config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

import dotterbear.service.eureka.scheduler.entity.EurekaScheduler;
import dotterbear.service.eureka.scheduler.entity.TaskMap;
import dotterbear.service.eureka.scheduler.manager.EurekaSchedulerManager;

@ConditionalOnProperty(value = "scheduler.enable", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {

  Logger logger = Logger.getLogger(SchedulerConfig.class);

  @Autowired private EurekaSchedulerManager eurekaSchedulerManager;

  @Autowired private RestTemplate restTemplate;

  @Autowired private EurekaClient eurekaClient;

  @Bean(destroyMethod = "shutdownNow")
  public Executor taskExecutor() {
    return Executors.newScheduledThreadPool(10);
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(taskExecutor());
    Optional<EurekaScheduler> eurekaScheduler = eurekaSchedulerManager.getConfig();
    System.out.println(eurekaScheduler);
    EurekaScheduler config = eurekaScheduler.isPresent() ? eurekaScheduler.get() : null;
    List<String> taskList =
        Optional.ofNullable(config)
            .map(EurekaScheduler::getTaskList)
            .orElse(new ArrayList<String>());
    Map<String, TaskMap> taskMap =
        Optional.ofNullable(config)
            .map(EurekaScheduler::getTaskMap)
            .orElse(new HashMap<String, TaskMap>());
    taskList
        .parallelStream()
        .map(taskMap::get)
        .collect(Collectors.toList())
        .forEach(
            task ->
                taskRegistrar.addTriggerTask(
                    () -> {
                      Application application = eurekaClient.getApplication(task.getName());
                      InstanceInfo instanceInfo = application.getInstances().get(0);
                      String url =
                          "http://"
                              + instanceInfo.getIPAddr()
                              + ":"
                              + instanceInfo.getPort()
                              + task.getPath();
                      logger.info("Fetch eureka client: " + task.getName() + ", url: " + url);
                      restTemplate.getForObject(url, String.class);
                    },
                    triggerContext -> {
                      Calendar nextExecutionTime = new GregorianCalendar();
                      Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
                      nextExecutionTime.setTime(
                          lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
                      nextExecutionTime.add(Calendar.MILLISECOND, Integer.parseInt(task.getRate()));
                      return nextExecutionTime.getTime();
                    }));
  }
}
