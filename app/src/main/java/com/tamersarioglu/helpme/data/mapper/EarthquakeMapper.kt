package com.tamersarioglu.helpme.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.tamersarioglu.helpme.data.remote.dto.EarthquakeDto
import com.tamersarioglu.helpme.domain.model.Earthquake
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun EarthquakeDto.toDomain(): Earthquake {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    
    return Earthquake(
        id = id,
        date = dateTime.format(dateFormatter),
        time = dateTime.format(timeFormatter),
        latitude = latitude,
        longitude = longitude,
        depth = depthKm,
        magnitude = magnitude ?: 0.0,
        location = location
    )
}