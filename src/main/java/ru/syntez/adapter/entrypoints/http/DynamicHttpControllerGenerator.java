package ru.syntez.adapter.entrypoints.http;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Argument;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.syntez.adapter.core.entities.HandleMessageResult;
import ru.syntez.adapter.core.entities.IMessagePayload;
import ru.syntez.adapter.core.usecases.HandleMessageUsecase;

import java.lang.reflect.Modifier;

/**
 * Generates rest controller for {@link IMessagePayload} at runtime:
 * {@code
 *
 * package ru.syntez.adapter.entrypoints.http;
 * import io.swagger.annotations.Api;
 * import io.swagger.annotations.ApiOperation;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.web.bind.annotation.*;
 * import ru.syntez.adapter.core.entities.HandleMessageResult;
 * import ru.syntez.adapter.core.usecases.HandleMessageUsecase;
 *
 * @RestController
 * @RequestMapping("/sample-document/api/v1")
 * @Api(value = "sample-document")
 * public class AdapterController {
 *
 *     private final HandleMessageUsecase handleMessageUsecase;
 *
 *     public AdapterController(HandleMessageUsecase handleMessageUsecase) {
 *         this.handleMessageUsecase = handleMessageUsecase;
 *     }
 *
 *     @PostMapping
 *     @ApiOperation(value = "Handle sample document", produces = "application/json")
 *     public HandleMessageResult create(
 *             @RequestBody SampleDocument sampleDocument
 *     ) {
 *         return handleMessageUsecase.execute(sampleDocument);
 *     }
 *
 * }
 *
 * @author Skyhunter
 * @date 17.01.2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicHttpControllerGenerator {

    private final HandleMessageUsecase handleMessageUsecase;

    @SneakyThrows
    public Object execute(Class<?> messagePayloadClass) {

        // init static implementation to avoid reflection usage
        HttpControllerMethodsImplementation.handleMessageUsecase = handleMessageUsecase;

        // creates builder with unique `class` name and `@RestController` annotation
        Object adapterHttpController = new ByteBuddy()
                .subclass(Object.class)
                .name("AdapterHttpController")
                .annotateType(AnnotationDescription.Builder
                        .ofType(RestController.class) // don't use `request` mapping here
                        .build())
                .annotateType(AnnotationDescription.Builder.ofType(Api.class)
                    .define("value", "sample-document")
                    .build())
                /**
                 * Delegates to:
                 * {@link HttpControllerMethodsImplementation#create(IMessagePayload)}
                 */
                .defineMethod("create", HandleMessageResult.class, Modifier.PUBLIC)
                .withParameter(messagePayloadClass, messagePayloadClass.getName())
                .annotateParameter(AnnotationDescription.Builder
                        .ofType(RequestBody.class)
                        .build())
                .intercept(MethodDelegation.to(HttpControllerMethodsImplementation.class))

                // creates instance of generated `controller`
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();

        log.info("Generated `AdapterHttpController`: {}", adapterHttpController.getClass().getName());
        return adapterHttpController;
    }

    /**
     * Methods implementation for {@link IMessagePayload} controller by {@link HandleMessageUsecase}
     */
    public static class HttpControllerMethodsImplementation {

        private static HandleMessageUsecase handleMessageUsecase;

        /**
         * Delegates to:
         * {@link HandleMessageUsecase#execute(IMessagePayload)}
         */
        public static HandleMessageResult create(@Argument(0) IMessagePayload messagePayload) {
            return handleMessageUsecase.execute(messagePayload);
        }

    }

}
