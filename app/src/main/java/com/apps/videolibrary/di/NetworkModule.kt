package com.apps.videolibrary.di

import android.content.Context
import androidx.room.Room
import com.apps.videolibrary.data.db.AppDatabase
import com.apps.videolibrary.data.db.HitsDao
import com.apps.videolibrary.data.network.PixabayApiService
import com.apps.videolibrary.data.repository.VideosRepositoryImpl
import com.apps.videolibrary.domain.repository.VideosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pixabay.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePexelsApiService(retrofit: Retrofit): PixabayApiService {
        return retrofit.create(PixabayApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
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

    @Provides
    @Singleton
    fun provideVideosRepository(
        pixabayApiService: PixabayApiService,
        hitsDao: HitsDao
    ): VideosRepository {
        return VideosRepositoryImpl(pixabayApiService, hitsDao)
    }


}
