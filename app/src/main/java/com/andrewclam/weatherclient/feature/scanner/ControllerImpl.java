package com.andrewclam.weatherclient.feature.scanner;

import com.andrewclam.weatherclient.feature.scanner.data.ServiceEventDataSource;
import com.andrewclam.weatherclient.feature.scanner.model.ServiceEventModel;
import com.andrewclam.weatherclient.feature.scanner.model.ServiceModel;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.processors.BehaviorProcessor;

class ControllerImpl implements ScannerContract.Controller {

  private final ServiceEventDataSource mDataSource;

  private final CompositeDisposable mCompositeDisposable;

  @Inject
  ControllerImpl(ServiceEventDataSource dataSource){
    mDataSource = dataSource;
    mCompositeDisposable = new CompositeDisposable();
  }

  @Override
  public BehaviorProcessor<ServiceEventModel> getEventSource() {
    return null;
  }

  @Override
  public Flowable<ServiceModel> getModel() {
    return null;
  }

  @Override
  public void start() {
    mCompositeDisposable.add(mDataSource.get().subscribe(event -> {
      switch (event){
        case START_SCAN:
          break;
        case STOP_SCAN:
          break;
      }
    }));
  }

  @Override
  public void stop() {
    mCompositeDisposable.clear();
  }
}
