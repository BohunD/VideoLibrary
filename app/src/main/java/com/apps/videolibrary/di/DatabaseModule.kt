package com.apps.videolibrary.di

import android.content.Context
import androidx.room.Room
import com.apps.videolibrary.data.db.AppDatabase
import com.apps.videolibrary.data.db.HitsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "video_library_db"
        ).build()
    }

    @Provides
    fun provideHitsDao(appDatabase: AppDatabase): HitsDao {
        return appDatabase.hitsDao()
    }
}