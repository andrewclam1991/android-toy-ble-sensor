package com.andrweclam.bluetoothstate.notification;

import android.os.Bundle;

public enum NotificationType {
  MISSING_NETWORK_STATE,
  MISSING_BLUETOOTH_ADAPTER_STATE,
  MISSING_LOCATION_ADAPTER_STATE,
  MISSING_LOCATION_PERMISSION_STATE;

  private Bundle mArgs;

  public void setArgs(Bundle bundle){
    mArgs = bundle;
  }

  public Bundle getArgs(){
    return mArgs;
  }
}
