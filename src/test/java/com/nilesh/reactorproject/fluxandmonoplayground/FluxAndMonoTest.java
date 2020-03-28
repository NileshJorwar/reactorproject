package com.nilesh.reactorproject.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {
    @Test
    public void fluxTest()
    {
        Flux<String> stringFlux= Flux.just("srping", "springboot", "reactive spring")
                .concatWith(Flux.error(new RuntimeException("Attaching Exception to above Flux")))
                .concatWith(Flux.just("After Exception Thrown")) // After Error occured above does not emit the message in current flux
                .log();

        stringFlux.subscribe(System.out ::  println
                ,(error) -> System.err.println(error)
        , () -> System.out.println("Completed Successfully with NO ERROR"));
            // Third parameter of subscribe() gets executed if NO ERROR
    }

    @Test
    public void fluxTestElements_withoutError()
    {
        Flux<String> stringFlux1= Flux.just("srping", "springboot", "reactive spring")
                .log();
        //Using StepVerifier class of reactor-test module to test the elements of Flux
        StepVerifier.create(stringFlux1)
                .expectNext("srping")
                .expectNext("springboot")
                .expectNext("reactive spring")
                .verifyComplete(); // Must be with Stepveifier to end with bcz it starts the subscribing
        ; // If order changed --fails
        //Stepverifier subscribes and then asserting the value
    }

    @Test
    public void fluxTestElements_withError()
    {
        Flux<String> stringFlux1= Flux.just("srping", "springboot", "reactive spring")
                .concatWith(Flux.error(new RuntimeException("Exception attached to the Flux above")))
                .log();
        //Using StepVerifier class of reactor-test module to test the elements of Flux
        StepVerifier.create(stringFlux1)
                .expectNext("srping")
                .expectNext("springboot")
                .expectNext("reactive spring")
                .expectErrorMessage("Exception attached to the Flux above")
                //.expectError(RuntimeException.class)
                .verify(); // start the whole flow from flux to subscriber

        //Stepverifier subscribes and then asserting the value
    }

    @Test
    public void fluxTestElementsCount_withError()
    {
        Flux<String> stringFlux= Flux.just("srping", "springboot", "reactive spring")
                .concatWith(Flux.error(new RuntimeException("Exception attached to the Flux above")))
                .log();
        //Using StepVerifier class of reactor-test module to test the elements of Flux
        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectError()
                .verify();

        //Stepverifier subscribes and then asserting the value
    }

    @Test
    public void fluxTestElements_withError1()
    {
        Flux<String> stringFlux1= Flux.just("srping", "springboot", "reactive spring")
                .concatWith(Flux.error(new RuntimeException("Exception attached to the Flux above")))
                .log();
        StepVerifier.create(stringFlux1)
                .expectNext("srping","springboot","reactive spring")
                .expectErrorMessage("Exception attached to the Flux above")
                .verify(); // start the whole flow from flux to subscriber
    }

    @Test
    public void mono_test()
    {
        Mono<String> stringMono = Mono.just("SpringMono")
                .log();

        StepVerifier.create(stringMono)
                .expectNext("SpringMono")
                .verifyComplete();

    }

    @Test
    public void monoTest_error()
    {
        StepVerifier.create(Mono.error(new RuntimeException("Error occured in Mono")).log())
                .expectError(RuntimeException.class)
                .verify();

    }
}
