package com.tamersarioglu.helpme.domain.model

import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class Earthquake(
    val id: Int,
    val date: String,
    val time: String,
    val latitude: Double,
    val longitude: Double,
    val depth: Double,
    val magnitude: Double,
    val location: String
)