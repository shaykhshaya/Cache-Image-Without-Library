package com.shaya.cacheImage.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CacheDao {

    @Upsert
    suspend fun save(cacheEntity: CacheEntity)

    @Query("SELECT * FROM cache_table")
    fun getAll():Flow<List<CacheEntity>>

    @Query("SELECT * FROM cache_table WHERE url = :url")
    fun get(url: String):Flow<CacheEntity?>

}