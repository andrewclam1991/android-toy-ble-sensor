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
 * ScannerNotification.java
 *
 */

package com.andrewclam.weatherclient.service.scanner;

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
final class ScannerNotification {

  private ScannerNotification() {
    // Prevent instantiations from clients
  }

  // Tracking notification constants
  static final int SCANNER_NOTIFICATION_ID = 1001;
  private static final String PACKAGE_NAME = ScannerNotification.class.getPackage().toString();
  private static final String SCANNER_NOTIFICATION_CHANNEL_ID = PACKAGE_NAME + ".scanner_channel";

  /**
   * Scanner Notification
   * Helper method to build a a simple foreground notification to notify user that the app
   * is currently scanning for nearby devices, and allows user to manipulate the scanner
   * service state
   *
   * @param context application context
   * @return a foreground notification that shows user
   */
  @NonNull
  static Notification build(@NonNull Context context) {
    NotificationManager notificationManager = (NotificationManager)
        context.getSystemService(NOTIFICATION_SERVICE);
    assert notificationManager != null;

    /* Implement NotificationChannel for Devices running Android O or later */
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

      String notificationChannelDescription = context.getString(
          R.string.scanner_notification_description);

      NotificationChannel notificationChannel = new NotificationChannel(
          SCANNER_NOTIFICATION_CHANNEL_ID,
          notificationChannelDescription,
          NotificationManager.IMPORTANCE_DEFAULT
      );

      // Configure the notification channel.
      notificationChannel.setDescription(notificationChannelDescription);
      notificationChannel.enableLights(false);
      notificationChannel.setLightColor(ContextCompat.getColor(context, R.color.colorPrimary));
      notificationChannel.enableVibration(false);
      notificationManager.createNotificationChannel(notificationChannel);
    }

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
        SCANNER_NOTIFICATION_CHANNEL_ID);

    builder.setContentTitle(context.getString(R.string.app_name))
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentText(context.getString(R.string.tracking_notification_content))
        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true);

    // If the build version is greater than JELLY_BEAN and lower than OREO,
    // set the notification's priority to PRIORITY_HIGH.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    return builder.build();
  }
}
