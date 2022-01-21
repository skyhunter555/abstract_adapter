package ru.syntez.adapter.dataproviders.kafka;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.MethodDelegation;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;
import ru.syntez.adapter.config.IDataproviderKafkaConfig;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.core.utils.AsyncapiService;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Optional;

/**
 * Генератор настроек kafka
 *
 * @author Skyhunter
 * @date 17.01.2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicKafkaConfigGenerator {

    private final ApplicationContext applicationContext;
    private final DynamicKafkaConfigImpl kafkaConfig;
    private final AsyncapiService asyncapiService;

    @SneakyThrows
    public IDataproviderKafkaConfig execute(AsyncapiServerEntity serverKafka) {

        KafkaConfigBeanImplementation.dynamicKafkaConfigImpl = kafkaConfig;

        //Задаем параметры конфигурации продюсера из настроек asyncapi
        kafkaConfig.initProducerConfigs(getBootstrapServers(serverKafka));
        kafkaConfig.setTopicName(getTopicName(asyncapiService));

        IDataproviderKafkaConfig kafkaConfig = new ByteBuddy()
                .subclass(IDataproviderKafkaConfig.class)
                .name("DataproviderKafkaConfig")
                .annotateType(AnnotationDescription.Builder
                        .ofType(Configuration.class) // don't use `request` mapping here
                        .build())
                .annotateType(AnnotationDescription.Builder
                        .ofType(Primary.class) // don't use `request` mapping here
                        .build())
                .defineMethod("producerConfigs", Map.class, Modifier.PUBLIC)
                .intercept(MethodDelegation.to(DynamicKafkaConfigGenerator.KafkaConfigBeanImplementation.class))
                .annotateMethod(AnnotationDescription.Builder
                        .ofType(Bean.class)
                        .build())
                .defineMethod("producerFactory", ProducerFactory.class, Modifier.PUBLIC)
                .intercept(MethodDelegation.to(DynamicKafkaConfigGenerator.KafkaConfigBeanImplementation.class))
                .annotateMethod(AnnotationDescription.Builder
                        .ofType(Bean.class)
                        .build())
                .defineMethod("kafkaTemplate", KafkaTemplate.class, Modifier.PUBLIC)
                .intercept(MethodDelegation.to(DynamicKafkaConfigGenerator.KafkaConfigBeanImplementation.class))
                .annotateMethod(AnnotationDescription.Builder
                        .ofType(Bean.class)
                        .build())
                .defineMethod("getTopicName", String.class, Modifier.PUBLIC)
                .intercept(MethodDelegation.to(DynamicKafkaConfigGenerator.KafkaConfigBeanImplementation.class))

                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();

        //Регистрируем новые бины конфигурации
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        beanFactory.registerSingleton(kafkaConfig.getClass().getCanonicalName(), kafkaConfig);
        //beanFactory.registerSingleton("producerFactory", kafkaConfig.producerFactory());

        log.info("Generated `KafkaConfig`: {}", kafkaConfig.getClass().getName());
        return kafkaConfig;
    }

    /**
     * Получение адреса отправки kafka для продюсера
     * @param serverKafka
     * @return
     */
    private String getBootstrapServers(AsyncapiServerEntity serverKafka) {

        if (serverKafka.getUrl() == null) {
            throw new AsyncapiParserException("Asyncapi Kafka dataprovider url not found!");
        }

        if (serverKafka.getVariables() == null
                || serverKafka.getVariables().getPort() == null
                || serverKafka.getVariables().getPort().getDefaultValue() == null) {
            throw new AsyncapiParserException("Asyncapi Kafka dataprovider port not found!");
        }

        return String.format("%s:%s", serverKafka.getUrl(), serverKafka.getVariables().getPort().getDefaultValue());
    }

    /**
     * Получение топика отправки kafka для продюсера
     * @param asyncapiService
     * @return
     */
    private String getTopicName(AsyncapiService asyncapiService) {

        Optional<String> topicName = asyncapiService.getDataproviderKafkaTopicName();
        if (topicName.isPresent()) {
            return topicName.get();
        }
        throw new AsyncapiParserException("Asyncapi Kafka dataprovider topicName not found!");
    }

    /**
     * Methods implementation controller by {@link DynamicKafkaConfigImpl}
     */
    public static class KafkaConfigBeanImplementation {

        private static DynamicKafkaConfigImpl dynamicKafkaConfigImpl;

        /**
         * Delegates to:
         * {@link DynamicKafkaConfigImpl#producerConfigs()}
         */
        public static Map<String, Object> producerConfigs() {
            return dynamicKafkaConfigImpl.producerConfigs();
        }
        /**
         * Delegates to:
         * {@link DynamicKafkaConfigImpl#producerFactory()}
         */
        public static ProducerFactory<String, String> producerFactory() {
            return dynamicKafkaConfigImpl.producerFactory();
        }
        /**
         * Delegates to:
         * {@link DynamicKafkaConfigImpl#kafkaTemplate()}
         */
        public static KafkaTemplate<String, String> kafkaTemplate() {
            return dynamicKafkaConfigImpl.kafkaTemplate();
        }
        /**
         * Delegates to:
         * {@link DynamicKafkaConfigImpl#getTopicName()}
         */
        public static String getTopicName() {
            return dynamicKafkaConfigImpl.getTopicName();
        }
    }
}
