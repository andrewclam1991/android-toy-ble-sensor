package com.andrewclam.weatherclient.feature.scannerx.authority.model;

public enum AuthorityResult {
  HAS_BLE_FEATURE,
  HAS_BT_ADAPTER_ON,
  HAS_BT_PERMISSION;

  private boolean mResult;

  /**
   * Sets the value of a {@link AuthorityResult}
   * @param result a boolean flag
   * @return a {@link AuthorityResult}
   */
  public AuthorityResult is(boolean result){
    mResult = result;
    return this;
  }

  /**
   * Gets the value of a {@link AuthorityResult}
   * @return a boolean flag
   */
  public boolean isAuthorized() {
    return mResult;
  }

  public boolean isDenied(){
    return !mResult;
  }
}
