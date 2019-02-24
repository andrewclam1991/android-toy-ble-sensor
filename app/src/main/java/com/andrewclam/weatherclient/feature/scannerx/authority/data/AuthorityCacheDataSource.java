package com.andrewclam.weatherclient.feature.scannerx.authority.data;

import com.andrewclam.weatherclient.feature.scannerx.authority.model.AuthorityEvent;
import com.andrewclam.weatherclient.feature.scannerx.authority.model.AuthorityResult;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;

/**
 * In memory implementation of the {@link AuthorityDataSource}
 */
class AuthorityCacheDataSource implements AuthorityDataSource {


  @NonNull
  private final BehaviorProcessor<AuthorityResult> mResultStream;
  @NonNull
  private final BehaviorProcessor<AuthorityEvent> mEventStream;

  @Inject
  AuthorityCacheDataSource() {
    mResultStream = BehaviorProcessor.create();
    mEventStream = BehaviorProcessor.create();
  }

  @Override
  public void put(AuthorityResult model) {
    mResultStream.onNext(model);
  }

  @NonNull
  @Override
  public Flowable<AuthorityResult> getResult() {
    return mResultStream;
  }

  @NonNull
  @Override
  public Flowable<AuthorityEvent> getEvent() {
    return mEventStream;
  }

  @Override
  public void put(AuthorityEvent event) {
    mEventStream.onNext(event);
  }
}
