/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.feline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FelineApplication {

  public static void main(String[] args) {
    SpringApplication.run(FelineApplication.class, args);
  }
}
