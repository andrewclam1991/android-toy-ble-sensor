package com.andrewclam.weatherclient.feature.scanner.data.event;

import com.andrewclam.weatherclient.feature.scanner.model.ServiceEventModel;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.BehaviorProcessor;

/**
 * In memory implementation of a {@link ServiceEventDataSource}
 */
class ServiceEventCacheDataSource implements ServiceEventDataSource {

  @NonNull
  private final BehaviorProcessor<ServiceEventModel> mEventSource;

  @Inject
  ServiceEventCacheDataSource() {
    mEventSource = BehaviorProcessor.create();
  }

  @Override
  public void put(ServiceEventModel model) {
    mEventSource.onNext(model);
  }

  @Override
  public Flowable<ServiceEventModel> get() {
    return mEventSource.onBackpressureLatest();
  }
}
