package com.ids.snort.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime eventTime;

  @Column(nullable = false, length = 512)
  private String message;

  @Column(nullable = false)
  private Integer priority;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Severity severity;

  @Column(length = 12)
  private String protocol;

  @Column(length = 64)
  private String srcIp;

  private Integer srcPort;

  @Column(length = 64)
  private String dstIp;

  private Integer dstPort;

  @Lob
  private String rawLine;

  @Column(nullable = false)
  private Instant createdAt;

  @PrePersist
  public void onCreate() {
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getEventTime() {
    return eventTime;
  }

  public void setEventTime(LocalDateTime eventTime) {
    this.eventTime = eventTime;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public Severity getSeverity() {
    return severity;
  }

  public void setSeverity(Severity severity) {
    this.severity = severity;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getSrcIp() {
    return srcIp;
  }

  public void setSrcIp(String srcIp) {
    this.srcIp = srcIp;
  }

  public Integer getSrcPort() {
    return srcPort;
  }

  public void setSrcPort(Integer srcPort) {
    this.srcPort = srcPort;
  }

  public String getDstIp() {
    return dstIp;
  }

  public void setDstIp(String dstIp) {
    this.dstIp = dstIp;
  }

  public Integer getDstPort() {
    return dstPort;
  }

  public void setDstPort(Integer dstPort) {
    this.dstPort = dstPort;
  }

  public String getRawLine() {
    return rawLine;
  }

  public void setRawLine(String rawLine) {
    this.rawLine = rawLine;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
