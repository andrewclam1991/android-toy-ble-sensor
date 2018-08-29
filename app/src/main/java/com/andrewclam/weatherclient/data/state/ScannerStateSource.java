package com.andrewclam.weatherclient.data.state;

import com.andrewclam.weatherclient.model.ScannerState;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class ScannerStateSource implements StateSource<ScannerState> {

  @Nonnull
  private final PublishSubject<ScannerState> mScannerState;

  @Inject
  ScannerStateSource(){
    mScannerState = PublishSubject.create();
  }

  @Nonnull
  @Override
  public PublishSubject<ScannerState> get() {
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
