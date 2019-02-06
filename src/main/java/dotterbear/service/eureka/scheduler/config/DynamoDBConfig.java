package dotterbear.service.eureka.scheduler.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.ConsistentReads;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.PaginationLoadingStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;

@Configuration
@EnableDynamoDBRepositories(basePackages = "dotterbear.service.rss.reader.repository")
public class DynamoDBConfig {

  @Value("${amazon.dynamodb.endpoint}")
  private String amazonDynamoDBEndpoint;

  @Value("${amazon.dynamodb.location}")
  private String amazonDynamoDBLocation;

  @Value("${amazon.aws.accesskey}")
  private String amazonAWSAccessKey;

  @Value("${amazon.aws.secretkey}")
  private String amazonAWSSecretKey;

  @Bean
  public AmazonDynamoDB amazonDynamoDB(AWSCredentialsProvider awsCredentialsProvider) {
    AmazonDynamoDB amazonDynamoDB =
        AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(
                new EndpointConfiguration(amazonDynamoDBEndpoint, amazonDynamoDBLocation))
            .withCredentials(awsCredentialsProvider)
            .build();
    return amazonDynamoDB;
  }

  @Bean
  public DynamoDBMapperConfig dynamoDBMapperConfig() {
    DynamoDBMapperConfig dynamoDBMapperConfig =
        new DynamoDBMapperConfig.Builder()
            .withSaveBehavior(SaveBehavior.CLOBBER)
            .withConsistentReads(ConsistentReads.CONSISTENT)
            .withPaginationLoadingStrategy(PaginationLoadingStrategy.EAGER_LOADING)
            .build();
    return dynamoDBMapperConfig;
  }

  @Bean
  public AWSCredentials amazonAWSCredentials() {
    return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
  }

  @Bean
  @Primary
  public AWSCredentialsProvider awsCredentialsProvider() {
    return new DefaultAWSCredentialsProviderChain();
  }
}
