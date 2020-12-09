package br.com.sicredi.backendtest.controller.v1;


import br.com.sicredi.backendtest.controller.v1.model.ErrorResponse;
import br.com.sicredi.backendtest.controller.v1.model.SessionRequest;
import br.com.sicredi.backendtest.entity.Discussion;
import br.com.sicredi.backendtest.entity.Session;
import br.com.sicredi.backendtest.service.SessionService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1/session")
public class SessionApi extends ApiBase {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private SessionService service;

    @ApiOperation(value = "Cria uma nova sessão de votação para uma determinada pauta de discussão. "
            + "A pauta discussão deve estar cadastrada para realizaar a criação de sessão de votação.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "Conflict", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)
    })
    @PostMapping(path = "/{discussionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> createSession(@PathVariable String discussionId, @RequestBody Mono<SessionRequest> payload) {
        log.debug("Create session for discussionId {}, payload {}", discussionId, payload);
        final Mono<Session> sessioncreated = payload.flatMap(value ->
                service.createSession(discussionId, mapper.map(value, Session.class)));
        return sessioncreated.map(session -> ResponseEntity.status(HttpStatus.CREATED).body(session));
    }

    @ApiOperation(value = "Consulta sessão de votação aberta pelo identificador da pauta de discussão.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)
    })
    @GetMapping(path = "/{discussionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Session>> findSession(@PathVariable String discussionId) {
        log.debug("Find session by discussionId {}}", discussionId);
        return service.findOpenSessionByDiscussionId(discussionId).map(discussion -> ResponseEntity.ok(discussion))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
