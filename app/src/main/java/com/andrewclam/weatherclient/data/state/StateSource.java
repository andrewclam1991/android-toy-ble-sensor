package com.andrewclam.weatherclient.data.state;

import com.andrewclam.weatherclient.model.BaseModel;

import javax.annotation.Nonnull;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Reactive and thread-safe state get and set interface
 */
public interface StateSource<S extends BaseModel> {
  @Nonnull
  Flowable<S> get();

  @Nonnull
  Completable set(@Nonnull S source);
}
