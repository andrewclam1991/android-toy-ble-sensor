package com.andrewclam.weatherclient.feature.scannerx.authx.data;

import com.andrewclam.weatherclient.feature.scannerx.authx.model.AuthX;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;

class AuthXDataSourceImpl implements AuthXDataSource {

  private final BehaviorProcessor<Boolean> mHasBluetoothLowEnergyStream;
  private final BehaviorProcessor<Boolean> mHasBluetoothAdapterStream;
  private final BehaviorProcessor<Boolean> mHasBluetoothPermissionStream;

  @Inject
  AuthXDataSourceImpl(){
    mHasBluetoothLowEnergyStream = BehaviorProcessor.create();
    mHasBluetoothAdapterStream = BehaviorProcessor.create();
    mHasBluetoothPermissionStream = BehaviorProcessor.create();
  }

  @Override
  public void setHasBluetoothLowEnergy(boolean hasBluetoothLowEnergy) {
    mHasBluetoothLowEnergyStream.onNext(hasBluetoothLowEnergy);
  }

  @Override
  public void setHasBluetoothAdapter(boolean hasBluetoothAdapter) {
    mHasBluetoothAdapterStream.onNext(hasBluetoothAdapter);
  }

  @Override
  public void setHasBluetoothPermission(boolean hasBluetoothPermission) {
    mHasBluetoothPermissionStream.onNext(hasBluetoothPermission);
  }

  @Override
  public Flowable<Boolean> getHasBluetoothLowEnergy() {
    return mHasBluetoothLowEnergyStream;
  }

  @Override
  public Flowable<Boolean> getHasBluetoothAdapter() {
    return mHasBluetoothAdapterStream;
  }

  @Override
  public Flowable<Boolean> getHasBluetoothPermission() {
    return mHasBluetoothPermissionStream;
  }

  @Override
  public Flowable<AuthX> getAuthX() {
    return Flowable.combineLatest(
        getHasBluetoothLowEnergy(),
        getHasBluetoothAdapter(),
        getHasBluetoothPermission(),
        AuthX::new); // Note: constructor signature could be confusing
  }
}
