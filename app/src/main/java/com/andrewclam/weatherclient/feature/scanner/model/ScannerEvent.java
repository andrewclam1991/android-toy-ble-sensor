package com.andrewclam.weatherclient.feature.scanner.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static com.andrewclam.weatherclient.feature.scanner.model.ScannerEvent.START_SCAN;
import static com.andrewclam.weatherclient.feature.scanner.model.ScannerEvent.STOP_SCAN;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@StringDef({
    START_SCAN,
    STOP_SCAN
})
public @interface ScannerEvent {
  String START_SCAN = "start_scan";
  String STOP_SCAN = "stop_scan";
}
