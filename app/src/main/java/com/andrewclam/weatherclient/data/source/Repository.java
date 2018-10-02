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

package com.andrewclam.weatherclient.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.andrewclam.weatherclient.model.BaseModel;
import com.google.common.base.Optional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generic Repository implementation of a {@link DataSource <>}
 * that provides implementation for common base CRUD methods
 *
 * @param <M>
 */
@Singleton
public class Repository<M extends BaseModel> implements DataSource<M> {

  @NonNull
  private final DataSource<M> mLocalDataSource;

  @NonNull
  private final DataSource<M> mRemoteDataSource;

  /**
   * This variable has package local visibility so it can be accessed from tests.
   */
  @VisibleForTesting
  @NonNull
  final Map<String, M> mCachedItems;

  /**
   * Marks the cache as invalid, to force an updateTrip the next time data is requested. This
   * variable
   * has package local visibility so it can be accessed from tests.
   * Note: default set flag to false, so at init (with mCachedItems empty),
   * repository will always try local-first
   */
  @VisibleForTesting
  boolean mCacheIsDirty = false;

  @VisibleForTesting
  @Inject
  protected Repository(@NonNull @Local DataSource<M> localDataSource,
                       @NonNull @Remote DataSource<M> remoteDataSource) {
    mLocalDataSource = checkNotNull(localDataSource, "localDataSource can't be null!");
    mRemoteDataSource = checkNotNull(remoteDataSource, "remoteDataSource can't be null!");
    mCachedItems = new LinkedHashMap<>();
  }

  @Override
  public void refresh() {
    mCacheIsDirty = true;
  }

  @NonNull
  @Override
  public Completable add(@NonNull M item) {
    mCachedItems.put(item.getUid(), checkNotNull(item));
    return mLocalDataSource.add(item).andThen(mRemoteDataSource.add(item));
  }

  @NonNull
  @Override
  public Completable add(@NonNull List<M> items) {
    for (M item : items) {
      mCachedItems.put(item.getUid(), checkNotNull(item));
    }
    return mLocalDataSource.add(items).andThen(mRemoteDataSource.add(items));
  }

  @NonNull
  @Override
  public Flowable<List<M>> getAll() {
    // Respond immediately with cache if available and not dirty
    if (!mCachedItems.isEmpty() && !mCacheIsDirty) {
      return Flowable.fromIterable(mCachedItems.values()).toList().toFlowable();
    }

    // Repository starts with clean cache (mCacheIsDirty = false);
    // Queries local first
    // if local empty, try remote,
    // if local has data, add each item to cache, return data.
    // if remote empty, return no data.
    // if remote has data, add each item to local and cache, mark cache as clean return data.

    Flowable<List<M>> remoteItems = getAndSaveRemoteItems();

    if (mCacheIsDirty) {
      // refresh local data with remote
      return remoteItems;
    } else {
      // query local and remote data sources, emit the first result
      Flowable<List<M>> localItems = getAndCacheLocalItems();
      return Flowable.concat(localItems, remoteItems)
          .filter(items -> !items.isEmpty())
          .firstOrError()
          .toFlowable();
    }
  }

  @NonNull
  @Override
  public Flowable<Optional<M>> get(@NonNull String itemId) {
    checkNotNull(itemId);

    final M cachedItem = getItemWithIdFromCache(itemId);

    // Respond with the if it is available in cache
    if (cachedItem != null) {
      return Flowable.just(Optional.of(cachedItem));
    }

    // Create an Observable to query the item in the local data source
    Flowable<Optional<M>> localItem = getLocalItemById(itemId);

    // Create an Observable to query the item in the remote data source, and download it
    Flowable<Optional<M>> remoteItem = getRemoteItemById(itemId);

    // Concat the local and remote sources into one,
    return Flowable.concat(localItem, remoteItem)
        .firstElement()
        .toFlowable();
  }

  @NonNull
  @Override
  public Completable update(@NonNull M item) {
    mCachedItems.put(item.getUid(), item);
    return mLocalDataSource.update(item).andThen(mRemoteDataSource.update(item));
  }

  @NonNull
  @Override
  public Completable delete(@NonNull String entityId) {
    if (!mCachedItems.isEmpty()) {
      mCachedItems.remove(entityId);
    }
    return mLocalDataSource.delete(entityId).andThen(mRemoteDataSource.delete(entityId));
  }

  @NonNull
  @Override
  public Completable deleteAll() {
    mCachedItems.clear();
    return mLocalDataSource.deleteAll().andThen(mRemoteDataSource.deleteAll());
  }

  @NonNull
  private Flowable<List<M>> getAndCacheLocalItems() {
    return mLocalDataSource.getAll()
        .flatMap(items -> Flowable.fromIterable(items)
            .doOnNext(this::saveItemToCache)
            .toList()
            .toFlowable()
        )
        .takeWhile(items -> !items.isEmpty());// this completes the stream when the list becomes
    // empty
  }

  @NonNull
  private Flowable<List<M>> getAndSaveRemoteItems() {
    return mRemoteDataSource.getAll()
        .flatMap(items -> Flowable.fromIterable(items)
            .doOnNext(item -> mLocalDataSource.add(item).andThen(saveItemToCache(item)))
            .toList()
            .toFlowable()
        ).doOnComplete(() -> mCacheIsDirty = false);
  }

  @NonNull
  private Flowable<Optional<M>> getLocalItemById(@NonNull final String itemId) {
    return mLocalDataSource.get(itemId)
        .takeWhile(Optional::isPresent)
        .flatMap(itemOptional -> {
          if (itemOptional.isPresent()) {
            M item = itemOptional.get();
            return saveItemToCache(item)
                .andThen(Flowable.just(itemOptional));
          } else {
            return Flowable.just(Optional.absent());
          }
        });
  }

  @NonNull
  private Flowable<Optional<M>> getRemoteItemById(@NonNull final String itemId) {
    return mRemoteDataSource.get(itemId)
        .flatMap(itemOptional -> {
          if (itemOptional.isPresent()) {
            M item = itemOptional.get();
            return mLocalDataSource.add(item)
                .andThen(saveItemToCache(item))
                .andThen(Flowable.just(itemOptional));
          } else {
            return Flowable.just(Optional.absent());
          }
        });
  }

  @VisibleForTesting
  private Completable saveItemToCache(@NonNull M item) {
    mCachedItems.put(item.getUid(), item);
    return Completable.complete();
  }

  @Nullable
  private M getItemWithIdFromCache(@NonNull final String itemId) {
    checkNotNull(itemId);

    if (mCachedItems.isEmpty()) {
      return null;
    } else {
      // still might not be able to find the item with id, return a nullable item
      return mCachedItems.get(itemId);
    }
  }
}
