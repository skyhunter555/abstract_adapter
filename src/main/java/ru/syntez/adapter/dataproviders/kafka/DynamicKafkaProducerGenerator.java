package ru.syntez.adapter.dataproviders.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Argument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.syntez.adapter.core.components.IDataprovider;
import ru.syntez.adapter.core.entities.HandleMessageResult;
import ru.syntez.adapter.core.entities.IMessageOutput;
import ru.syntez.adapter.core.entities.IMessageReceived;
import ru.syntez.adapter.core.usecases.HandleMessageUsecase;
import ru.syntez.adapter.core.usecases.SendMessageUsecase;
import ru.syntez.adapter.entrypoints.http.SampleDocument;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Generates rest controller for {@link SampleDocument} at runtime:
 * {@code
 *
 * @Component
 * @Primary
 * public class AdapterKafkaProducer implements IDataprovider {
 *
 *     private static Logger LOG = LogManager.getLogger(HandleMessageUsecase.class);
 *
 *     @Value("${kafka.input-topic-name}")
 *     private String topicName;
 *
 *     @Value("${kafka.producer.send-timeout-seconds}")
 *     private Integer sendTimeout;
 *
 *     private final ObjectMapper mapper;
 *
 *     private final KafkaTemplate<String, String> kafkaTemplate;
 *
 *     public AdapterKafkaProducer(ObjectMapper objectMapper,
 *                                 KafkaTemplate<String, String> kafkaTemplate) {
 *         this.mapper = objectMapper;
 *         this.kafkaTemplate = kafkaTemplate;
 *     }
 *
 *     public HandleMessageResult sendMessage(IMessageOutput messageOutput) {
 *
 *         //TODO: в зависимости от требований тип ключа - параметризовать
 *         String messageKey = UUID.randomUUID().toString();
 *
 *         String message;
 *         try {
 *             message = mapper.writeValueAsString(messageOutput);
 *         } catch (JsonProcessingException e) {
 *             LOG.error("Unable to write message as json: " + e.getMessage());
 *             return HandleMessageResult.ERROR;
 *         }
 *
 *         ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(new ProducerRecord<>(topicName, messageKey, message));
 *
 *         try {
 *             SendResult<String, String> result = future.completable().get(sendTimeout, TimeUnit.SECONDS);
 *             LOG.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
 *             return HandleMessageResult.OK;
 *         } catch (Exception ex) {
 *             LOG.error("Unable to send message=[" + message + "] due to : " + ex.getMessage());
 *             return HandleMessageResult.ERROR;
 *         }
 *
 *     }
 *
 * }
 */
@Slf4j
@Component
@DependsOn("kafkaSendMessageUsecase")
@RequiredArgsConstructor
public class DynamicKafkaProducerGenerator {

    private final ApplicationContext applicationContext;

    @SneakyThrows
    public IDataprovider generate() {
        // init static implementation to avoid reflection usage
        KafkaProducerMethodsImplementation.sendMessageUsecase = applicationContext.getBean(KafkaSendMessageUsecase.class);

        // creates builder with unique `class` name and `@Component` annotation
        IDataprovider adapterKafkaProducer = new ByteBuddy()
                .subclass(IDataprovider.class)
                .name("DataproviderImpl")
                .annotateType(AnnotationDescription.Builder
                        .ofType(Component.class) // don't use `request` mapping here
                        .build())
                .annotateType(AnnotationDescription.Builder
                        .ofType(Primary.class) // don't use `request` mapping here
                        .build())
                /**
                 * Delegates to:
                 * {@link KafkaProducerMethodsImplementation#sendMessage(IMessageOutput)}
                 */
                .defineMethod("sendMessage", HandleMessageResult.class, Modifier.PUBLIC)
                .withParameter(IMessageOutput.class, "messageOutput")
                .annotateParameter(AnnotationDescription.Builder
                        .ofType(RequestBody.class)
                        .build())
                .intercept(MethodDelegation.to(KafkaProducerMethodsImplementation.class))

                // creates instance of generated `controller`
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();

        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        beanFactory.registerSingleton(adapterKafkaProducer.getClass().getCanonicalName(), adapterKafkaProducer);

        log.info("Generated `DataproviderImpl`: {}", adapterKafkaProducer.getClass().getName());
        return adapterKafkaProducer;
    }

    /**
     * Methods implementation for {@link IMessageOutput} controller by {@link KafkaSendMessageUsecase}
     */
    public static class KafkaProducerMethodsImplementation {

        private static KafkaSendMessageUsecase sendMessageUsecase;

        /**
         * Delegates to:
         * {@link KafkaSendMessageUsecase#execute(IMessageOutput)}
         */
        public static HandleMessageResult sendMessage(@Argument(0) IMessageOutput document) {
            return sendMessageUsecase.execute(document);
        }

    }

}