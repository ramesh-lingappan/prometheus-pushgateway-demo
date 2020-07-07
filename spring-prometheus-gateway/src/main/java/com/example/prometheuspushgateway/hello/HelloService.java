package com.example.prometheuspushgateway.hello;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    private final Counter counter;

    public HelloService(MeterRegistry meterRegistry) {
        this.counter = meterRegistry.counter("hello.counter", "type", "hello");
    }

    /* @Timed annotation avoid boilerplate code like below,
    *  timer = Timer
                .builder("hello.process")
                .description("Time spent processing hello")
                .tags("type", "hello")
                .register(meterRegistry);
    * */
    @Timed(value = "hello-process", description = "Time spent processing hello", extraTags = {"type", "hello"})
    public String getHello() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        counter.increment();
        return "Hello, counter = " + counter.count();
    }
}
