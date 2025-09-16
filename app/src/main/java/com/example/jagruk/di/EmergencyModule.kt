package com.example.jagruk.di

import android.content.Context
import com.example.jagruk.emergency.EmergencyAlarmManager
import com.example.jagruk.emergency.EmergencyNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EmergencyModule {

    @Provides
    @Singleton
    fun provideEmergencyAlarmManager(
        @ApplicationContext context: Context
    ): EmergencyAlarmManager {
        return EmergencyAlarmManager(context)
    }

    @Provides
    @Singleton
    fun provideEmergencyNotificationManager(
        @ApplicationContext context: Context
    ): EmergencyNotificationManager {
        return EmergencyNotificationManager(context)
    }
}