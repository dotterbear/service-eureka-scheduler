package com.dotterbear.service.eureka.scheduler.config;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
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

import com.dotterbear.service.eureka.scheduler.config.SchedulerPropertiesConfig.ConfigMap;
import com.dotterbear.service.eureka.scheduler.service.PropertiesService;
import com.dotterbear.service.eureka.scheduler.service.RibbonService;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@ConditionalOnProperty(value = "scheduler.enable", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {

  Logger logger = Logger.getLogger(SchedulerConfig.class);

  @Autowired PropertiesService propertiesService;

  @Autowired RibbonService ribbonService;

  @Autowired private EurekaClient eurekaClient;

  @Bean(destroyMethod = "shutdownNow")
  public Executor taskExecutor() {
    return Executors.newScheduledThreadPool(10);
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(taskExecutor());
    SchedulerPropertiesConfig schedulerPropertiesConfig =
        propertiesService.getSchedulerPropertiesConfig();
    List<String> configList = schedulerPropertiesConfig.getConfigList();
    Map<String, ConfigMap> configMap = schedulerPropertiesConfig.getConfigMap();
    if (configList.isEmpty()) {
      return;
    }
    configList
        .parallelStream()
        .map(configMap::get)
        .collect(Collectors.toList())
        .forEach(
            task ->
                taskRegistrar.addTriggerTask(
                    () -> {
                      Application application = eurekaClient.getApplication(task.getName());
                      InstanceInfo instanceInfo = application.getInstances().get(0);
                      ribbonService.call(task.getName(), instanceInfo.getPort(), task.getPath());
                    },
                    triggerContext -> {
                      Calendar nextExecutionTime = new GregorianCalendar();
                      Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
                      if (lastActualExecutionTime == null) {
                        nextExecutionTime.setTime(new Date());
                        nextExecutionTime.add(Calendar.MILLISECOND, 60000);
                      } else {
                        nextExecutionTime.setTime(lastActualExecutionTime);
                        nextExecutionTime.add(Calendar.MILLISECOND, task.getRate());
                      }
                      return nextExecutionTime.getTime();
                    }));
  }
}
