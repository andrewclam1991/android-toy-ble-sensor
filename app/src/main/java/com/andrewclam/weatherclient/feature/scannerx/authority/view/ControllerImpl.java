package com.andrewclam.weatherclient.feature.scannerx.authority.view;

import com.andrewclam.weatherclient.feature.scannerx.authority.data.AuthorityDataSource;
import com.andrewclam.weatherclient.feature.scannerx.authority.model.AuthorityEvent;
import com.andrewclam.weatherclient.feature.scannerx.authority.model.AuthorityResult;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.Flowable;

class ControllerImpl implements AuthorityContract.Controller {

  private AuthorityDataSource mDataSource;

  @Inject
  ControllerImpl(@NonNull AuthorityDataSource dataSource) {
    mDataSource = dataSource;
  }

  @Override
  public Flowable<AuthorityEvent> getEvent() {
    return mDataSource.getEvent();
  }

  @Override
  public void setHasBleFeature(boolean hasBleFeature) {
    mDataSource.put(AuthorityResult.HAS_BLE_FEATURE.is(hasBleFeature));
  }

  @Override
  public void setHasBtAdapter(boolean hasBtAdapter) {
    mDataSource.put(AuthorityResult.HAS_BT_ADAPTER_ON.is(hasBtAdapter));
  }

  @Override
  public void setHasPermissions(boolean hasPermissions) {
    mDataSource.put(AuthorityResult.HAS_BT_PERMISSION.is(hasPermissions));
  }
}
