package com.ids.snort.dto;

public class RuleResponse {
  private String sid;
  private String name;
  private String description;
  private String severity;
  private boolean enabled;

  public RuleResponse(String sid, String name, String description, String severity, boolean enabled) {
    this.sid = sid;
    this.name = name;
    this.description = description;
    this.severity = severity;
    this.enabled = enabled;
  }

  public String getSid() {
    return sid;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getSeverity() {
    return severity;
  }

  public boolean isEnabled() {
    return enabled;
  }
}
