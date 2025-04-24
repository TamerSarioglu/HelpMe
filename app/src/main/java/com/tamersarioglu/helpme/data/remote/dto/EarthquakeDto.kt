package com.tamersarioglu.helpme.data.remote.dto

import java.time.LocalDateTime

data class EarthquakeDto(
    val id: Int,
    val dateTime: LocalDateTime,
    val latitude: Double,
    val longitude: Double,
    val depthKm: Double,
    val magnitude: Double?,
    val location: String,
    val quality: String
)