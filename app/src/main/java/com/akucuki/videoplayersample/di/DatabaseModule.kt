package com.akucuki.videoplayersample.di

import android.content.Context
import androidx.room.Room
import com.akucuki.videoplayersample.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "video_player_sample").build()
    }

    @Singleton
    @Provides
    fun provideVideosDao(appDatabase: AppDatabase) = appDatabase.videosDao()
}