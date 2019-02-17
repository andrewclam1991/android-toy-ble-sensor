package com.andrewclam.weatherclient.feature.scannerx.view;

import com.andrewclam.weatherclient.feature.scannerx.data.result.ScannerResultDataSource;
import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXResult;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

class ScannerXPresenter implements ScannerXContract.Presenter {

  private final ScannerResultDataSource mDataSource;

  @Inject
  ScannerXPresenter(@NonNull ScannerResultDataSource dataSource){
    mDataSource = dataSource;
  }

  @Override
  public Flowable<ScannerXResult> getModel() {
    return mDataSource.get();
  }
}
