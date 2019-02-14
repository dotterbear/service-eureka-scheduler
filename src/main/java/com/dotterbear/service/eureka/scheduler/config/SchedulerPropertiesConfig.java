package com.dotterbear.service.eureka.scheduler.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scheduler")
public class SchedulerPropertiesConfig {

  private boolean enable;

  private List<String> configList = new ArrayList<>();

  private Map<String, ConfigMap> configMap = new HashMap<>();

  public SchedulerPropertiesConfig() {}

  public SchedulerPropertiesConfig(
      boolean enable, List<String> configList, Map<String, ConfigMap> configMap) {
    this.enable = enable;
    this.configMap = configMap;
    this.configList = configList;
  }

  public boolean getEnable() {
    return this.enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public void setConfigList(List<String> configList) {
    this.configList = configList;
  }

  public List<String> getConfigList() {
    return this.configList;
  }

  public void setConfigMap(Map<String, ConfigMap> configMap) {
    this.configMap = configMap;
  }

  public Map<String, ConfigMap> getConfigMap() {
    return this.configMap;
  }

  public static class ConfigMap {

    private String name;
    private int rate;
    private String path;

    public ConfigMap() {}

    public ConfigMap(String name, int rate, String path) {
      this.name = name;
      this.rate = rate;
      this.path = path;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getRate() {
      return rate;
    }

    public void setRate(int rate) {
      this.rate = rate;
    }

    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = path;
    }

    @Override
    public String toString() {
      return String.format("ConfigMap[rate=%s, path=%s]", rate, path);
    }
  }

  @Override
  public String toString() {
    return String.format(
        "SchedulerPropertiesConfig[enable=%s, configList=%s, configMap=%s]",
        enable, configList, configMap);
  }
}
