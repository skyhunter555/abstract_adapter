package ru.syntez.adapter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.syntez.adapter.config.ApplicationStartedEventListener;
import ru.syntez.adapter.core.entities.asyncapi.AsyncapiEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.core.usecases.asyncapi.CreateAsyncapiFromFileUsecase;
import ru.syntez.adapter.core.utils.AsyncapiService;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Optional;

/**
 * Main class for console running
 *
 * @author Skyhunter
 * @date 26.12.2021
 */
@SpringBootApplication
@EnableSwagger2
public class AdapterMain {

    private static Logger LOG = LogManager.getLogger(AdapterMain.class);

    public static void main(String[] args) {

        LOG.info("Start process parse files...");

        AsyncapiEntity asyncapi = createAsyncapi(args);

        //Если есть порт для входящих http указываем в конфигурации перед стартом приложения
        Optional<Integer> httpPort = AsyncapiService.getEntrypointsHttpPort(asyncapi);
        httpPort.ifPresent(integer -> System.getProperties().put("server.port", integer));

        SpringApplication sa = new SpringApplication(AdapterMain.class);
        sa.addListeners(new ApplicationStartedEventListener(asyncapi));
        sa.run(args);

    }

    private static AsyncapiEntity createAsyncapi(String[] args) {
        CreateAsyncapiFromFileUsecase createAsyncapiFromFileUsecase = new CreateAsyncapiFromFileUsecase();
        for (int i = 0; i < args.length; ++i) {
            String fileName = args[i];
            LOG.info("args[{}]: {}", i, fileName);
            return createAsyncapiFromFileUsecase.execute(fileName);
        }
        throw new AsyncapiParserException("Asyncapi not created from args");
    }

}