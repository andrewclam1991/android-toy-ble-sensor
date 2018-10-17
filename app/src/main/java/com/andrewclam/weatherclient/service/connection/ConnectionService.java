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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.andrewclam.weatherclient.model.Peripheral;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.BehaviorProcessor;
import timber.log.Timber;

/**
 * Framework service implementation of {@link ConnectionContract.Service}
 */
public class ConnectionService extends DaggerService implements ConnectionContract.Service {

  @NonNull
  private final ServiceBinder mBinder;

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  @Nullable
  private ConnectionContract.View mView;

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
    mView = null;
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
    mView = null;
    mCompositeDisposable.clear();
    stopForeground(true);
    stopSelf();
  }

  @NonNull
  @Override
  public Flowable<ConnectionContract.ConnectionState> connect(Peripheral peripheral) {
    if (mView != null) {
      Disposable disposable = mView.checkSettingsSatisfied()
          .andThen(mView.checkBluetoothPermissions())
          .doOnComplete(() -> Timber.w("Has settings and permissions"))
          .doOnEvent(event -> {

          })
          .doOnError(Timber::e)
          .subscribe();
      mCompositeDisposable.add(disposable);
    } else {
      Timber.d("Unable to check for settings and permissions");
    }
    return mStatePublisher;
  }

  @NonNull
  @Override
  public Flowable<ConnectionContract.ConnectionState> reconnect(Peripheral peripheral) {
    return mStatePublisher;
  }

  @NonNull
  @Override
  public Flowable<ConnectionContract.ConnectionState> disconnect(Peripheral peripheral) {
    return mStatePublisher;
  }

  @Override
  public void postNotification() {
    ConnectionNotification.build(this);
  }

  @Override
  public void postNotificationUpdate() {

  }


  final class ServiceBinder extends Binder {
    ConnectionContract.Service getService(@NonNull ConnectionContract.View view) {
      mView = view;
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
