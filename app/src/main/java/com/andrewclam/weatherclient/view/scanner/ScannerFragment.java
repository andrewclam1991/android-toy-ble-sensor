package com.andrewclam.weatherclient.view.scanner;


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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 * View class responsible for
 * - showing user a list of nearby peripherals
 * - delegate user interaction to ?? class
 */
@ActivityScoped
public class ScannerFragment extends DaggerFragment implements ScannerViewContract.View{

  @Inject
  ScannerViewContract.Presenter mPresenter;

  @Nullable
  private ScannerContract.Authority mParent;

  @BindView(R.id.scanner_start_scan_btn)
  Button mButtonStartScanner;

  public ScannerFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ScannerContract.Authority) {
      mParent = (ScannerContract.Authority) context;
    } else {
      throw new IllegalArgumentException(context.getClass().getSimpleName() +
          " must implement ScannerViewContract.Authority");
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
    mButtonStartScanner.setOnClickListener(view -> {
      mPresenter.refresh();
      mPresenter.loadScannerState();
      mPresenter.loadPeripherals();
    });
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
    Toast.makeText(getActivity(), isVisible ? "scanning" : "done", Toast.LENGTH_LONG).show();
  }

  @Override
  public boolean isActive() {
    return isAdded();
  }

}
