package com.andrewclam.weatherclient.feature.scannerx.data.event;

import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXEvent;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

/**
 * User event data source
 */
public interface ScannerXEventDataSource {
  void put(@NonNull @ScannerXEvent String model);

  @ScannerXEvent
  Flowable<String> get();
}
