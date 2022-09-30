package io.opentelemetry.examples.mustelid;

import io.opentelemetry.examples.utils.OpenTelemetryConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

    @SpringBootApplication
    public class MustelidApplication {

        public static void main(String[] args) {
            // Configure OpenTelemetry as early as possible
            OpenTelemetryConfig.configureGlobal("feline-app");
            SpringApplication.run(MustelidApplication.class, args);
        }
    }

