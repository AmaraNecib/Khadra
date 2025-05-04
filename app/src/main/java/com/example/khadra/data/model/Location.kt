package com.example.khadra.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
)