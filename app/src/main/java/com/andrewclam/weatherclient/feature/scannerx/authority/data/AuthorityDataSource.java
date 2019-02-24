package com.andrewclam.weatherclient.feature.scannerx.authority.data;

import com.andrewclam.weatherclient.feature.scannerx.authority.model.AuthorityEvent;
import com.andrewclam.weatherclient.feature.scannerx.authority.model.AuthorityResult;

import androidx.annotation.NonNull;
import io.reactivex.Flowable;

public interface AuthorityDataSource {

  /**
   * Gets the authorization (go-go or no-go signal)
   *
   * @return a {@link Flowable} updates of {@link AuthorityResult}
   */
  @NonNull
  Flowable<AuthorityResult> getResult();

  @NonNull
  Flowable<AuthorityEvent> getEvent();

  /**
   * Sets the {@link AuthorityResult}
   */
  void put(AuthorityResult result);

  /**
   * Sets the {@link AuthorityEvent}
   * @param event a {@link AuthorityEvent}
   */
  void put(AuthorityEvent event);
}
