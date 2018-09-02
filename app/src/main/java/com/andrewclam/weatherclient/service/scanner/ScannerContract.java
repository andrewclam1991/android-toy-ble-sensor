/*
 * Copyright 2018 Andrew Chi Heng Lam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * com.andrewclam.weatherclient.service.pairing.PairingServiceContract
 */

package com.andrewclam.weatherclient.service.scanner;

import android.bluetooth.BluetoothAdapter;

import com.andrewclam.weatherclient.model.Peripheral;
import com.andrewclam.weatherclient.service.BaseController;
import com.andrewclam.weatherclient.service.BaseService;
import com.andrewclam.weatherclient.view.BaseView;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.Nonnull;
import javax.inject.Qualifier;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

/**
 * Scanner service contract handles device discovery responsibilities
 * Stores discovered devices to the data layer
 */
public interface ScannerContract {

  // --------------------------------- Framework Layer ------------------------------------------ //

  /**
   * Framework layer
   * Reactive Authority handles requesting permission and necessary
   * settings from user.
   */
  interface Authority extends BaseView {

    void checkBluetoothAdapterSettings();

    void checkBluetoothPermissions();

  }

  /**
   * Framework layer
   * Reactive Service handles keeping components alive
   */
  interface Service extends BaseService, State {

    void setAuthority(@Nonnull Authority authority);

    void dropAuthority();

  }

  /**
   * Framework layer
   * Design limit: only controller states should own this abstraction.
   * <p>
   * Reactive producer that honors controller requests
   * to produce discovered peripherals, return
   */
  interface Producer {
    @NonNull
    Flowable<Peripheral> start();

    @NonNull
    Completable stop();
  }

  // --------------------------------- Business Logic Layer ------------------------------------- //

  /**
   * Business logic layer
   * Reactive Controller responsibilities:
   * - handles how scanner behaves depending on its internal {@link State}
   * - interfaces with data layer to store discovered devices.
   */
  interface Controller extends BaseController<Service>, State, Context {

  }

  /**
   * Defines possible behaviors in each {@link State}
   */
  interface State {
    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @interface Idle {

    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @interface Active {

    }

    /**
     * Command to start device start
     */
    void startScan();

    /**
     * Command to stop device start
     */
    void stopScan();

    /**
     * Command to cleanup resources
     */
    void cleanup();
  }

  /**
   * Defines {@link State}s owner behaviors
   */
  interface Context {
    /**
     * Handle switching context {@link ScannerContract.State}
     *
     * @param state the current {@link ScannerContract.State} to be set
     */
    void setCurrentState(@NonNull ScannerContract.State state);

    @NonNull
    ScannerContract.State getIdleState();

    @NonNull
    ScannerContract.State getActiveState();
  }

}
