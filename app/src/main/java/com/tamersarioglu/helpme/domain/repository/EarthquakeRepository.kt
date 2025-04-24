package com.tamersarioglu.helpme.domain.repository

import com.tamersarioglu.helpme.domain.model.Earthquake
import com.tamersarioglu.helpme.presantaion.util.Resource
import kotlinx.coroutines.flow.Flow

interface EarthquakeRepository {
    fun getEarthquakes(): Flow<Resource<List<Earthquake>>>
}