package com.apps.videolibrary.data.repository

import android.util.Log
import com.apps.videolibrary.data.db.HitsDao
import com.apps.videolibrary.data.db.HitsEntity
import com.apps.videolibrary.data.models.PixabayVideoResponse
import com.apps.videolibrary.data.network.PixabayApiService
import com.apps.videolibrary.domain.repository.VideosRepository
import javax.inject.Inject

class VideosRepositoryImpl @Inject constructor(
    private val pixabayApiService: PixabayApiService,
    private val hitsDao: HitsDao
) : VideosRepository {

    override suspend fun searchVideos(query: String, perPage: Int): PixabayVideoResponse {
        val response = pixabayApiService.searchVideos(query, perPage)
        Log.d("RESPONSE", response.toString())

        return response
    }

    override suspend fun saveHitsToDB(hits: List<HitsEntity>) {
        for(hit in hits){
            hitsDao.insertHit(hit)
        }
    }

    override suspend fun getHitsFromDB(): List<HitsEntity> {
        return hitsDao.getAllHits()
    }
}