package com.shaya.cacheImage.data

import com.shaya.cacheImage.database.CacheEntity
import com.shaya.cacheImage.model.CacheModel
import kotlinx.coroutines.flow.Flow

interface CacheRepository {

    suspend fun getAll() : Flow<List<CacheModel>>

    suspend fun get(url: String): Flow<CacheModel?>

    suspend fun save(cacheModel: CacheModel)
}