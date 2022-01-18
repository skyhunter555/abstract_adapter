package ru.syntez.adapter.core.usecases.generate;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.syntez.adapter.config.IKafkaConfig;
import ru.syntez.adapter.core.components.IDataprovider;
import ru.syntez.adapter.core.entities.IMessageOutput;
import ru.syntez.adapter.core.entities.asyncapi.ServerProtocolEnum;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.dataproviders.kafka.DynamicKafkaConfigGenerator;
import ru.syntez.adapter.dataproviders.kafka.DynamicKafkaProducerGenerator;
import ru.syntez.adapter.dataproviders.kafka.OutputSampleDocument;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenerateDataproviderUsecase {

    private final DynamicKafkaConfigGenerator kafkaConfigGenerator;
    private final DynamicKafkaProducerGenerator kafkaProducerGenerator;

    private static Logger LOG = LogManager.getLogger(GenerateDataproviderUsecase.class);

    public void execute(AsyncapiServerEntity dataproviderServer) {

        if (dataproviderServer.getProtocol() == null) {
            throw new AsyncapiParserException("Asyncapi dataprovider protocol not found!");
        }

        //if (dataproviderServer.getVariables() == null || dataproviderServer.getVariables().getBasePath() == null) {
        //     throw new AsyncapiParserException("Asyncapi dataprovider http basePath not found!");
        //}

        if (dataproviderServer.getProtocol() == ServerProtocolEnum.kafka) {

            IKafkaConfig kafkaConfig = kafkaConfigGenerator.generate(dataproviderServer);
            IDataprovider kafkaProducer = kafkaProducerGenerator.generate();

            OutputSampleDocument documentNew = new OutputSampleDocument();
            documentNew.setDocumentId(123);
            documentNew.setDocumentDescription("test");

            try {
                Method m = kafkaProducer.getClass().getDeclaredMethod("sendMessage", IMessageOutput.class);
                Object result = m.invoke(kafkaProducer, documentNew);

                Map map = kafkaConfig.producerConfigs();

                LOG.info("Asyncapi kafka producer generated");

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            //httpControllerRegister.registerUserController(dataproviderServer.getVariables().getBasePath());

            LOG.info("Asyncapi kafka producer generated");
        }

    }
}
