package com.tamersarioglu.helpme.domain.usecase


import com.tamersarioglu.helpme.domain.model.Earthquake
import com.tamersarioglu.helpme.domain.repository.EarthquakeRepository
import com.tamersarioglu.helpme.presantaion.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEarthquakesUseCase @Inject constructor(
    private val repository: EarthquakeRepository
) {
    operator fun invoke(): Flow<Resource<List<Earthquake>>> {
        return repository.getEarthquakes()
    }
}