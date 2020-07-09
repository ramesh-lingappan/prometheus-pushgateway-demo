package com.example.prometheuspushgateway;

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

    @Timed(value = "hello-process", description = "Time spent processing hello", extraTags = {"type", "hello"})
    public String getHello() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        counter.increment();
        return "Hello, counter = " + counter.count();
    }
}
