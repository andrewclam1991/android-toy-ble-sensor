/*
 * Copyright 2018 Andrew Chi Heng Lam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andrewclam.weatherclient.service.connection;

import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.andrewclam.weatherclient.model.Peripheral;
import com.andrewclam.weatherclient.scheduler.BaseSchedulerProvider;
import com.andrweclam.bleclient.BleConnectionClient;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dagger.android.DaggerService;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.processors.BehaviorProcessor;
import timber.log.Timber;

/**
 * Framework service implementation of {@link ConnectionContract.Service}
 */
public class ConnectionService extends DaggerService implements ConnectionContract.Service {

  @Nullable
  private ConnectionContract.View mAuthority;

  @Inject
  BaseSchedulerProvider mSchedulerProvider;

  @Inject
  BleConnectionClient mBleConnectionClient;

  @NonNull
  private final ServiceBinder mBinder;

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  @NonNull
  private final BehaviorProcessor<ConnectionContract.ConnectionState> mStatePublisher;

  @Inject
  public ConnectionService() {
    // Required no arg constructor
    mBinder = new ServiceBinder();
    mCompositeDisposable = new CompositeDisposable();
    mStatePublisher = BehaviorProcessor.create();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Timber.d("Service onStartCommand() called, service started by framework.");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      Timber.d("SDK Version > Android O, post foreground notification to keep service alive.");
      startForeground(ConnectionNotification.CONNECTION_NOTIFICATION_ID,
          ConnectionNotification.build(getApplicationContext()));
    }
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mAuthority = null;
    mCompositeDisposable.clear();
  }

  @Override
  public void startService() {
    Timber.d("startService() called to start foreground service.");
    Intent intent = new Intent(getApplicationContext(), ConnectionService.class);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      startForegroundService(intent);
    } else {
      startService(intent);
    }
  }

  @Override
  public void stopService() {
    mAuthority = null;
    mCompositeDisposable.clear();
    stopForeground(true);
    stopSelf();
  }

  @NonNull
  @Override
  public Flowable<ConnectionContract.ConnectionState> connect(Peripheral peripheral) {
    // if the same peripheral is connected, ignore this request
    // else if connected with another peripheral, disconnects from current peripheral THEN connects to new peripheral
    // else connects to new peripheral

    checkRequirements()
        .subscribeOn(mSchedulerProvider.ui())
        .observeOn(mSchedulerProvider.io())
        .doOnSubscribe(mCompositeDisposable::add)
        .doOnComplete(() -> mBleConnectionClient.connect(peripheral.getBluetoothDevice()))
        // TODO compose write request flowable, zip them and


        .doOnError(Timber::e) // TODO notify user unable to connect
        .subscribe();

    return mStatePublisher;
  }

  @NonNull
  @Override
  public Flowable<ConnectionContract.ConnectionState> reconnect(Peripheral peripheral) {
    // if the current peripheral is connected, ignore this request
    // else if connected with another peripheral, ignore this request
    // else if the current peripheral is disconnected, reconnects to current peripheral

    checkRequirements()
        .subscribeOn(mSchedulerProvider.ui())
        .observeOn(mSchedulerProvider.io())
        .doOnSubscribe(mCompositeDisposable::add)
        .doOnComplete(() -> Timber.d("Do reconnection here "))
        .doOnError(Timber::e) // TODO notify user unable to reconnect
        .subscribe();

    return mStatePublisher;
  }

  @NonNull
  @Override
  public Flowable<ConnectionContract.ConnectionState> disconnect(Peripheral peripheral) {
    // if the current peripheral is connected, disconnect this peripheral
    // else if connected with another peripheral, ignore this request

    checkRequirements()
        .subscribeOn(mSchedulerProvider.ui())
        .observeOn(mSchedulerProvider.io())
        .doOnSubscribe(mCompositeDisposable::add)
        .doOnComplete(() -> Timber.d("Do disconnection here "))
        .doOnError(Timber::e) // TODO notify user unable to disconnect
        .subscribe();

    return mStatePublisher;
  }

  @Override
  public void postNotification() {
    ConnectionNotification.showUpdate(getApplicationContext());
  }

  @Override
  public void postNotificationUpdate() {
    ConnectionNotification.showUpdate(getApplicationContext());
  }

  /**
   * Note: run this functional check before any work is done
   *
   * @return a completable when complete, means requirements are satisfied.
   */
  private Completable checkRequirements() {
    if (mAuthority != null && mAuthority.isActive()) {
      return mAuthority.checkSettingsSatisfied()
          .andThen(mAuthority.checkBluetoothPermissions())
          .doOnComplete(() -> Timber.w("Has settings and permissions"));
    } else {
      return Completable.error(new Throwable("AuthorityDataSource not available to check requirements"));
    }
  }

  public final class ServiceBinder extends Binder {
    public ConnectionContract.Service getService(@NonNull ConnectionContract.View view) {
      mAuthority = view;
      return ConnectionService.this;
    }
  }

  private final class ConnectionState implements ConnectionContract.ConnectionState {

    private boolean mIsConnected;
    private boolean mIsCalibrated;

    protected ConnectionState() {
    }

    void setConnected(boolean isConnected) {
      mIsConnected = isConnected;
    }

    void setCalibrated(boolean isCalibrated) {
      mIsCalibrated = isCalibrated;
    }

    @Override
    public boolean isConnected() {
      return mIsConnected;
    }

    @Override
    public boolean isCalibrated() {
      return mIsCalibrated;
    }
  }
}
