package com.apps.videolibrary.di

import com.apps.videolibrary.data.db.HitsDao
import com.apps.videolibrary.data.network.PixabayApiService
import com.apps.videolibrary.data.repository.VideosRepositoryImpl
import com.apps.videolibrary.domain.repository.VideosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideVideosRepository(
        pixabayApiService: PixabayApiService,
        hitsDao: HitsDao
    ): VideosRepository {
        return VideosRepositoryImpl(pixabayApiService, hitsDao)
    }
}
