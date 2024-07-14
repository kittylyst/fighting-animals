/* Copyright (C) Red Hat 2024 */
package io.fighting.animals.hospital;

import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.concurrent.CompletionStage;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

@ApplicationScoped
public class VeterinaryHospital {

  public static final String INGRESS_CHANNEL = "ingress";

  @PostConstruct
  public void init() {}

  @Incoming(INGRESS_CHANNEL)
  @Blocking
  @Transactional
  public CompletionStage<Void> processMainFlow(Message<String> message) {
    var payload = message.getPayload();
    Log.infof("Processed message: %s", payload);

    return message.ack();
  }
}
