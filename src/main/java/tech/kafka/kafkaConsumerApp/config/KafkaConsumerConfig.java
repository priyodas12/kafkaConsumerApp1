package tech.kafka.kafkaConsumerApp.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import lombok.extern.log4j.Log4j2;
import tech.kafka.kafkaConsumerApp.model.Product;

@EnableKafka
@Log4j2
@Configuration
public class KafkaConsumerConfig {

  @Value ("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Product>> kafkaListenerContainerFactory () {
    ConcurrentKafkaListenerContainerFactory<String, Product> factory =
        new ConcurrentKafkaListenerContainerFactory<> ();
    factory.setConsumerFactory (consumerFactory ());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, Product> consumerFactory () {
    return new DefaultKafkaConsumerFactory<> (consumerConfigs ());
  }

  @Bean
  public Map<String, Object> consumerConfigs () {

    Map<String, Object> props = new HashMap<> ();
    props.put (ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    //props.put (ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put (ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put (ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put (
        ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ProductDeserializer.class.getName ());
    return props;
  }

}

