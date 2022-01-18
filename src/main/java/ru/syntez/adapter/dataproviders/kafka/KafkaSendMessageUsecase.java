package ru.syntez.adapter.dataproviders.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.config.IKafkaConfig;
import ru.syntez.adapter.core.components.IDataprovider;
import ru.syntez.adapter.core.entities.HandleMessageResult;
import ru.syntez.adapter.core.entities.IMessageOutput;
import ru.syntez.adapter.core.usecases.HandleMessageUsecase;
import ru.syntez.adapter.core.utils.AsyncapiService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaSendMessageUsecase {

    private static Logger LOG = LogManager.getLogger(KafkaSendMessageUsecase.class);

    private final ObjectMapper mapper;
    private final AsyncapiService asyncapiService;
    private final ApplicationContext applicationContext;

    public HandleMessageResult execute(IMessageOutput messageOutput) {

        IKafkaConfig kafkaConfig = (IKafkaConfig) applicationContext.getBean("KafkaConfig");

        String messageKey = UUID.randomUUID().toString();
        String message;
        try {
            message = mapper.writeValueAsString(messageOutput);
        } catch (JsonProcessingException e) {
            LOG.error("Unable to write message as json: " + e.getMessage());
            return HandleMessageResult.ERROR;
        }
        return HandleMessageResult.OK;
    }

}
