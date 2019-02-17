package com.andrewclam.weatherclient.feature.scanner;

import com.andrewclam.weatherclient.feature.scanner.model.ServiceResultModel;

import io.reactivex.Flowable;

public class ScannerContract {
  interface Service {
    void showInProgress();

    void showComplete();

    void showError(String message);
  }

  interface Controller {
    // NOTE only exposes the event source with platform intent action approach
    // BehaviorProcessor<ServiceEventModel> getEventSource();

    Flowable<ServiceResultModel> getModel();

    void start();

    void stop();
  }
}
