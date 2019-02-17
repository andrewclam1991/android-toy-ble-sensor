package com.andrewclam.weatherclient.feature.scanner.data.result;

import com.andrewclam.weatherclient.feature.scanner.model.ScannerResult;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.BehaviorProcessor;

class ScannerResultCacheDataSource implements ScannerResultDataSource {

  @NonNull
  private final BehaviorProcessor<ScannerResult> mResultSource;

  @Inject
  ScannerResultCacheDataSource() {
    mResultSource = BehaviorProcessor.create();
  }

  @Override
  public void add(ScannerResult model) {
    mResultSource.onNext(model);
  }

  @Override
  public Flowable<ScannerResult> get() {
    return mResultSource.onBackpressureBuffer(10);
  }
}
