package com.andrewclam.weatherclient.feature.scannerx.data;

import com.andrewclam.weatherclient.feature.scannerx.data.event.ScannerEventDataSourceModule;
import com.andrewclam.weatherclient.feature.scannerx.data.result.ScannerResultDataSourceModule;

import dagger.Module;

/**
 * Dagger module for the scanner data feature
 */
@Module(includes = {ScannerEventDataSourceModule.class, ScannerResultDataSourceModule.class})
public abstract class ScannerXDataModule {
}
