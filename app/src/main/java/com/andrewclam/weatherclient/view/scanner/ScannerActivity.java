package com.andrewclam.weatherclient.view.scanner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.service.scanner.ScannerContract;
import com.andrewclam.weatherclient.service.scanner.ScannerContract.Service;
import com.andrewclam.weatherclient.service.scanner.ScannerService;
import com.andrewclam.weatherclient.view.util.ActivityUtils;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

/**
 * Framework {@link DaggerAppCompatActivity} that acts as the ViewHandler
 * <p>
 * Responsibilities:
 * - commands {@link ScannerContract.Service} to do background work.
 */
public class ScannerActivity extends DaggerAppCompatActivity implements ScannerViewContract.Handler {

  @Nonnull
  private final ServiceConnection mScannerServiceConnection = getServiceConnection();

  @Nullable
  private Service mScannerService;

  private boolean mScannerServiceBound = false;

  @Inject
  Lazy<ScannerFragment> mScannerFragmentProvider;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scanner);
    loadViewScannerFragment();
  }

  @Override
  protected void onStart() {
    super.onStart();
    Timber.d("Authority View started, call to bind View to ScannerService.");
    Intent service = new Intent(this, ScannerService.class);
    bindService(service, mScannerServiceConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onStop() {
    super.onStop();
    Timber.d("Authority View stopped, call to unbind View from ScannerService.");
    if (mScannerService != null && mScannerServiceBound) {
      mScannerService.dropAuthority();
      unbindService(mScannerServiceConnection);
      mScannerServiceBound = false;
    }
  }

  /**
   * Internal
   * Loads the View class
   */
  private void loadViewScannerFragment() {
    ScannerFragment fragment = (ScannerFragment) getSupportFragmentManager()
        .findFragmentById(R.id.fragment_container);

    if (fragment == null) {
      // Create the fragment
      fragment = mScannerFragmentProvider.get();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), fragment, R.id.fragment_container);
    }
  }

  /**
   * Internal
   * Defines behaviors on service connection events
   *
   * @return an instance of {@link ServiceConnection}
   */
  @Nonnull
  private ServiceConnection getServiceConnection() {
    return new ServiceConnection() {
      @Override
      public void onServiceConnected(ComponentName name, IBinder service) {
        ScannerService.ServiceBinder binder = (ScannerService.ServiceBinder) service;
        mScannerService = binder.getService();
        mScannerServiceBound = true;
        Timber.d("View connected and bound to ScannerService.");
      }

      @Override
      public void onServiceDisconnected(ComponentName name) {
        mScannerServiceBound = false;
        Timber.d("View disconnected and unbound from ScannerService.");
      }
    };
  }

  @Override
  public void onUserStartScan() {
    Timber.d("Received user start scan command.");
    if (mScannerService != null && mScannerServiceBound) {
      Timber.d("Delegate start scan work to ScannerService.");
      mScannerService.startService();
      mScannerService.startScan();
    } else {
      Timber.w("Service disconnected, unable to handle start scan command.");
    }
  }

  @Override
  public void onUserStopScan() {
    Timber.d("Received user stop scan command.");
    if (mScannerService != null && mScannerServiceBound) {
      Timber.d("Delegate stop scan work to ScannerService.");
      mScannerService.stopService();
    } else {
      Timber.w("Service disconnected, unable to handle stop scan command.");
    }
  }
}
