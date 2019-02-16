package com.andrewclam.weatherclient.feature.scanner.model;

import java.util.Objects;

/**
 * Model for data consumer (data)
 * eg. user manipulation, changes, commands
 * view -> controller -> data
 */
public enum ServiceEventModel {
  START_SCAN("event_start_scan"),
  STOP_SCAN("event_stop_scan");

  String mEventId;

  ServiceEventModel(String eventId) {
    mEventId = eventId;
  }

  public ServiceEventModel getEvent(String eventId) {
    Objects.requireNonNull(eventId, "event id is null.");
    for (ServiceEventModel model : ServiceEventModel.values()) {
      if (model.mEventId.equals(eventId)) {
        return model;
      }
    }
    throw new IllegalArgumentException("Unsupported event id: " + eventId);
  }
}
