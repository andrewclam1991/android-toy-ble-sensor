package com.andrewclam.weatherclient.feature.scanner.view;

import com.andrewclam.weatherclient.feature.scanner.data.result.ScannerResultDataSource;
import com.andrewclam.weatherclient.feature.scanner.model.ScannerResult;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

class ScannerPresenter implements ScannerContract.Presenter {

  private final ScannerResultDataSource mDataSource;

  @Inject
  ScannerPresenter(@NonNull ScannerResultDataSource dataSource){
    mDataSource = dataSource;
  }

  @Override
  public Flowable<ScannerResult> getModel() {
    return mDataSource.get();
  }
}
