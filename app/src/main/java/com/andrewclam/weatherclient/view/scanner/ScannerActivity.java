package com.andrewclam.weatherclient.view.scanner;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.view.util.ActivityUtils;
import com.andrewclam.weatherclient.view.util.FragmentServiceBinder;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

/**
 * Framework {@link DaggerAppCompatActivity} that acts as the parent to child View classes
 * <p>
 * Responsibilities:
 * - binds child-views.
 * - binds service for child-views.
 */
public class ScannerActivity extends DaggerAppCompatActivity implements FragmentServiceBinder {

  @Inject
  Lazy<ScannerFragment> mScannerFragmentProvider;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scanner);
    loadViewScannerFragment();
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

  @Override
  public <S extends android.app.Service> void onRequestBindService(@NonNull Class<S> serviceClass,
                                                                   @NonNull ServiceConnection serviceConnection,
                                                                   int flags) {
    Timber.d("Request to bind this View to Service: %s", serviceClass.getSimpleName());
    Intent serviceIntent = new Intent(ScannerActivity.this, serviceClass);
    bindService(serviceIntent, serviceConnection, flags);
  }

  @Override
  public void onRequestUnbindService(@NonNull ServiceConnection serviceConnection) {
    Timber.d("Request to unbind this View from Service.");
    unbindService(serviceConnection);
  }
}
