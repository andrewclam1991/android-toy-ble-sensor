package com.andrewclam.weatherclient.feature.scanner.data.event;

import com.andrewclam.weatherclient.feature.scanner.model.ServiceEventModel;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

/**
 * User event data source
 */
public interface ServiceEventDataSource {
  void put(@NonNull ServiceEventModel model);

  Flowable<ServiceEventModel> get();
}
