package com.andrewclam.weatherclient.feature.scannerx.view;

import com.andrewclam.weatherclient.feature.scannerx.data.result.ScannerXResultDataSource;
import com.andrewclam.weatherclient.feature.scannerx.model.ScannerXResult;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

class ScannerXPresenter implements ScannerXContract.Presenter {

  private final ScannerXResultDataSource mDataSource;

  @Inject
  ScannerXPresenter(@NonNull ScannerXResultDataSource dataSource){
    mDataSource = dataSource;
  }

  @Override
  public Flowable<ScannerXResult> getModel() {
    return mDataSource.get();
  }
}
