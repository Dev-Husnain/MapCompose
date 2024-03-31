package com.map.mapcompose.di

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.map.mapcompose.presentation.map.data.ParkingSpotDatabase
import com.map.mapcompose.presentation.map.data.ParkingSpotRepositoryImpl
import com.map.mapcompose.presentation.map.domain.repository.ParkingSpotRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideParkingDatabase(@ApplicationContext context: Context): ParkingSpotDatabase {
        return Room.databaseBuilder(context, ParkingSpotDatabase::class.java, "parking_spots.db")
            .build()
    }

    @Singleton
    @Provides
    fun provideParkingRepository(db: ParkingSpotDatabase): ParkingSpotRepository {
        return ParkingSpotRepositoryImpl(db.dao)
    }

    @Singleton
    @Provides
    fun provideFusedLocation(@ApplicationContext appContext: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(appContext)

    @Singleton
    @Provides
    fun provideLocationManager(@ApplicationContext appContext: Context): LocationManager =
        appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Singleton
    @Provides
    fun provideGeoCoder(@ApplicationContext appContext: Context): Geocoder = Geocoder(appContext, Locale.getDefault())
}