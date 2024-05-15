package com.example.cacheimage.di

import com.example.cacheimage.data.local.ImageDatabase
import com.example.cacheimage.data.remote.client.MainServices
import com.example.cacheimage.data.repository.ImageRepositoryImpl
import com.example.cacheimage.domain.repository.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun provideImageRepository(
        mainServices: MainServices,
        imageDb: ImageDatabase
    ): ImageRepository {
        return ImageRepositoryImpl(mainServices, imageDb)
    }

}
