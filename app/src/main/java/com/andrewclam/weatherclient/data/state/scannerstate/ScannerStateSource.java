package com.andrewclam.weatherclient.data.state.scannerstate;

import com.andrewclam.weatherclient.data.state.StateSource;
import com.andrewclam.weatherclient.model.ScannerState;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;

@Singleton
public class ScannerStateSource implements StateSource<ScannerState> {

  @Nonnull
  private final PublishProcessor<ScannerState> mScannerState;

  @Inject
  ScannerStateSource(){
    mScannerState = PublishProcessor.create();
  }

  @Nonnull
  @Override
  public Flowable<ScannerState> get() {
    return mScannerState;
  }

  @Nonnull
  @Override
  public Completable set(@Nonnull ScannerState source) {
    return Completable.create(emitter -> {
      mScannerState.onNext(source);
      emitter.onComplete();
    });
  }
}
