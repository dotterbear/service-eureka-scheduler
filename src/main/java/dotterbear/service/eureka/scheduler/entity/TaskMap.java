package dotterbear.service.eureka.scheduler.entity;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class TaskMap {

  private String name;

  private String cron;

  private String rate;

  private String path;

  private Map<String, String> param;

  private String method;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCron() {
    return cron;
  }

  public void setCron(String cron) {
    this.cron = cron;
  }

  public String getRate() {
    return rate;
  }

  public void setRate(String rate) {
    this.rate = rate;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Map<String, String> getParam() {
    return param;
  }

  public void setParam(Map<String, String> param) {
    this.param = param;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  @Override
  public String toString() {
    return String.format(
        "TaskMap[name=%s, cron=%s, rate=%s, path=%s, param=%s, method=%s]",
        name, cron, rate, path, param, method);
  }
}
