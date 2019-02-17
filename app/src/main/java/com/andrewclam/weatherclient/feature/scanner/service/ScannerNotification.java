package com.andrewclam.weatherclient.feature.scanner.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.andrewclam.weatherclient.R;
import com.andrewclam.weatherclient.feature.scanner.model.ScannerEvent;
import com.andrewclam.weatherclient.feature.scanner.model.ServiceResultModel;

import io.reactivex.annotations.NonNull;

import static android.content.Context.NOTIFICATION_SERVICE;

public final class ScannerNotification {
  static final int SCANNER_NOTIFICATION_ID = 1001;
  private static final String SCANNER_NOTIFICATION_CHANNEL_ID = "scanner_notification_channel_id";

  private ScannerNotification() {
    // Prevents instantiation
  }

  /**
   * Factory for making a {@link Notification} with a {@link ServiceResultModel}
   *
   * @param context application context
   * @param model   a {@link ServiceResultModel}
   * @return a {@link Notification} that represents a {@link ServiceResultModel}
   */
  public static Notification build(@NonNull Context context, @NonNull ServiceResultModel model) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      setupNotificationChannel(context);
    }

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
        SCANNER_NOTIFICATION_CHANNEL_ID);

    builder.setSmallIcon(R.drawable.ic_launcher_background)
        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true);

    if (model.isInProgress()) {
      builder.setContentTitle("Scanning...");
      builder.setProgress(0, 0, true);
      builder.addAction(getAction(context, ScannerEvent.STOP_SCAN));
      if (model.isResult()) {
        builder.setContentInfo("Scanning...");
        builder.setContentTitle("Result");
        builder.setContentText(model.getDevice().getAddress());
      }
    } else if (model.isComplete()) {
      builder.setContentTitle("Idle");
      builder.addAction(getAction(context, ScannerEvent.START_SCAN));
    } else if (model.isError()) {
      builder.setContentTitle("Error");
      builder.setContentText(model.getErrorMessage());
      builder.addAction(getAction(context, ScannerEvent.START_SCAN));
    }

    return builder.build();
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private static void setupNotificationChannel(@NonNull Context context) {
    NotificationManager notificationManager = (NotificationManager)
        context.getSystemService(NOTIFICATION_SERVICE);
    assert notificationManager != null;

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

  private static NotificationCompat.Action getAction(Context context, @ScannerEvent String event) {
    switch (event) {
      case ScannerEvent.START_SCAN:
        return new NotificationCompat.Action(R.drawable.ic_launcher_background, "Start Scan",
            getPendingIntent(context, event));
      case ScannerEvent.STOP_SCAN:
        return new NotificationCompat.Action(R.drawable.ic_launcher_background, "Stop Scan",
            getPendingIntent(context, event));
      default:
        throw new UnsupportedOperationException("Unknown event " + event);
    }
  }

  // NOTE weak-ish string-based contract on how to start a service
  // that is a LOT of boiler sanity check code on the service side before an action is executed
  private static PendingIntent getPendingIntent(Context context, @ScannerEvent String event) {
    Intent intent = new Intent(context, ScannerService.class);
    intent.setAction(event);
    return PendingIntent.getService(context, ScannerService.SCANNER_SERVICE_REQUEST_CODE,
        intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

}