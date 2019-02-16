package com.andrewclam.weatherclient.feature.scanner;

import com.andrewclam.weatherclient.feature.scanner.model.ServiceEventModel;
import com.andrewclam.weatherclient.feature.scanner.model.ServiceModel;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;

public class ScannerContract {
  interface Service {
    void showInProgress();

    void showComplete();

    void showError(String message);
  }

  interface Controller {
    // NOTE only exposes the event source with platform intent action approach
    BehaviorProcessor<ServiceEventModel> getEventSource();

    Flowable<ServiceModel> getModel();

    void start();
    void stop();
  }
}
