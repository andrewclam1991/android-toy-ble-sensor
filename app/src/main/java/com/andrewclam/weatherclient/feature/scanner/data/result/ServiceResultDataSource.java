package com.andrewclam.weatherclient.feature.scanner.data.result;

import com.andrewclam.weatherclient.feature.scanner.model.ServiceResultModel;

import io.reactivex.Flowable;

public interface ServiceResultDataSource {
  void add(ServiceResultModel model);

  Flowable<ServiceResultModel> get();
}
