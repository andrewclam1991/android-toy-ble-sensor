package com.andrewclam.weatherclient.feature.scannerx.authx.model;

/**
 * List all available commands for {@link AuthXResult}
 */
public enum AuthXCommand {
  IS_BLUETOOTH_LE_AVAILABLE,
  IS_BLUETOOTH_ADAPTER_AVAILABLE,
  IS_BLUETOOTH_PERMISSION_GRANTED,
  REQUEST_BLUETOOTH_PERMISSION,
  REQUEST_BLUETOOTH_ADAPTER
}
