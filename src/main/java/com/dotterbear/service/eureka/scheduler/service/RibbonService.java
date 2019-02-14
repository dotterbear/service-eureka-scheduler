package com.dotterbear.service.eureka.scheduler.service;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class RibbonService {

  private Logger logger = Logger.getLogger(RibbonService.class);

  @Autowired RestTemplate restTemplate;

  @HystrixCommand(fallbackMethod = "error")
  public String call(String serviceName, int port, String path) {
    return restTemplate.getForObject(
        "http://" + serviceName + ":" + port + "/" + path, String.class);
  }

  public String error(String serviceName, int port, String path) {
    logger.error("Fail to call service: " + serviceName + ", port: " + port + ", path: " + path);
    return "sorry, error ribbon hystrix!";
  }
}
