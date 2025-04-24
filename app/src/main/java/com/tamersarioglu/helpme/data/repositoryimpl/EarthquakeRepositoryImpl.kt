package com.tamersarioglu.helpme.data.repositoryimpl

import com.tamersarioglu.helpme.data.mapper.toDomain
import com.tamersarioglu.helpme.data.remote.scraper.JsoupEarthquakeService
import com.tamersarioglu.helpme.domain.model.Earthquake
import com.tamersarioglu.helpme.domain.repository.EarthquakeRepository
import com.tamersarioglu.helpme.presantaion.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EarthquakeRepositoryImpl @Inject constructor(
    private val jsoupService: JsoupEarthquakeService
) : EarthquakeRepository {
    override fun getEarthquakes(): Flow<Resource<List<Earthquake>>>  = flow {
        try {
            val earthquakes = jsoupService.fetchEarthquakes().map { it.toDomain() }
            emit(value = Resource.Success(data = earthquakes))
        } catch (e: Exception) {
            emit(value = Resource.Error(message = e.message ?: "An unexpected error occurred"))
        }
    }
}