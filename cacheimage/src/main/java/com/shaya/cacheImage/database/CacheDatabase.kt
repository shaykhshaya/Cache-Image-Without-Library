package com.shaya.cacheImage.database

import androidx.room.Database
import androidx.room.RoomDatabase

const val DatabaseName = "cache_image.db"

@Database(
    entities = [CacheEntity::class],
    version = 1
)
abstract class CacheDatabase: RoomDatabase() {

    abstract val dao: CacheDao
}