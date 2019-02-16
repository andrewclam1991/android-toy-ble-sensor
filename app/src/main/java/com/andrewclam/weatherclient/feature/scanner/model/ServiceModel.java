package com.andrewclam.weatherclient.feature.scanner.model;

/**
 * Model for UI consumer (users)
 * eg. data changes and updates
 * data -> controller -> view
 */
public class ServiceModel {
  private final boolean isInProgress;
  private final boolean isComplete;
  private final boolean isError;
  private final String errorMessage;

  private ServiceModel(boolean isInProgress, boolean isComplete, boolean isError, String errorMessage) {
    this.isInProgress = isInProgress;
    this.isComplete = isComplete;
    this.isError = isError;
    this.errorMessage = errorMessage;
  }

  public static ServiceModel inProgress() {
    return new ServiceModel(true, false, false, "no_error");
  }

  public static ServiceModel complete() {
    return new ServiceModel(false, true, false, "no_error");
  }

  public static ServiceModel error(String errorMessage) {
    return new ServiceModel(false, false, true, errorMessage);
  }

  public boolean isInProgress() {
    return isInProgress;
  }

  public boolean isComplete() {
    return isComplete;
  }

  public boolean isError() {
    return isError;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
