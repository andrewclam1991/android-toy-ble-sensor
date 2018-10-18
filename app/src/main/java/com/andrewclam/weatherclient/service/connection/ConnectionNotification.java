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
 */

package com.andrewclam.weatherclient.service.connection;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.andrewclam.weatherclient.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Framework View class
 * Required to keep background framework service class alive.
 */
final class ConnectionNotification {

  private ConnectionNotification() {
    // Prevent instantiations from clients
  }

  static final int CONNECTION_NOTIFICATION_ID = 1002;
  private static final String PACKAGE_NAME = ConnectionNotification.class.getPackage().toString();
  private static final String CONNECTION_NOTIFICATION_CHANNEL_ID = PACKAGE_NAME + ".connection_channel";

  /**
   * Builds a a simple foreground {@link Notification} to notify user that the app
   * is currently connected to certain device, and allows user to interact with
   * the device connection.
   *
   * @param context application context
   * @return a foreground {@link Notification}
   */
  @NonNull
  static Notification build(@NonNull Context context) {
    NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    assert nm != null;

    /* Implement NotificationChannel for Devices running Android O or later */
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

      String notificationChannelDescription = context.getString(
          R.string.connection_notification_description);

      NotificationChannel notificationChannel = new NotificationChannel(
          CONNECTION_NOTIFICATION_CHANNEL_ID,
          notificationChannelDescription,
          NotificationManager.IMPORTANCE_DEFAULT
      );

      // Configure the notification channel.
      notificationChannel.setDescription(notificationChannelDescription);
      notificationChannel.enableLights(false);
      notificationChannel.setLightColor(ContextCompat.getColor(context, R.color.colorPrimary));
      notificationChannel.enableVibration(false);
      nm.createNotificationChannel(notificationChannel);
    }

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
        CONNECTION_NOTIFICATION_CHANNEL_ID);

    builder.setContentTitle(context.getString(R.string.app_name))
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentText(context.getString(R.string.connection_state_default_message))
        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true);

    // If the build version is greater than JELLY_BEAN and lower than OREO,
    // set the notification's priority to PRIORITY_HIGH.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      builder.setPriority(NotificationCompat.PRIORITY_MIN);
    }

    return builder.build();
  }

  static void showNotification(@NonNull Context context) {
    NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    assert nm != null;
    nm.notify(CONNECTION_NOTIFICATION_ID, build(context));
  }

  static void showUpdate(@NonNull Context context) {
    NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    assert nm != null;
    nm.notify(CONNECTION_NOTIFICATION_ID, build(context));
  }
}
