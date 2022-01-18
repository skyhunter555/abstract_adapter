package ru.syntez.adapter.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.MessageHandler;

import java.util.Map;

public interface IKafkaConfig {

    MessageHandler kafkaMessageHandler();

    KafkaTemplate<String, String> kafkaTemplate();

    ProducerFactory<String, String> producerFactory();

    Map<String, Object> producerConfigs();

    String getTopicName();

}
