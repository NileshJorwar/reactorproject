package com.nilesh.reactorproject.controller;
import org.junit.jupiter.api.Test;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebFluxTest // does not allow to run for component, service, repo annotations
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient; // Allows to test the endpoint (url) of flux streams

    @Test
    public void flux_apprach1()
    {
        Flux<Integer> streamResult= webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange() // Needed to hit the endpoint "flux"
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();
        StepVerifier.create(streamResult.log())
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete();
    }

    @Test
    public void approach2()
    {
        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(4);
    }

    @Test
    public void approach3()
    {
        EntityExchangeResult<List<Integer>> result= webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();
        List<Integer> expectedResult= Arrays.asList(1,2,3,4);
        assertEquals(result.getResponseBody(),expectedResult);
    }

    @Test
    public void approach4()
    {
        List<Integer> expectedResult= Arrays.asList(1,2,3,4);

        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith((response) ->
                {
                    assertEquals(expectedResult,response.getResponseBody());
                });
    }

    @Test
    public void approach_fluxStream()
    {
        List<Integer> expectedResult= Arrays.asList(1,2,3,4);

        Flux<Integer> actualRest= webTestClient.get().uri("/fluxStream")
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(actualRest.log())
                .expectNext(10)
                .expectNext(20)
                .expectNext(30)
                .expectNext(40)
                .verifyComplete();
    }

    @Test
    public void approach_fluxStreamInfinite() {
        List<Integer> expectedResult = Arrays.asList(1, 2, 3, 4);

        Flux<Long> actualRest = webTestClient.get().uri("/fluxStreamInfinite")
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();
        StepVerifier.create(actualRest.log())
                .expectNext(0l)
                .expectNext(1l)
                .expectNext(2l)
                .thenCancel()
                .verify();
    }

    @Test
    public void monoTest()
    {
        webTestClient.get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith((res) ->{
                    assertEquals(1,res.getResponseBody());
                });
    }

    }