package com.ids.snort.dto;

import java.time.LocalDateTime;

public class AlertResponse {
  private Long id;
  private LocalDateTime eventTime;
  private String message;
  private Integer priority;
  private String severity;
  private String protocol;
  private String srcIp;
  private Integer srcPort;
  private String dstIp;
  private Integer dstPort;

  public AlertResponse(Long id, LocalDateTime eventTime, String message, Integer priority,
      String severity, String protocol, String srcIp, Integer srcPort, String dstIp,
      Integer dstPort) {
    this.id = id;
    this.eventTime = eventTime;
    this.message = message;
    this.priority = priority;
    this.severity = severity;
    this.protocol = protocol;
    this.srcIp = srcIp;
    this.srcPort = srcPort;
    this.dstIp = dstIp;
    this.dstPort = dstPort;
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getEventTime() {
    return eventTime;
  }

  public String getMessage() {
    return message;
  }

  public Integer getPriority() {
    return priority;
  }

  public String getSeverity() {
    return severity;
  }

  public String getProtocol() {
    return protocol;
  }

  public String getSrcIp() {
    return srcIp;
  }

  public Integer getSrcPort() {
    return srcPort;
  }

  public String getDstIp() {
    return dstIp;
  }

  public Integer getDstPort() {
    return dstPort;
  }
}
