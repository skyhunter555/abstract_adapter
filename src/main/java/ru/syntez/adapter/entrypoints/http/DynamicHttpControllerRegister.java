package ru.syntez.adapter.entrypoints.http;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ru.syntez.adapter.core.entities.IMessageReceived;
import ru.syntez.adapter.core.entities.asyncapi.components.AsyncapiComponentMessageEntity;
import ru.syntez.adapter.core.entities.asyncapi.servers.AsyncapiServerEntity;
import ru.syntez.adapter.core.exceptions.AsyncapiParserException;
import ru.syntez.adapter.core.utils.AsyncapiService;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Optional;

/**
 * @author Skyhunter
 * @date 17.01.2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicHttpControllerRegister {

    private final AsyncapiService asyncapiService;
    private final DynamicHttpMessageGenerator messageGenerator;
    private final DynamicHttpControllerGenerator controllerGenerator;
    private final RequestMappingHandlerMapping handlerMapping;
    private final Docket api;

    @SneakyThrows
    public void execute(AsyncapiServerEntity httpServer) {

        IMessageReceived messageReceived = messageGenerator.execute(getMessageReceived(asyncapiService));

        Object adapterHttpController = controllerGenerator.execute();

        /**
         * Delegates to:
         * {@link AdapterHttpControllerMethodsImplementation#create(SampleDocument)}
         */
        handlerMapping.registerMapping(
                RequestMappingInfo.paths(getBasePath(httpServer))
                        .methods(RequestMethod.POST)
                        .consumes(MediaType.APPLICATION_JSON_VALUE)
                        .build(),
                adapterHttpController,
                adapterHttpController.getClass()
                        .getMethod("create", SampleDocument.class));

        //Обновление swagger api
        api.select()
           .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
           .paths(PathSelectors.any())
           .build();

        log.info("Registered request handler for `AdapterHttpController`: {}", adapterHttpController.getClass().getName());
    }

    private String getBasePath(AsyncapiServerEntity httpServer) {
        if (httpServer.getVariables() == null || httpServer.getVariables().getBasePath() == null) {
            throw new AsyncapiParserException("Asyncapi entrypoints http basePath not found!");
        }
        return httpServer.getVariables().getBasePath();
    }

    /**
     * Получение настроек входящего сообщения
     * @param asyncapiService
     * @return
     */
    private AsyncapiComponentMessageEntity getMessageReceived(AsyncapiService asyncapiService) {

        Optional<AsyncapiComponentMessageEntity> messageReceived = asyncapiService.getMessageReceived();
        if (messageReceived.isPresent()) {
            return messageReceived.get();
        }
        throw new AsyncapiParserException("Asyncapi http entrypoint messageReceived not found!");
    }
}
