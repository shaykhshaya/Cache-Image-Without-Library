package com.example.cacheimage.domain.repository

import com.example.cacheimage.domain.model.Image
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun getList(dispatcher: CoroutineDispatcher): Flow<Result<List<Image>>>

}