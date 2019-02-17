package com.andrweclam.bluetoothstate.notification;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import io.reactivex.annotations.NonNull;

public class NotificationBuilder {
  @NonNull
  private final Context mContext;

  public NotificationBuilder(@NonNull Context context) {
    mContext = context;
  }

  public Notification build(NotificationType type) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "Sample channel");
    switch (type) {
      case MISSING_NETWORK_STATE:
        builder.setContentText("Missing network state");
        break;
      case MISSING_BLUETOOTH_ADAPTER_STATE:
        builder.setContentText("Missing bluetooth adapter state");
        break;
      case MISSING_LOCATION_ADAPTER_STATE:
        builder.setContentText("Missing location adapter state");
        break;
      case MISSING_LOCATION_PERMISSION_STATE:
        builder.setContentText("Missing location permission state");
        break;
    }
    return builder.build();
  }
}
