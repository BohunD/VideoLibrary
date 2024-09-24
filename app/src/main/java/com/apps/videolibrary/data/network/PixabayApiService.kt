package com.apps.videolibrary.data.network

import com.apps.videolibrary.data.models.PixabayVideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "46141054-b093a64161b7c4e942c4771b0"

interface PixabayApiService {
    @GET("api/videos/")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 10,
        @Query("key") apiKey: String = API_KEY
    ): PixabayVideoResponse
}
