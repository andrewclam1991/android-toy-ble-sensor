package com.andrewclam.weatherclient.feature.scanner;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

class ServiceImpl implements ScannerContract.Service {

  @Inject
  ScannerContract.Controller mController;

  @NonNull
  private final CompositeDisposable mCompositeDisposable;

  public ServiceImpl() {
    mCompositeDisposable = new CompositeDisposable();
  }

//  NOTE get user actions by platform intent action approach
//  public void onStartCommand(Intent intent){
//    String action = intent.getAction();
//    if (Strings.isNullOrEmpty(action)){
//      throw new IllegalArgumentException("Intent action is null or empty");
//    }
//    ServiceEventModel model = ServiceEventModel.valueOf(action);
//    switch (model){
//      case START_SCAN:
//        mController.getEventSource().onNext(ServiceEventModel.START_SCAN);
//        break;
//      case STOP_SCAN:
//        mController.getEventSource().onNext(ServiceEventModel.STOP_SCAN);
//        break;
//    }
//  }


  public void onStart() {
    mController.start();

    mCompositeDisposable.add(mController.getModel().subscribe(model -> {
      if (model.isComplete()) {
        showComplete();
      } else if (model.isInProgress()) {
        showInProgress();
      } else if (model.isError()) {
        showError(model.getErrorMessage());
      }

      if (model.isResult()) {
        model.getDevice();
      }
    }));
  }

  public void onStop() {
    // Cleanup subscription
    mController.stop();
    mCompositeDisposable.clear();
  }

  @Override
  public void showInProgress() {
    Timber.d("Is in progress...");
  }

  @Override
  public void showComplete() {
    Timber.d("Is complete.");
  }

  @Override
  public void showError(String error) {
    Timber.e("Is error. %s", error);
  }
}
