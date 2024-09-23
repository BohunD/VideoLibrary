package com.apps.videolibrary.domain.repository

import com.apps.videolibrary.data.db.HitsEntity
import com.apps.videolibrary.data.models.PixabayVideoResponse

interface VideosRepository {
    suspend fun searchVideos(query: String, perPage: Int): PixabayVideoResponse

    suspend fun saveHitsToDB(hits: List<HitsEntity>)

    suspend fun getHitsFromDB(): List<HitsEntity>
}