package com.andrewclam.weatherclient.feature.scannerx.data.result;

import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXResult;

import io.reactivex.Flowable;

public interface ScannerXResultDataSource {
  void add(ScannerXResult model);

  Flowable<ScannerXResult> get();
}