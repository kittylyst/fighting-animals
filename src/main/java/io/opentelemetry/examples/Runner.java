/* Copyright (C) Red Hat 2023 */
package io.opentelemetry.examples;

import io.opentelemetry.examples.animal.AnimalApplication;
import org.springframework.boot.SpringApplication;

public class Runner {

  public static void main(String[] args) throws ClassNotFoundException {
    if (args.length > 0) {
      System.out.println("Running for class: " + args[0]);
      SpringApplication.run(Class.forName(args[0]), args);
    } else {
      System.out.println("Running for class: " + AnimalApplication.class.getName());
      SpringApplication.run(AnimalApplication.class, args);
    }
  }
}
