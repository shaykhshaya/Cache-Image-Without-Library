package com.shaya.cacheImage.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "cache_table"
)
data class CacheEntity(
    @PrimaryKey
    val  url: String,
    val uri: String
)
