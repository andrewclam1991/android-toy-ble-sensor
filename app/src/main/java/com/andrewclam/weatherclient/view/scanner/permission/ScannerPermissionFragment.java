package com.andrewclam.weatherclient.view.scanner.permission;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.di.ActivityScoped;
import com.andrewclam.weatherclient.model.Peripheral;
import com.andrewclam.weatherclient.service.scanner.ScannerContract;
import com.andrewclam.weatherclient.view.scanner.ScannerViewContract;
import com.andrewclam.weatherclient.view.util.FragmentServiceBinder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 * View class responsible for
 * - commands {@link ScannerContract.Service} to do background work.
 * - shows user a list of nearby {@link Peripheral}s.
 * - let user choose a {@link Peripheral} to pair.
 */
@ActivityScoped
public class ScannerPermissionFragment extends DaggerFragment {

  @Nullable
  private FragmentServiceBinder mServiceBinder;

  @Inject
  ScannerViewContract.Presenter mPresenter;

  @BindView(R.id.scanner_start_scan_btn)
  Button mButtonStartScanner;

  @BindView(R.id.scanner_stop_scan_btn)
  Button mButtonStopScanner;

  @Inject
  public ScannerPermissionFragment() {
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
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_scanner, container, false);
    ButterKnife.bind(this, rootView);
//    mButtonStartScanner.setOnClickListener(v -> onUserStartScan());
//    mButtonStopScanner.setOnClickListener(v -> onUserStopScan());
    return rootView;
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mServiceBinder = null;
  }


}
