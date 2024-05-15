package com.example.cacheimage.data.repository

import com.example.cacheimage.data.local.ImageDatabase
import com.example.cacheimage.data.mappers.toImage
import com.example.cacheimage.data.mappers.toImageEntity
import com.example.cacheimage.data.remote.client.MainServices
import com.example.cacheimage.domain.model.Image
import com.example.cacheimage.domain.repository.ImageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val mainServices: MainServices,
    private val imageDb: ImageDatabase
) : ImageRepository {

    override suspend fun getList(dispatcher: CoroutineDispatcher): Flow<Result<List<Image>>> =


        imageDb.dao.getAll().map {
            if (it.isEmpty()) {
                val images = mainServices.getRemoteImageList().body()
                val imageEntityList = images?.map { imageDto ->
                    imageDto.toImageEntity()
                }
                if (!imageEntityList.isNullOrEmpty()) {
                    imageDb.dao.insertImages(imageEntityList)
                }
            }
            val items = it.map { it.toImage() }
            Result.success(items)
        }

}

