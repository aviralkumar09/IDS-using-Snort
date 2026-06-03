package com.ids.snort.service;

import com.ids.snort.dto.AlertResponse;
import com.ids.snort.model.Alert;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class LiveAlertPublisher {
  private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  public SseEmitter subscribe() {
    SseEmitter emitter = new SseEmitter(0L);
    emitters.add(emitter);
    emitter.onCompletion(() -> emitters.remove(emitter));
    emitter.onTimeout(() -> emitters.remove(emitter));
    return emitter;
  }

  public void publish(Alert alert) {
    AlertResponse response = new AlertResponse(
        alert.getId(),
        alert.getEventTime(),
        alert.getMessage(),
        alert.getPriority(),
        alert.getSeverity().name(),
        alert.getProtocol(),
        alert.getSrcIp(),
        alert.getSrcPort(),
        alert.getDstIp(),
        alert.getDstPort());

    for (SseEmitter emitter : emitters) {
      try {
        emitter.send(SseEmitter.event().name("alert").data(response));
      } catch (IOException ex) {
        emitters.remove(emitter);
      }
    }
  }
}
