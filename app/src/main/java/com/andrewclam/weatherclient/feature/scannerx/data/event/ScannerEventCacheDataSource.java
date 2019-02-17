package com.andrewclam.weatherclient.feature.scannerx.data.event;

import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXEvent;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.BehaviorProcessor;

/**
 * In memory implementation of a {@link ScannerEventDataSource}
 */
class ScannerEventCacheDataSource implements ScannerEventDataSource {

  @NonNull
  private final BehaviorProcessor<String> mEventSource;

  @Inject
  ScannerEventCacheDataSource() {
    mEventSource = BehaviorProcessor.create();
  }

  @Override
  public void put(@NonNull @ScannerXEvent String model) {
    mEventSource.onNext(model);
  }

  @Override
  public Flowable<String> get() {
    return mEventSource.onBackpressureLatest();
  }
}
