package com.andrewclam.weatherclient.feature.scannerx.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static com.andrewclam.weatherclient.feature.scannerx.model.ScannerXEvent.START_SCAN;
import static com.andrewclam.weatherclient.feature.scannerx.model.ScannerXEvent.STOP_SCAN;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@StringDef({
    START_SCAN,
    STOP_SCAN
})
public @interface ScannerXEvent {
  String START_SCAN = "start_scan";
  String STOP_SCAN = "stop_scan";
}
