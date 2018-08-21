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
 * com.andrewclam.weatherclient.service.pairing.PairingService
 */

package com.andrewclam.weatherclient.service.pairing;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.andrewclam.weatherclient.data.source.Repo;
import com.andrewclam.weatherclient.data.source.peripheral.PeripheralsDataSource;
import com.andrewclam.weatherclient.model.Peripheral;
import com.andrewclam.weatherclient.schedulers.BaseSchedulerProvider;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Responsible for scanning for ble peripheral devices, handling on ble device click
 * TODO andrew move responsibilites to controller class
 */
public class PairingService extends DaggerService implements PairingServiceContract.Service {

  @NonNull
  private final PeripheralsDataSource mPeripheralsRepository;

  @NonNull
  private final BaseSchedulerProvider mSchedulerProvider;

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  @NonNull
  private Map<String, PairingServiceContract.View> mViewObservers;

  @Inject
  PairingService(@NonNull @Repo PeripheralsDataSource peripheralsRepository,
                 @NonNull BaseSchedulerProvider schedulerProvider) {
    mPeripheralsRepository = peripheralsRepository;
    mSchedulerProvider = schedulerProvider;
    mCompositeDisposable = new CompositeDisposable();
    mViewObservers = new LinkedHashMap<>();
  }

  @Override
  public void scanPeripherals() {
    Disposable disposable = mPeripheralsRepository.scan()
        .subscribeOn(mSchedulerProvider.io())
        .observeOn(mSchedulerProvider.ui())
        .subscribe();

    mCompositeDisposable.add(disposable);
  }

  @Override
  public boolean isScanning() {
    return mPeripheralsRepository.isScanning();
  }

  @Override
  public void handleOnPeripheralClicked(Peripheral peripheral) {

  }

  @NonNull
  @Override
  public String addView(@NonNull PairingServiceContract.View view) {
    String viewTag = UUID.randomUUID().toString();
    mViewObservers.put(viewTag, view);
    return viewTag;
  }

  @Override
  public void dropView(@NonNull String viewTag) {
    if (mViewObservers.isEmpty() || !mViewObservers.containsKey(viewTag)) {
      return; // no observers
    }
    mViewObservers.remove(viewTag);
  }

  @Override
  public void startService() {

  }

  @Override
  public void stopService() {
    mViewObservers.clear();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
