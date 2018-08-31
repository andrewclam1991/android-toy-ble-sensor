package com.andrewclam.weatherclient.view.scanner;

import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.data.source.Repo;
import com.andrewclam.weatherclient.data.source.peripheral.PeripheralsDataSource;
import com.andrewclam.weatherclient.data.state.StateSource;
import com.andrewclam.weatherclient.di.ActivityScoped;
import com.andrewclam.weatherclient.model.Peripheral;
import com.andrewclam.weatherclient.model.ScannerState;
import com.andrewclam.weatherclient.scheduler.BaseSchedulerProvider;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Presenter class responsible for listening to the data layer
 */
@ActivityScoped
class ScannerPresenter implements ScannerViewContract.Presenter {

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

  @Inject
  ScannerPresenter(@Nonnull @Repo StateSource<ScannerState> stateRepository,
                   @Nonnull @Repo PeripheralsDataSource peripheralsRepository,
                   @Nonnull BaseSchedulerProvider schedulerProvider) {
    mStateRepository = stateRepository;
    mPeripheralsRepository = peripheralsRepository;
    mSchedulerProvider = schedulerProvider;
    mCompositeDisposable = new CompositeDisposable();
    mViews = new LinkedHashSet<>();
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
  }

  @Override
  public void loadScannerState() {
    Disposable disposable = mStateRepository.get()
        .subscribeOn(mSchedulerProvider.io())
        .observeOn(mSchedulerProvider.ui())
        .subscribe(this::handleOnGetSuccess, this::handleOnGetError);

    mCompositeDisposable.add(disposable);
  }

  @Override
  public void loadPeripherals() {
    Disposable disposable = mPeripheralsRepository.getAll()
        .subscribeOn(mSchedulerProvider.io())
        .observeOn(mSchedulerProvider.ui())
        .subscribe(this::onGetPeripheralsNext, this::onGetPeripheralsError);

    mCompositeDisposable.add(disposable);
  }

  private void handleOnGetSuccess(@Nonnull ScannerState state) {
    Timber.d("Got scanner state update");
    for (ScannerViewContract.View view : mViews) {
      if (view != null && view.isActive()) {
        view.showScannerInProgress(state.isActive());
      } else {
        Timber.w("view unavailable, ignore notify.");
      }
    }
  }

  private void handleOnGetError(@Nonnull Throwable throwable) {
    Timber.e(throwable, "Error getting the scanner state.");
  }

  private void onGetPeripheralsNext(@Nonnull List<Peripheral> peripherals) {
    Timber.d("Got peripherals updated, %s", peripherals.toString());
    // TODO implement showing a list of peripherals to rv adapter
  }

  private void onGetPeripheralsError(@Nonnull Throwable throwable) {
    Timber.e(throwable, "Error getting peripherals.");
  }

  @Override
  public void refresh() {
    mPeripheralsRepository.refresh();
  }
}
