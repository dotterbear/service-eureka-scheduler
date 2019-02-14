package com.dotterbear.service.eureka.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@SpringBootApplication
@EnableHystrix
@EnableHystrixDashboard
public class ServiceSchedulerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServiceSchedulerApplication.class, args);
  }

  @Bean
  public ServletRegistrationBean<HystrixMetricsStreamServlet> getServlet() {
    HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
    ServletRegistrationBean<HystrixMetricsStreamServlet> registrationBean =
        new ServletRegistrationBean<>(streamServlet);
    registrationBean.setLoadOnStartup(1);
    registrationBean.addUrlMappings("/hystrix.stream");
    registrationBean.setName("HystrixMetricsStreamServlet");
    return registrationBean;
  }
}
