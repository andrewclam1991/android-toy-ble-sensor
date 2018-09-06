package com.andrewclam.weatherclient.view.scanner;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.di.ActivityScoped;
import com.andrewclam.weatherclient.model.Peripheral;
import com.andrewclam.weatherclient.service.scanner.ScannerContract;
import com.andrewclam.weatherclient.service.scanner.ScannerService;
import com.andrewclam.weatherclient.view.util.FragmentServiceBinder;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * View class responsible for
 * - commands {@link ScannerContract.Service} to do background work.
 * - shows user a list of nearby {@link Peripheral}s.
 * - let user choose a {@link Peripheral} to pair.
 *
 * Authority class responsible for
 * - request permission and setting from users
 * - handle on permission and setting changed
 */
@ActivityScoped
public class ScannerFragment extends DaggerFragment implements ScannerViewContract.View, ScannerContract.Authority {

  // Request Codes
  private static final int REQUEST_CODE_ENABLE_BLUETOOTH_ADAPTER = 1001;
  private static final int REQUEST_CODE_BLUETOOTH_PERMISSIONS = 1002;

  @Nonnull
  private final ServiceConnection mScannerServiceConnection = getServiceConnection();

  @Nullable
  private ScannerContract.Service mScannerService;

  private boolean mScannerServiceBound = false;

  @Nullable
  private FragmentServiceBinder mServiceBinder;

  @Inject
  ScannerViewContract.Presenter mPresenter;

  @BindView(R.id.scanner_start_scan_btn)
  Button mButtonStartScanner;

  @BindView(R.id.scanner_stop_scan_btn)
  Button mButtonStopScanner;

  @Inject
  public ScannerFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof FragmentServiceBinder) {
      mServiceBinder = (FragmentServiceBinder) context;
    } else {
      throw new IllegalArgumentException(context.getClass().getSimpleName() +
          " must implement FragmentServiceBinder");
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    mPresenter.addView(this);
    mPresenter.loadScannerState();
    mPresenter.loadPeripherals();
    if (mServiceBinder != null) {
      mServiceBinder.onRequestBindService(ScannerService.class, mScannerServiceConnection, Context.BIND_AUTO_CREATE);
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_scanner, container, false);
    ButterKnife.bind(this, rootView);
    mButtonStartScanner.setOnClickListener(v -> onUserStartScan());
    mButtonStopScanner.setOnClickListener(v -> onUserStopScan());
    return rootView;
  }

  @Override
  public void onPause() {
    super.onPause();
    mPresenter.dropView(this);
    if (mServiceBinder != null && mScannerService != null && mScannerServiceBound) {
      mScannerService.dropAuthority();
      mServiceBinder.onRequestUnbindService(mScannerServiceConnection);
      mScannerServiceBound = false;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mServiceBinder = null;
  }

  @Override
  public void showScannerInProgress(boolean isVisible) {
    Toast.makeText(getActivity(), isVisible ? "scanning" : "done", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onUserStartScan() {
    Timber.d("Received user start scan command.");
    if (mScannerService != null && mScannerServiceBound) {
      Timber.d("Delegate start scan work to ScannerService.");
      mScannerService.startService();
      mScannerService.startScan();
    } else if (mServiceBinder != null) {
      Timber.e("Service disconnected, unable to handle start scan command.");
    }
  }

  @Override
  public void onUserStopScan() {
    Timber.d("Received user stop scan command.");
    if (mScannerService != null && mScannerServiceBound) {
      Timber.d("Delegate stop scan work to ScannerService.");
      mScannerService.stopService();
    } else {
      Timber.e("Service disconnected, unable to handle stop scan command.");
    }
  }

  @Override
  public boolean isActive() {
    return isAdded();
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
        mScannerService.setAuthority(ScannerFragment.this);
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
  public void requestEnableBluetoothAdapter() {
    Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    startActivityForResult(bluetoothIntent, REQUEST_CODE_ENABLE_BLUETOOTH_ADAPTER);
  }

  @Override
  public void requestBluetoothPermissions() {
    FragmentActivity activity = getActivity();
    if (activity != null) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
          Manifest.permission.ACCESS_COARSE_LOCATION)) {
        Timber.d("should show request permission rationale.");
        // TODO implement permission request screens #9
      } else {
        // No explanation needed; request the permission
        Timber.d("call to request permissions.");
        ActivityCompat.requestPermissions(activity,
            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
            REQUEST_CODE_BLUETOOTH_PERMISSIONS);
      }
    } else {
      Timber.e("Unable to request permission, parent not available");
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    switch (requestCode) {
      case REQUEST_CODE_ENABLE_BLUETOOTH_ADAPTER:
        if (resultCode == Activity.RESULT_OK) {
          Timber.d("User turned on bluetooth adapter");
        } else {
          Timber.w("User deny turning on bluetooth adapter, request again.");
          requestEnableBluetoothAdapter();
        }
        break;
      default:
        Timber.w("Invalid request code.");
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode) {
      case REQUEST_CODE_BLUETOOTH_PERMISSIONS:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          Timber.d("User gave bluetooth permissions");
        } else {
          Timber.w("User denied permissions, request again.");
          requestBluetoothPermissions();
        }
        break;
      default:
        Timber.w("Invalid request code.");
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
