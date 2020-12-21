package br.com.sicredi.backendtest.controller.v1;

import br.com.sicredi.backendtest.controller.v1.model.ErrorResponse;
import br.com.sicredi.backendtest.controller.v1.model.VoteRequest;
import br.com.sicredi.backendtest.entity.Session;
import br.com.sicredi.backendtest.entity.Vote;
import br.com.sicredi.backendtest.service.VoteService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1/vote")
public class VoteApi extends ApiBase {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private VoteService service;

    @ApiOperation(value = "Efetiva o voto do associado para determinada sessão de pauta de discussão. "
            + "Uma sessão de uma pauta de discussão deve estar criada e aberta para votar.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class),
            @ApiResponse(code = 409, message = "Conflict", response = ErrorResponse.class),
            @ApiResponse(code = 417, message = "Expectation Failed", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)
    })
    @PostMapping(path = "/{discussionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> createVote(@PathVariable String discussionId, @RequestBody Mono<VoteRequest> payload) {
        log.debug("Create session for discussionId {}, payload {}", discussionId, payload);
        final Mono<Vote> voteCreated = payload.flatMap(value -> service.createVote(discussionId, mapper.map(value, Vote.class)));
        return voteCreated.map(vote -> ResponseEntity.status(HttpStatus.CREATED).body(vote));
    }

}
