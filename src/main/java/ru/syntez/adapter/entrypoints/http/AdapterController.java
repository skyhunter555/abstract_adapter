package ru.syntez.adapter.entrypoints.http;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.syntez.adapter.core.entities.HandleMessageResult;
import ru.syntez.adapter.core.usecases.HandleMessageUsecase;
import ru.syntez.adapter.entrypoints.http.entities.SampleDocument;

/**
 * Rest controller
 *
 * @author Skyhunter
 * @date 26.12.2021
 */
@RestController
@RequestMapping("/sample-document/api/v1")
@Api(value = "sample-document")
public class AdapterController {

    private final HandleMessageUsecase handleMessageUsecase;

    public AdapterController(HandleMessageUsecase handleMessageUsecase) {
        this.handleMessageUsecase = handleMessageUsecase;
    }

    @PostMapping
    @ApiOperation(value = "Handle sample document", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public HandleMessageResult create(
        @RequestBody SampleDocument sampleDocument
    ) {
        return handleMessageUsecase.execute(sampleDocument);
    }

}

