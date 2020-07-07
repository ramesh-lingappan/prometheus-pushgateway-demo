package com.example.prometheuspushgateway.config.prometheus;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.BasicAuthHttpConnectionFactory;
import io.prometheus.client.exporter.PushGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusProperties;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusPushGatewayManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;

@Configuration
public class PushGatewayConfiguration {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(@Value("${spring.profiles.active:default}") String activeEnvProfile) {
        return registry -> registry.config()
                .commonTags(
                        "env", activeEnvProfile
                );
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }


    @ConditionalOnProperty(prefix = "push-gateway.credentials", value = "enabled", havingValue = "true")
    @Bean
    @Primary
    public PrometheusPushGatewayManager prometheusPushGatewayManager(CollectorRegistry collectorRegistry,
                                                                     PrometheusProperties prometheusProperties,
                                                                     Environment environment,
                                                                     PushGatewayCredentials credentials) throws MalformedURLException {
        PrometheusProperties.Pushgateway properties = prometheusProperties.getPushgateway();
        Duration pushRate = properties.getPushRate();
        String job = this.getJob(properties, environment);
        Map<String, String> groupingKey = properties.getGroupingKey();
        PrometheusPushGatewayManager.ShutdownOperation shutdownOperation = properties.getShutdownOperation();
        return new PrometheusPushGatewayManager(this.getPushGateway(properties.getBaseUrl(), credentials), collectorRegistry, pushRate, job, groupingKey, shutdownOperation);
    }

    private PushGateway getPushGateway(String url, PushGatewayCredentials credentials) throws MalformedURLException {
        PushGateway pushGateway = new PushGateway(new URL(url));
        pushGateway.setConnectionFactory(new BasicAuthHttpConnectionFactory(credentials.getUsername(), credentials.getPassword()));
        return pushGateway;
    }

    private static String getJob(PrometheusProperties.Pushgateway properties, Environment environment) {
        String job = properties.getJob();
        job = job != null ? job : environment.getProperty("spring.application.name");
        return job != null ? job : "demo-service";
    }
}
