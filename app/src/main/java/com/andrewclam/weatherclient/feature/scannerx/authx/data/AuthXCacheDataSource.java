package com.andrewclam.weatherclient.feature.scannerx.authx.data;

import com.andrewclam.weatherclient.feature.scannerx.authx.model.AuthXResult;
import com.andrewclam.weatherclient.feature.scannerx.authx.model.AuthXCommand;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;

class AuthXCacheDataSource implements AuthXDataSource {

  private final BehaviorProcessor<AuthXCommand> mAuthXCommandStream;
  private final BehaviorProcessor<Boolean> mHasBluetoothLowEnergyStream;
  private final BehaviorProcessor<Boolean> mHasBluetoothAdapterStream;
  private final BehaviorProcessor<Boolean> mHasBluetoothPermissionStream;

  @Inject
  AuthXCacheDataSource(){
    mAuthXCommandStream = BehaviorProcessor.create();
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
  public void setAuthXCommand(AuthXCommand command) {
     mAuthXCommandStream.onNext(command);
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
  public Flowable<AuthXResult> getAuthXResult() {
    return Flowable.combineLatest(
        getHasBluetoothLowEnergy(),
        getHasBluetoothAdapter(),
        getHasBluetoothPermission(),
        AuthXResult::new); // Note: constructor signature could be confusing
  }

  @Override
  public Flowable<AuthXCommand> getAuthXCommand() {
    return mAuthXCommandStream;
  }
}
