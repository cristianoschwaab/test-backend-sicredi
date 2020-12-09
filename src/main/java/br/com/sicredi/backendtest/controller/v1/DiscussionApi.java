package br.com.sicredi.backendtest.controller.v1;

import br.com.sicredi.backendtest.controller.v1.model.DiscussionRequest;
import br.com.sicredi.backendtest.controller.v1.model.ErrorResponse;
import br.com.sicredi.backendtest.entity.Discussion;
import br.com.sicredi.backendtest.service.DiscussionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping("/v1/discussion")
public class DiscussionApi extends ApiBase {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private DiscussionService service;

    @ApiOperation(value = "Cria uma nova pauta de discussão.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "Conflict", response = ErrorResponse.class),
            @ApiResponse(code = 417, message = "Expectation failed", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)
    })
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> createDiscussion(@RequestBody Mono<DiscussionRequest> payload) {
        log.debug("Create Discussion. payload {} ", payload);
        final Mono<Discussion> discussionCreated = payload.flatMap(value ->
                service.createDiscussion(mapper.map(value, Discussion.class)));
        return discussionCreated.map(discussion -> ResponseEntity.status(HttpStatus.CREATED).body(discussion));
    }

    @ApiOperation(value = "Consulta pauta de discussão pelo identificador.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)
    })
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Discussion>> findDiscussion(@PathVariable String id) {
        log.debug("Find Discussion by id {}", id);
        return service.findById(id).map(discussion -> ResponseEntity.ok(discussion))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
