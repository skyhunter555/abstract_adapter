package ru.syntez.adapter.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.MessageHandler;

import java.util.Map;

/**
 * Конфигурация для продюсера kafka
 *
 * @author Skyhunter
 * @date 20.01.2022
 */

public interface IDataproviderKafkaConfig {

    MessageHandler kafkaMessageHandler();

    KafkaTemplate<String, String> kafkaTemplate();

    ProducerFactory<String, String> producerFactory();

    Map<String, Object> producerConfigs();

    String getTopicName();

    Class<?> kafkaOutputMessageClassTemplate();

}
