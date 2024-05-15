package com.shaya.cacheImage.data

import com.shaya.cacheImage.database.CacheDao
import com.shaya.cacheImage.database.mappers.asEntity
import com.shaya.cacheImage.database.mappers.toExternalModel
import com.shaya.cacheImage.model.CacheModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CacheRepositoryImpl(
    private val dao: CacheDao
) : CacheRepository {

    override suspend fun getAll(): Flow<List<CacheModel>> =
        dao.getAll()
            .map { it.map { it.toExternalModel() } }

    override suspend fun get(url: String): Flow<CacheModel?> =
        dao.get(url).map { it?.toExternalModel() }

    override suspend fun save(cacheModel: CacheModel) =
        dao.save(cacheModel.asEntity())
}