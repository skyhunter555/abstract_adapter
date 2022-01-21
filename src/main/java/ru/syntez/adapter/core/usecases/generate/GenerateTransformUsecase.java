package ru.syntez.adapter.core.usecases.generate;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.config.IDataproviderKafkaConfig;
import ru.syntez.adapter.config.ITransformConfig;
import ru.syntez.adapter.core.components.IDataprovider;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiProtocolEnum;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiTypeEnum;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentMessageEntity;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentSchemaEntity;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.core.utils.AsyncapiService;
import ru.syntez.adapter.dataproviders.kafka.DynamicKafkaConfigGenerator;
import ru.syntez.adapter.dataproviders.kafka.DynamicKafkaProducerGenerator;
import ru.syntez.adapter.dataproviders.transform.DynamicTransformConfigGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * В зависимости от полученной конфигурации asyncapi
 * генерация компонентов для простой трансформации сообщений с помощью конвертера
 * kafka
 *
 * @author Skyhunter
 * @date 20.01.2022
 */
@Service
@RequiredArgsConstructor
public class GenerateTransformUsecase {

    private final AsyncapiService asyncapiService;
    private final GenerateMessageClassUsecase generateMessageClass;
    private final DynamicTransformConfigGenerator transformConfigGenerator;
    private final GetMessagePayloadUsecase getMessagePayload;

    private static Logger LOG = LogManager.getLogger(GenerateTransformUsecase.class);

    public void execute() {

        Optional<AsyncapiComponentMessageEntity> messageOutputOptional = asyncapiService.getMessageOutput();

        //TODO Исходящего сообщения может и не быть, если включена какая то бизнес логика?
        if (!messageOutputOptional.isPresent()) {
            return;
        }

        AsyncapiComponentMessageEntity messageOutput = messageOutputOptional.get();

        AsyncapiComponentSchemaEntity messagePayloadSchema = getMessagePayload.execute(messageOutput);
        Class<?> messagePayloadClass = generateMessageClass.execute(messagePayloadSchema, messageOutput.getName());

        ITransformConfig transformConfig = transformConfigGenerator.execute(messagePayloadClass, getTransformSchema(messageOutput));
        LOG.info("Asyncapi transtormer generated");

    }

    /**
     * Получение настроек трансформации для формирования исходящего сообщения
     *
     * @param messageOutput
     * @return
     */
    private Map<String, Object> getTransformSchema(AsyncapiComponentMessageEntity messageOutput) {

        if (messageOutput.getPayload()!= null
                && messageOutput.getPayload().getTransform() != null
                && messageOutput.getPayload().getTransform().getReference() != null) {
            Optional<AsyncapiComponentSchemaEntity> transformSchemaOptional = asyncapiService.getMessagePayload(messageOutput.getPayload().getTransform().getReference());
            if (transformSchemaOptional.isPresent()) {

                AsyncapiComponentSchemaEntity transformSchema = transformSchemaOptional.get();

                if (transformSchema.getType() == null) {
                    throw new AsyncapiParserException("Asyncapi message payload transformSchema type not found!");
                }
                if (transformSchema.getType() == AsyncapiTypeEnum.OBJECT &&
                        (transformSchema.getProperties() == null || transformSchema.getProperties().getAdditionalProperties() == null)) {
                    throw new AsyncapiParserException("Asyncapi message payload transformSchema properties not found!");
                }
                return transformSchema.getProperties().getAdditionalProperties();
            }
        }
        //TODO Схемы трансформации сообщения может и не быть, если включена какая то бизнес логика?
        //В этом случае никакой трансформации не произойдет и исходящее сообщение останется пустым
        return new HashMap<>();
    }
}
