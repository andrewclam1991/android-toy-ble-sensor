package com.andrewclam.weatherclient.feature.scanner.data;

import com.andrewclam.weatherclient.feature.scanner.model.ServiceEventModel;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;

class ServiceEventRepository implements ServiceEventDataSource {

  private final BehaviorProcessor<ServiceEventModel> mStream;

  @Inject
  ServiceEventRepository() {
    mStream = BehaviorProcessor.create();
  }

  @Override
  public void put(ServiceEventModel model) {
    mStream.onNext(model);
  }

  @Override
  public Flowable<ServiceEventModel> get() {
    return mStream;
  }
}
