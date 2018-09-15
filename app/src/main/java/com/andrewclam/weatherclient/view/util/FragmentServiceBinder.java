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
 * FragmentServiceBinder.java
 *
 */

package com.andrewclam.weatherclient.view.util;

import android.app.Service;
import android.content.ServiceConnection;
import android.support.annotation.NonNull;

/**
 * Fragment interaction listener that delegates bind/unbind service request to
 * its attached parent.
 * <p>
 * Note: Simple pattern that instead of checking and using getActivity()
 * uses standard Android Fragment -> Activity listener based communication
 */
public interface FragmentServiceBinder {
  /**
   * Delegates bind service request
   *
   * @param serviceClass      the class that is asked to be bound
   * @param serviceConnection connection callback that defines connection behaviors.
   * @param flags             bind service flag
   * @param <S>               Type of {@link Service}
   */
  <S extends Service> void onRequestBindService(@NonNull Class<S> serviceClass,
                                                @NonNull ServiceConnection serviceConnection,
                                                int flags);

  /**
   * Delegates unbind service request
   *
   * @param serviceConnection connection callback that defines connection behaviors.
   */
  void onRequestUnbindService(@NonNull ServiceConnection serviceConnection);
}
