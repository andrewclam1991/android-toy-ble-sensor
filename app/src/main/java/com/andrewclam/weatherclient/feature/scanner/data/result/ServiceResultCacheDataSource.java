package com.andrewclam.weatherclient.feature.scanner.data.result;

import com.andrewclam.weatherclient.feature.scanner.model.ServiceResultModel;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.BehaviorProcessor;

class ServiceResultCacheDataSource implements ServiceResultDataSource {

  @NonNull
  private final BehaviorProcessor<ServiceResultModel> mResultSource;

  @Inject
  ServiceResultCacheDataSource() {
    mResultSource = BehaviorProcessor.create();
  }

  @Override
  public void add(ServiceResultModel model) {
    mResultSource.onNext(model);
  }

  @Override
  public Flowable<ServiceResultModel> get() {
    return mResultSource.onBackpressureBuffer(10);
  }
}
