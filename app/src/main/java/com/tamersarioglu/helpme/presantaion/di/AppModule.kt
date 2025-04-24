package com.tamersarioglu.helpme.presantaion.di

import com.tamersarioglu.helpme.data.remote.scraper.JsoupEarthquakeService
import com.tamersarioglu.helpme.data.repositoryimpl.EarthquakeRepositoryImpl
import com.tamersarioglu.helpme.domain.repository.EarthquakeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJsoupEarthquakeService(): JsoupEarthquakeService {
        return JsoupEarthquakeService(Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideEarthquakeRepository(
        jsoupService: JsoupEarthquakeService
    ): EarthquakeRepository {
        return EarthquakeRepositoryImpl(jsoupService)
    }
}