package ru.syntez.adapter.dataproviders.transform;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Slf4j
@Service
public class DynamicTransformConfigImpl {

    private Class<?> outputMessageClass;

    public Class<?> getOutputMessageClass() {
        return outputMessageClass;
    }

    public void setOutputMessageClass(Class<?> outputMessageClass) {
        this.outputMessageClass = outputMessageClass;
    }

}
