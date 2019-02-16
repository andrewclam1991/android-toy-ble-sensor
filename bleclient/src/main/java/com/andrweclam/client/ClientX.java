package com.andrweclam.client;

import com.andrweclam.bluetoothstate.service.ServiceContext;

import javax.inject.Inject;

public class ClientX {
  @Inject
  ServiceContext mServiceContext;

  public ClientX(){
    mServiceContext.discover();
    mServiceContext.connect("");
    mServiceContext.disconnect();
  }
}
