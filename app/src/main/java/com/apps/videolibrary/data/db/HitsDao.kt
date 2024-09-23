package com.apps.videolibrary.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HitsDao {
    @Query("SELECT * FROM hits")
    suspend fun getAllHits(): List<HitsEntity>

    @Query("SELECT * FROM hits WHERE apiId IN (:id)")
    suspend fun getHitById(id: Int): HitsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHit( hit: HitsEntity)

    @Delete
    fun delete(hit: HitsEntity)
}