package com.andrewclam.weatherclient.view.main;

import com.andrewclam.weatherclient.view.BasePresenter;
import com.andrewclam.weatherclient.view.BaseView;

interface MainContract {
  interface View extends BaseView {

  }

  interface Presenter extends BasePresenter<View> {

  }
}
