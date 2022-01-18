package ru.syntez.adapter.dataproviders.kafka;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.core.entities.IMessageReceived;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.usecases.HandleMessageUsecase;
import ru.syntez.adapter.core.utils.AsyncapiService;
import ru.syntez.adapter.entrypoints.http.SampleDocument;

import java.util.HashMap;
import java.util.Map;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicKafkaConfigImpl {

    private final AsyncapiService asyncapiService;

    @Value("${kafka.producer.acks}")
    private String kafkaProducerAcks;

    @Value("${kafka.producer.retries}")
    private Integer kafkaProducerRetries;

    @Value("${kafka.producer.linger-ms}")
    private Integer kafkaProducerLingerMs;

    @Value("${kafka.producer.request-timeout-ms}")
    private Integer kafkaProducerRequestTimeoutMs;

    @Value("${kafka.producer.delivery-timeout-ms}")
    private Integer kafkaProducerDeliveryTimeoutMs;


    public Map<String, Object> producerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        //properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        properties.put(ProducerConfig.ACKS_CONFIG, kafkaProducerAcks);
        properties.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerRetries);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerLingerMs);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerRequestTimeoutMs);
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, kafkaProducerDeliveryTimeoutMs);

        return properties;
    }

}
