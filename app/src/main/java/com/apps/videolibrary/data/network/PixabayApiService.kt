package com.apps.videolibrary.data.network

import com.apps.videolibrary.data.models.PixabayVideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "api_key"

interface PixabayApiService {
    @GET("api/videos/")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 10,
        @Query("key") apiKey: String = API_KEY
    ): PixabayVideoResponse
}
