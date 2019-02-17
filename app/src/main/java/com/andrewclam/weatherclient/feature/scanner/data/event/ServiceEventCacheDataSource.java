package com.andrewclam.weatherclient.feature.scanner.data.event;

import com.andrewclam.weatherclient.feature.scanner.model.ScannerEvent;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.BehaviorProcessor;

/**
 * In memory implementation of a {@link ServiceEventDataSource}
 */
class ServiceEventCacheDataSource implements ServiceEventDataSource {

  @NonNull
  private final BehaviorProcessor<String> mEventSource;

  @Inject
  ServiceEventCacheDataSource() {
    mEventSource = BehaviorProcessor.create();
  }

  @Override
  public void put(@NonNull @ScannerEvent String model) {
    mEventSource.onNext(model);
  }

  @Override
  public Flowable<String> get() {
    return mEventSource.onBackpressureLatest();
  }
}
