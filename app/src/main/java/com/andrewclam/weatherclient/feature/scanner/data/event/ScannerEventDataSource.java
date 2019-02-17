package com.andrewclam.weatherclient.feature.scanner.data.event;

import com.andrewclam.weatherclient.feature.scanner.model.ScannerEvent;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

/**
 * User event data source
 */
public interface ScannerEventDataSource {
  void put(@NonNull @ScannerEvent String model);

  @ScannerEvent
  Flowable<String> get();
}
