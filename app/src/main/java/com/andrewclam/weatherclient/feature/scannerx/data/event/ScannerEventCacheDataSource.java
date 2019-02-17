package com.andrewclam.weatherclient.feature.scannerx.data.event;

import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.schedulers.Schedulers;

/**
 * In memory implementation of a {@link ScannerEventDataSource}
 */
class ScannerEventCacheDataSource implements ScannerEventDataSource {

  @NonNull
  private final BehaviorProcessor<String> mEventSource;

  @Inject
  ScannerEventCacheDataSource() {
    mEventSource = BehaviorProcessor.create();
  }

  @Override
  public void put(@NonNull @ScannerXEvent String model) {
    mEventSource.onNext(model);
  }

  @Override
  public Flowable<String> get() {
    return mEventSource.throttleLast(100, TimeUnit.MILLISECONDS)
        .subscribeOn(AndroidSchedulers.mainThread());
  }
}
