package com.andrewclam.weatherclient.view.main;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.inject.Inject;

public class MainPresenter implements MainContract.Presenter {

  private final Set<MainContract.View> mViews;

  @Inject
  MainPresenter() {
    mViews = new LinkedHashSet<>();
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
