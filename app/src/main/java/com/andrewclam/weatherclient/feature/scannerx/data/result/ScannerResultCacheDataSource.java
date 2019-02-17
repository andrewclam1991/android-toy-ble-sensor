package com.andrewclam.weatherclient.feature.scannerx.data.result;

import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXResult;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.BehaviorProcessor;

class ScannerResultCacheDataSource implements ScannerResultDataSource {

  @NonNull
  private final BehaviorProcessor<ScannerXResult> mResultSource;

  @Inject
  ScannerResultCacheDataSource() {
    mResultSource = BehaviorProcessor.create();
  }

  @Override
  public void add(ScannerXResult model) {
    mResultSource.onNext(model);
  }

  @Override
  public Flowable<ScannerXResult> get() {
    return mResultSource.throttleLast(1, TimeUnit.SECONDS);
  }
}
