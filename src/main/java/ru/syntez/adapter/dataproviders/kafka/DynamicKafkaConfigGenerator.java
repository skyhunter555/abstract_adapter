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
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.syntez.adapter.config.IKafkaConfig;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.usecases.HandleMessageUsecase;
import ru.syntez.adapter.entrypoints.http.SampleDocument;

import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Generates rest controller for {@link SampleDocument} at runtime:
 * {@code
 *
 * @Component
 * @Primary public class AdapterKafkaProducer implements IDataprovider {
 * <p>
 * private static Logger LOG = LogManager.getLogger(HandleMessageUsecase.class);
 * @Value("${kafka.input-topic-name}") private String topicName;
 * @Value("${kafka.producer.send-timeout-seconds}") private Integer sendTimeout;
 * <p>
 * private final ObjectMapper mapper;
 * <p>
 * private final KafkaTemplate<String, String> kafkaTemplate;
 * <p>
 * public AdapterKafkaProducer(ObjectMapper objectMapper,
 * KafkaTemplate<String, String> kafkaTemplate) {
 * this.mapper = objectMapper;
 * this.kafkaTemplate = kafkaTemplate;
 * }
 * <p>
 * public HandleMessageResult sendMessage(IMessageOutput messageOutput) {
 * <p>
 * //TODO: в зависимости от требований тип ключа - параметризовать
 * String messageKey = UUID.randomUUID().toString();
 * <p>
 * String message;
 * try {
 * message = mapper.writeValueAsString(messageOutput);
 * } catch (JsonProcessingException e) {
 * LOG.error("Unable to write message as json: " + e.getMessage());
 * return HandleMessageResult.ERROR;
 * }
 * <p>
 * ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(new ProducerRecord<>(topicName, messageKey, message));
 * <p>
 * try {
 * SendResult<String, String> result = future.completable().get(sendTimeout, TimeUnit.SECONDS);
 * LOG.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
 * return HandleMessageResult.OK;
 * } catch (Exception ex) {
 * LOG.error("Unable to send message=[" + message + "] due to : " + ex.getMessage());
 * return HandleMessageResult.ERROR;
 * }
 * <p>
 * }
 * <p>
 * }
 */
@Slf4j
@Component
@DependsOn("dynamicKafkaConfigImpl")
@RequiredArgsConstructor
public class DynamicKafkaConfigGenerator {

    private final ApplicationContext applicationContext;

    /*
    Class<?> type = new ByteBuddy()
  .subclass(Object.class)
  .name("MyClassName")
  .defineMethod("custom", String.class, Modifier.PUBLIC)
  .intercept(MethodDelegation.to(Bar.class))
  .defineField("x", String.class, Modifier.PUBLIC)
  .make()
  .load(
    getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
  .getLoaded();

Method m = type.getDeclaredMethod("custom", null);
assertEquals(m.invoke(type.newInstance()), Bar.sayHelloBar());
assertNotNull(type.getDeclaredField("x"));
     */

    @SneakyThrows
    public IKafkaConfig generate(AsyncapiServerEntity serverKafka) {
        // init static implementation to avoid reflection usage
        KafkaConfigBeanImplementation.dynamicKafkaConfigImpl = applicationContext.getBean(DynamicKafkaConfigImpl.class);

        IKafkaConfig kafkaConfig = new ByteBuddy()
                .subclass(IKafkaConfig.class)
                .name("KafkaConfig")
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

                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();


        //  KafkaConfig kafkaConfigProxy = new ByteBuddy()
        //          .subclass(KafkaConfig.class)
        //          .method(named("producerConfigs"))
        //          .intercept(MethodDelegation.to(DynamicKafkaConfigGenerator.KafkaConfigBeanImplementation.class))
        //          .make()
        //          .load(KafkaConfig.class.getClassLoader())
        //          .getLoaded()
        //          .newInstance();

        // KafkaConfig kafkaConfig = applicationContext.getBean(KafkaConfig.class);
        //applicationContext.getParentBeanFactory().getBeanProvider(). = kafkaConfig;
        //BeanUtils.


        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        beanFactory.registerSingleton(kafkaConfig.getClass().getCanonicalName(), kafkaConfig);

        log.info("Generated `KafkaConfig`: {}", kafkaConfig.getClass().getName());
        return kafkaConfig;
    }

    /**
     * Methods implementation for {@link SampleDocument} controller by {@link HandleMessageUsecase}
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

    }
}
