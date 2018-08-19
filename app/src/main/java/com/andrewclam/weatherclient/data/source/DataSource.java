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
 *
 * com.andrewclam.weatherclient.data.source.DataSource
 */

package com.andrewclam.weatherclient.data.source;

import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.model.BaseModel;
import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Generic API to the model layer
 */
public interface DataSource<M extends BaseModel> {

  /**
   * Get item by id of type {@link M} from its data layer
   *
   * @param id unique id of a {@link M} type
   * @return observable that when subscribed to, would fetch and emit the item
   * and error otherwise.
   */
  @NonNull
  Flowable<Optional<M>> get(@NonNull String id);

  /**
   * Gets all items of type {@link M} from the data layer
   *
   * @return a hot observable that when subscribed to, emits model updates to the
   * list of {@link M} items.
   */
  @NonNull
  Flowable<List<M>> getAll();

  /**
   * Puts a single item of type {@link M} to the data layer
   *
   * @param item item to be add into the model layer
   * @return an observable that when subscribed to, emits {@link Completable#complete()}
   * when the operation is completeTrip and error otherwise.
   */
  @NonNull
  Completable add(@NonNull M item);

  /**
   * Puts a list of items of type {@link M} to the data layer
   *
   * @param items collection of items to be add into the model layer
   * @return an observable that when subscribed to, emits {@link Completable#complete()}
   * when the operation is completeTrip and error otherwise.
   */
  @NonNull
  Completable add(@NonNull List<M> items);

  /**
   * Updates a particular item of type {@link M} to the data layer
   *
   * @param item the updated item data
   * @return an observable that when subscribed to, emits {@link Completable#complete()}
   * when the operation is completeTrip and error otherwise.
   */
  @NonNull
  Completable update(@NonNull M item);

  /**
   * Deletes a particular item by id of type {@link M} from the data layer
   *
   * @param id unique id of the item
   * @return an observable that when subscribed to, emits {@link Completable#complete()}
   * when the operation is completeTrip and error otherwise.
   */
  @NonNull
  Completable delete(@NonNull String id);

  /**
   * Deletes all items of type {@link M} from the data layer
   *
   * @return an observable that when subscribed to, emits {@link Completable#complete()}
   * when the operation is completeTrip and error otherwise.
   */
  @NonNull
  Completable deleteAll();

  /**
   * Forces implementation to clear in-memory cache
   * and fetch data from the persistent model layer(s)
   */
  void refresh();
}
