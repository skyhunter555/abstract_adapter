package ru.syntez.adapter.dataproviders.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import ru.syntez.adapter.config.IKafkaConfig;
import ru.syntez.adapter.core.entities.HandleMessageResult;
import ru.syntez.adapter.core.entities.IMessageOutput;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Реализация KafkaProducer с использованием динамически созданных настроек kafkaTemplate
 *
 * @author Skyhunter
 * @date 18.01.2022
 */
@Service
@RequiredArgsConstructor
public class DynamicKafkaProducerImpl {

    private static Logger LOG = LogManager.getLogger(DynamicKafkaProducerImpl.class);

    @Value("${kafka.producer.send-timeout-seconds}")
    private Integer sendTimeout;

    private final ObjectMapper mapper;
    private final ApplicationContext applicationContext;

    public HandleMessageResult sendMessage(IMessageOutput messageOutput) {

        IKafkaConfig kafkaConfig = (IKafkaConfig) applicationContext.getBean("KafkaConfig");

        String messageKey = UUID.randomUUID().toString();
        String message;
        try {
            message = mapper.writeValueAsString(messageOutput);
        } catch (JsonProcessingException e) {
            LOG.error("Unable to write message as json: " + e.getMessage());
            return HandleMessageResult.ERROR;
        }

        ListenableFuture<SendResult<String, String>> future = kafkaConfig.kafkaTemplate().send(
                new ProducerRecord<>(kafkaConfig.getTopicName(), messageKey, message)
        );

        try {
            SendResult<String, String> result = future.completable().get(sendTimeout, TimeUnit.SECONDS);
            LOG.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            return HandleMessageResult.OK;
        } catch (Exception ex) {
            LOG.error("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            return HandleMessageResult.ERROR;
        }
    }

}
