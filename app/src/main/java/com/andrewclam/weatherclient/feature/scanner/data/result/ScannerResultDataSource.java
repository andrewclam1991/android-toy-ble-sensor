package com.andrewclam.weatherclient.feature.scanner.data.result;

import com.andrewclam.weatherclient.feature.scanner.model.ScannerResult;

import io.reactivex.Flowable;

public interface ScannerResultDataSource {
  void add(ScannerResult model);

  Flowable<ScannerResult> get();
}
