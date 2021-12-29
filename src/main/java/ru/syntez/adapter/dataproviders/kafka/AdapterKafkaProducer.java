package ru.syntez.adapter.dataproviders.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import ru.syntez.adapter.core.components.IDataprovider;
import ru.syntez.adapter.core.entities.HandleMessageResult;
import ru.syntez.adapter.core.entities.IMessageOutput;
import ru.syntez.adapter.core.usecases.HandleMessageUsecase;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Реализация компонента kafka для отправки сообщений с помощью kafkaTemplate
 *
 * @author Skyhunter
 * @date 27.12.2021
 */
@Component
@Primary
public class AdapterKafkaProducer implements IDataprovider {

    private static Logger LOG = LogManager.getLogger(HandleMessageUsecase.class);

    @Value("${kafka.input-topic-name}")
    private String topicName;

    @Value("${kafka.producer.send-timeout-seconds}")
    private Integer sendTimeout;

    private final ObjectMapper mapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public AdapterKafkaProducer(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.mapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public HandleMessageResult sendMessage(IMessageOutput messageOutput) {

        //TODO: в зависимости от требований тип ключа - параметризовать
        String messageKey = UUID.randomUUID().toString();

        String message;
        try {
            message = mapper.writeValueAsString(messageOutput);
        } catch (JsonProcessingException e) {
            LOG.error("Unable to write message as json: " + e.getMessage());
            return HandleMessageResult.ERROR;
        }

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(new ProducerRecord<>(topicName, messageKey, message));

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
