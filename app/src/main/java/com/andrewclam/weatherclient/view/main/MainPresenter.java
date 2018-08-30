package com.andrewclam.weatherclient.view.main;

import android.support.annotation.NonNull;

import com.andrewclam.weatherclient.data.source.Repo;
import com.andrewclam.weatherclient.data.state.StateSource;
import com.andrewclam.weatherclient.model.ScannerState;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class MainPresenter implements MainContract.Presenter {

  @Nonnull
  private final StateSource<ScannerState> mStateRepository;

  @Nonnull
  private final Set<MainContract.View> mViews;

  @Inject
  MainPresenter(@Nonnull @Repo StateSource<ScannerState> stateRepository) {
    mViews = new LinkedHashSet<>();
    mStateRepository = stateRepository;
  }

  @Override
  public void addView(@NonNull MainContract.View view) {
    mViews.add(view);
  }

  @Override
  public void dropView(@NonNull MainContract.View view) {
    if (mViews.isEmpty() || !mViews.contains(view)) {
      return; // no observers
    }
    mViews.remove(view);
  }
}
