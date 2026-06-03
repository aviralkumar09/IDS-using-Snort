package com.ids.snort.util;

import com.ids.snort.model.Alert;
import com.ids.snort.model.Severity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class SnortAlertParser {
  private static final Pattern ALERT_PATTERN = Pattern.compile(
      "^(\\d{2}/\\d{2}-\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?)\\s+\\[\\*\\*\\]\\s+\\[(\\d+):(\\d+):(\\d+)\\]\\s+(.*?)\\s+\\[\\*\\*\\]\\s+\\[Priority: (\\d+)\\]\\s+\\{(\\w+)\\}\\s+([^\\s]+)\\s+->\\s+([^\\s]+)");

  public Alert parse(String line) {
    Matcher matcher = ALERT_PATTERN.matcher(line);
    if (!matcher.find()) {
      return null;
    }

    String timestamp = matcher.group(1);
    String message = matcher.group(4).trim();
    int priority = Integer.parseInt(matcher.group(5));
    String protocol = matcher.group(6);
    String src = matcher.group(7);
    String dst = matcher.group(8);

    Alert alert = new Alert();
    alert.setEventTime(parseTimestamp(timestamp));
    alert.setMessage(message);
    alert.setPriority(priority);
    alert.setSeverity(mapSeverity(priority));
    alert.setProtocol(protocol);
    String[] srcParts = splitAddress(src);
    alert.setSrcIp(srcParts[0]);
    if (srcParts[1] != null) {
      alert.setSrcPort(Integer.parseInt(srcParts[1]));
    }
    String[] dstParts = splitAddress(dst);
    alert.setDstIp(dstParts[0]);
    if (dstParts[1] != null) {
      alert.setDstPort(Integer.parseInt(dstParts[1]));
    }
    alert.setRawLine(line);
    return alert;
  }

  private LocalDateTime parseTimestamp(String raw) {
    var formatter = new DateTimeFormatterBuilder()
        .appendPattern("MM/dd-HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.MICRO_OF_SECOND, 1, 6, true)
        .optionalEnd()
        .toFormatter();
    var accessor = formatter.parse(raw);
    int month = accessor.get(ChronoField.MONTH_OF_YEAR);
    int day = accessor.get(ChronoField.DAY_OF_MONTH);
    int hour = accessor.get(ChronoField.HOUR_OF_DAY);
    int minute = accessor.get(ChronoField.MINUTE_OF_HOUR);
    int second = accessor.get(ChronoField.SECOND_OF_MINUTE);
    int nano = accessor.isSupported(ChronoField.NANO_OF_SECOND)
        ? accessor.get(ChronoField.NANO_OF_SECOND)
        : 0;
    int year = LocalDate.now().getYear();
    return LocalDateTime.of(year, month, day, hour, minute, second, nano);
  }

  private Severity mapSeverity(int priority) {
    if (priority <= 1) {
      return Severity.HIGH;
    }
    if (priority == 2) {
      return Severity.MEDIUM;
    }
    if (priority == 3) {
      return Severity.LOW;
    }
    return Severity.INFO;
  }

  private String[] splitAddress(String value) {
    String[] parts = value.split(":");
    if (parts.length >= 2) {
      return new String[] {parts[0], parts[1]};
    }
    return new String[] {value, null};
  }
}
