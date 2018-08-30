package com.andrewclam.weatherclient.view.scanner;


import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.di.ActivityScoped;
import com.andrewclam.weatherclient.service.scanner.ScannerContract;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
@ActivityScoped
public class ScannerFragment extends DaggerFragment implements ScannerViewContract.View, ScannerContract.View {

  @Inject
  ScannerViewContract.Presenter mPresenter;

  @Nullable
  private ScannerContract.View mParent;

  @BindView(R.id.scanner_start_scan_btn)
  Button mButtonStartScanner;

  public ScannerFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ScannerContract.View) {
      mParent = (ScannerContract.View) context;
    } else {
      throw new IllegalArgumentException(context.getClass().getSimpleName() +
          " must implement ScannerViewContract.View");
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    mPresenter.addView(this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_scanner, container, false);
    ButterKnife.bind(this, rootView);
    mButtonStartScanner.setOnClickListener(view -> mPresenter.loadScannerState());
    return rootView;
  }

  @Override
  public void onPause() {
    super.onPause();
    mPresenter.dropView(this);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mParent = null;
  }

  @Override
  public void showScannerInProgress(boolean isVisible) {
    Toast.makeText(getContext(), isVisible ? "scanning" : "done", Toast.LENGTH_LONG).show();
  }

  @Override
  public boolean isActive() {
    return isAdded();
  }

  @Override
  public void checkBluetoothAdapterSettings() {
    if (mParent != null) {
      mParent.checkBluetoothAdapterSettings();
    }
  }

  @Override
  public void checkBluetoothPermissions() {
    if (mParent != null) {
      mParent.checkBluetoothPermissions();
    }
  }

  @NonNull
  @Override
  public BluetoothAdapter getBluetoothAdapter() {
    if (mParent != null) {
      return mParent.getBluetoothAdapter();
    } else {
      throw new RuntimeException("Unable to get bluetooth adapter");
    }
  }
}
