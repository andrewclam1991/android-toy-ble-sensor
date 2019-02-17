package com.andrewclam.weatherclient.feature.scanner.data;

import com.andrewclam.weatherclient.feature.scanner.model.ServiceEventModel;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.BehaviorProcessor;

class ServiceEventRepository implements ServiceEventDataSource {

  @NonNull
  private final BehaviorProcessor<ServiceEventModel> mEventSource;

  @Inject
  ServiceEventRepository() {
    mEventSource = BehaviorProcessor.create();
  }

  @Override
  public void put(ServiceEventModel model) {
    mEventSource.onNext(model);
  }

  @Override
  public Flowable<ServiceEventModel> get() {
    return mEventSource;
  }
}
