package tech.kafka.kafkaConsumerApp.service;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ConsumerService {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @KafkaListener (topics = "payment-topic", groupId = "default-group", topicPartitions =
  @TopicPartition (topic = "payment-topic", partitions = {"1"}))
  public void listenMessages (ConsumerRecord<String, String> consumerRecord) {
    log.info ("key :  {}, value:  {}", consumerRecord.key (), consumerRecord.value ());
    log.info ("topicName : {}, partition: {}, offset: {}, timestamp:{}",
              consumerRecord.topic (),
              consumerRecord.partition (),
              consumerRecord.offset (), consumerRecord.timestamp ()
             );
  }
}
