# Spring Boot Prometheus PushGateway Demo

This is a demo app using [Spring Boot] 2.2 with [Micrometer] to publish metrics to [Prometheus] using [Prometheus Push Gateway].

The Prometheus Pushgateway exists to allow ephemeral and batch jobs to expose their metrics to Prometheus. Since these kinds of jobs may not exist long enough to be scraped, they can instead push their metrics to a Pushgateway. The Pushgateway then exposes these metrics to Prometheus.

# Setup

## Prometheus Push Gateway (without basic authentication)

Using the `docker-compose` command setup the necessary services 

~~~
cd setup
docker-compose up
~~~

## Prometheus Push Gateway (with basic authentication)

Using the `docker-compose` command setup the necessary services 

~~~
cd setup-security
docker-compose up
~~~

This will setup Prometheus & PushGateway instances 

Second, open the browser and access the address 
- Prometheus : `http://localhost:9090`
- PushGateway : `http://localhost:9091`

# Application

Start the application

~~~
mvn spring-boot:run
~~~

with security enabled, this will use basic authentication to connect to push-gateway
~~~
mvn spring-boot:run -Drun.profiles=secure
~~~

NOTE: make you the basic auth credentials used in `demo-security/htpasswd` and `application.yml` are same.

# Testing

First, call the endpoint `http://localhost:8080/hello`

Second, open the Prometheus Push Gateway `http://localhost:9091` and see the metrics, mainly the `hello_counter`

Third, you can also open Prometheus dashboard `http://localhost:9090` to check the metrics


[Spring Boot]: https://spring.io/blog/2019/10/16/spring-boot-2-2-0
[Prometheus]: https://prometheus.io
[Prometheus Push Gateway]: https://github.com/prometheus/pushgateway
[Micrometer]: https://github.com/micrometer-metrics/micrometer
