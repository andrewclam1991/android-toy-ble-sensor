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
 * com.andrewclam.weatherclient.data.roomdb.BaseDao
 */

package com.andrewclam.weatherclient.data.roomdb;

import com.andrewclam.weatherclient.model.BaseModel;
import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Base CRUD operations that all {@link BaseModel}
 * should have.
 */
public interface BaseDao<M extends BaseModel> {
  /**
   * Inserts an item into the data layer
   *
   * @param item {@link BaseModel} type item to persist
   */
  void add(M item);

  /**
   * Inserts a list of items into the data layer
   *
   * @param items collection of {@link BaseModel} type item to persist
   */
  void addAll(List<M> items);

  /**
   * Updates a particular item within the data layer
   *
   * @param item {@link BaseModel} type item to update
   */
  void update(M item);

  /**
   * Gets an emission of items from the data layer
   *
   * @return an observable stream collection of {@link BaseModel} type items to retrieve
   */
  Flowable<List<M>> getAll();

  /**
   * Gets an emission of a particular item from the data layer
   *
   * @param itemId unique id of a particular {@link BaseModel} type item
   * @return an observable item that has the {@code itemId}
   */
  Flowable<Optional<M>> get(String itemId);

  /**
   * Deletes a particular item within the data layer
   *
   * @param itemId unique id of a particular {@link BaseModel} type item
   */
  void delete(String itemId);

  /**
   * Delete all items of type {@link M} within the data layer
   */
  void deleteAll();
}
