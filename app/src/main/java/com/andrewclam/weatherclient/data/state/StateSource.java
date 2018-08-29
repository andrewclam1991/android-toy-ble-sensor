package com.andrewclam.weatherclient.data.state;

import com.andrewclam.weatherclient.model.BaseModel;

import javax.annotation.Nonnull;

import io.reactivex.Completable;
import io.reactivex.subjects.PublishSubject;

/**
 * Reactive and thread-safe state get and set interface
 */
public interface StateSource<S extends BaseModel> {
  @Nonnull
  PublishSubject<S> get();

  @Nonnull
  Completable set(@Nonnull S source);
}
