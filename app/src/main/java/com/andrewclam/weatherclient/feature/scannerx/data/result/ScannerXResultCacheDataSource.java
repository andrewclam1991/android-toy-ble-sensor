package com.andrewclam.weatherclient.feature.scannerx.data.result;

import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXResult;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.schedulers.Schedulers;

class ScannerXResultCacheDataSource implements ScannerXResultDataSource {

  @NonNull
  private final BehaviorProcessor<ScannerXResult> mResultSource;

  @Inject
  ScannerXResultCacheDataSource() {
    mResultSource = BehaviorProcessor.create();
  }

  @Override
  public void add(ScannerXResult model) {
    mResultSource.onNext(model);
  }

  @Override
  public Flowable<ScannerXResult> get() {
    return mResultSource.throttleLast(1, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.io());
  }
}
