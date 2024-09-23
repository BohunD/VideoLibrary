package com.apps.videolibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HitsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hitsDao(): HitsDao
}