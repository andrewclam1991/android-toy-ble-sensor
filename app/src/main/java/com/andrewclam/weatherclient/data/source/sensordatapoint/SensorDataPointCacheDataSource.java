package com.andrewclam.weatherclient.data.source.sensordatapoint;

import com.andrewclam.weatherclient.model.DataPoint;
import com.andrewclam.weatherclient.model.SensorDataPoint;
import com.google.common.base.Optional;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * In memory implementation of {@link SensorDataPointDataSource}
 */
public class SensorDataPointCacheDataSource implements SensorDataPointDataSource {
  @NonNull
  @Override
  public Flowable<List<DataPoint>> getDataPointsBySensorId(@NonNull String sensorId) {
    return null;
  }

  @NonNull
  @Override
  public Completable deleteAll(@NonNull String sensorId) {
    return null;
  }

  @NonNull
  @Override
  public Flowable<Optional<SensorDataPoint>> get(@NonNull String id) {
    return null;
  }

  @NonNull
  @Override
  public Flowable<List<SensorDataPoint>> getAll() {
    return null;
  }

  @NonNull
  @Override
  public Completable add(@NonNull SensorDataPoint item) {
    return null;
  }

  @NonNull
  @Override
  public Completable add(@NonNull List<SensorDataPoint> items) {
    return null;
  }

  @NonNull
  @Override
  public Completable update(@NonNull SensorDataPoint item) {
    return null;
  }

  @NonNull
  @Override
  public Completable delete(@NonNull String id) {
    return null;
  }

  @NonNull
  @Override
  public Completable deleteAll() {
    return null;
  }

  @Override
  public void refresh() {

  }
}
