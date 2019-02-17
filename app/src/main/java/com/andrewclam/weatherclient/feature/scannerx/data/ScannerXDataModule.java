package com.andrewclam.weatherclient.feature.scannerx.data;

import com.andrewclam.weatherclient.feature.scannerx.data.event.ScannerXEventDataSourceModule;
import com.andrewclam.weatherclient.feature.scannerx.data.result.ScannerXResultDataSourceModule;

import dagger.Module;

/**
 * Dagger module for the scanner data feature
 */
@Module(includes = {ScannerXEventDataSourceModule.class, ScannerXResultDataSourceModule.class})
public abstract class ScannerXDataModule {
}
