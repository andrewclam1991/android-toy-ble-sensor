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

package com.andrewclam.weatherclient.view.scanner;

import com.andrewclam.weatherclient.data.source.Repo;
import com.andrewclam.weatherclient.data.source.peripheral.PeripheralsDataSource;
import com.andrewclam.weatherclient.data.state.StateSource;
import com.andrewclam.weatherclient.di.ActivityScoped;
import com.andrewclam.weatherclient.model.Peripheral;
import com.andrewclam.weatherclient.model.ScannerState;
import com.andrewclam.weatherclient.scheduler.BaseSchedulerProvider;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Presenter class responsible for listening to the data layer
 */
@ActivityScoped
class ScannerPresenter implements ScannerViewContract.Presenter, ScannerViewContract.ListPresenter {

  @Nonnull
  private final StateSource<ScannerState> mStateRepository;

  @Nonnull
  private final PeripheralsDataSource mPeripheralsRepository;

  @Nonnull
  private final BaseSchedulerProvider mSchedulerProvider;

  @Nonnull
  private final CompositeDisposable mCompositeDisposable;

  @Nonnull
  private final Set<ScannerViewContract.View> mViews;

  @NonNull
  private final List<Peripheral> mPeripherals;

  private boolean mFirstLoad;

  @Inject
  ScannerPresenter(@Nonnull @Repo StateSource<ScannerState> stateRepository,
                   @Nonnull @Repo PeripheralsDataSource peripheralsRepository,
                   @Nonnull BaseSchedulerProvider schedulerProvider) {
    mStateRepository = stateRepository;
    mPeripheralsRepository = peripheralsRepository;
    mSchedulerProvider = schedulerProvider;
    mCompositeDisposable = new CompositeDisposable();
    mViews = new LinkedHashSet<>();
    mPeripherals = new ArrayList<>();
    mFirstLoad = true;
  }

  @Override
  public void addView(@NonNull ScannerViewContract.View view) {
    mViews.add(view);
  }

  @Override
  public void dropView(@NonNull ScannerViewContract.View view) {
    if (mViews.isEmpty() || !mViews.contains(view)) {
      return; // no observers
    }
    mViews.remove(view);
    mCompositeDisposable.clear();
    mPeripherals.clear();
  }

  @Override
  public void loadScannerState() {
    Disposable disposable = mStateRepository.get()
        .subscribeOn(mSchedulerProvider.io())
        .observeOn(mSchedulerProvider.ui())
        .subscribe(this::onScannerStateChanged, this::onScannerStateError);

    mCompositeDisposable.add(disposable);
  }

  @Override
  public void loadPeripherals(boolean forceRefresh) {
    if (mFirstLoad || forceRefresh) {
      mPeripheralsRepository.refresh();
      mFirstLoad = false;
    }

    Disposable disposable = mPeripheralsRepository.getAll()
        .subscribeOn(mSchedulerProvider.io())
        .observeOn(mSchedulerProvider.ui())
        .subscribe(this::onNextPeripheral, this::onGetPeripheralError);

    mCompositeDisposable.add(disposable);
  }

  private void onScannerStateChanged(@Nonnull ScannerState state) {
    Timber.d("Got scanner state update");
    for (ScannerViewContract.View view : mViews) {
      if (view != null && view.isActive()) {
        view.showScannerInProgress(state.isActive());
      } else {
        Timber.w("view unavailable, ignore notify.");
      }
    }
  }

  private void onScannerStateError(@Nonnull Throwable throwable) {
    Timber.e(throwable, "Error getting the scanner state.");
  }

  private void onNextPeripheral(@Nonnull List<Peripheral> peripherals) {

    for (Peripheral item : peripherals) {
      Timber.d("Got peripherals updated, %s", item.getUid());
      if (!mPeripherals.contains(item)) {
        mPeripherals.add(item);
        for (ScannerViewContract.View view : mViews) {
          if (view != null && view.isActive()) {
            view.notifyItemInserted(mPeripherals.size() - 1);
          } else {
            Timber.w("view unavailable, ignore notify.");
          }
        }
      }
    }

  }

  private void onGetPeripheralError(@Nonnull Throwable throwable) {
    Timber.e(throwable, "Error getting peripherals.");
  }

  @Override
  public void onAdapterItemClicked(int adapterPosition) {
    if (adapterPosition < 0 || mPeripherals.get(adapterPosition) == null) {
      Timber.e("Invalid position or item at position is gone.");
      return;
    }
    Peripheral item = mPeripherals.get(adapterPosition);
    Timber.d("item mac address clicked: %s", item.getBluetoothDevice().getAddress());
  }

  @Override
  public void onAdapterBindViewHolder(@NonNull ScannerViewContract.ViewHolder viewHolder, int adapterPosition) {
    if (adapterPosition < 0 || mPeripherals.get(adapterPosition) == null) {
      Timber.e("Invalid position or item at position is gone.");
      return;
    }
    Peripheral item = mPeripherals.get(adapterPosition);
    viewHolder.showDeviceMacAddress(item.getBluetoothDevice().getAddress());
    viewHolder.showDeviceName(item.getBluetoothDevice().getName());
    viewHolder.showDeviceSignalStrength(String.format(Locale.getDefault(), "%.1f ", item.getRssi()));
  }

  @Override
  public int onAdapterRequestItemCount() {
    return mPeripherals.size();
  }
}
