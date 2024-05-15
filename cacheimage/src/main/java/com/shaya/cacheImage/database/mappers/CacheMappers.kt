package com.shaya.cacheImage.database.mappers

import com.shaya.cacheImage.database.CacheEntity
import com.shaya.cacheImage.model.CacheModel


fun CacheEntity.toExternalModel() = CacheModel(
        url = url,
        uri = uri
    )

fun CacheModel.asEntity() = CacheEntity(
        url = url.orEmpty(),
        uri = uri
    )

