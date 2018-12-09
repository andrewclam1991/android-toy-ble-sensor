/*
 * Copyright 2018 Andrew Chi Heng Lam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andrewclam.weatherclient.data.source.peripheral;

import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.model.Peripheral;
import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;
import timber.log.Timber;

/**
 * Concrete implementation of a data source as a in memory cache
 * - Whenever a list is updated, notify the subscribers
 */
@Singleton
class PeripheralsCacheDataSource implements PeripheralsDataSource {

  @NonNull
  private final List<Peripheral> mPeripherals;

  @Nonnull
  private final PublishProcessor<Peripheral> mCachePeripheralsPublisher;

  @Inject
  PeripheralsCacheDataSource() {
    mPeripherals = new LinkedList<>();
    mCachePeripheralsPublisher = PublishProcessor.create();
  }

  @NonNull
  @Override
  public Flowable<Optional<Peripheral>> get(@NonNull String id) {
    Timber.w("Data source doesn't support get by id");
    return Flowable.empty();
  }

  @NonNull
  @Override
  public Flowable<List<Peripheral>> getAll() {
    Timber.w("Data source doesn't support get all by collection");
    return Flowable.empty();
  }

  @NonNull
  @Override
  public Completable add(@NonNull Peripheral item) {
    return Completable.create(emitter -> {
      Timber.d("Add item: %s", item.getUid());
      mPeripherals.add(item);
      mCachePeripheralsPublisher.onNext(item);
      emitter.onComplete();
    });
  }

  @NonNull
  @Override
  public Completable add(@NonNull List<Peripheral> items) {
    Timber.w("Data source doesn't support bulk insert");
    return Completable.complete();
  }

  @NonNull
  @Override
  public Completable update(@NonNull Peripheral item) {
    return add(item);
  }

  @NonNull
  @Override
  public Completable delete(@NonNull String id) {
    Timber.w("Data source doesn't support delete by id");
    return Completable.complete();
  }

  @NonNull
  @Override
  public Completable deleteAll() {
    Timber.w("Data source doesn't support delete all");
    return Completable.complete();
  }

  @Override
  public void refresh() {

  }

  @NonNull
  @Override
  public Flowable<Optional<Peripheral>> get() {
    return null;
  }
}
