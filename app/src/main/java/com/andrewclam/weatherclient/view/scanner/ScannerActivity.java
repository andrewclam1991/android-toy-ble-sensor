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

package com.andrewclam.weatherclient.view.scanner;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.view.util.ActivityUtils;
import com.andrewclam.weatherclient.view.util.FragmentServiceBinder;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragment_container);

    if (fragment == null) {
      fragment = mScannerFragmentProvider.get();
      ActivityUtils.addFragmentToActivity(fm, fragment, R.id.fragment_container);
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
