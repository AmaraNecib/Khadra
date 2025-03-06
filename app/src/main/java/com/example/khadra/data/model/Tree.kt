package com.example.khadra.data.model

import java.util.Date

data class Tree(
    val id: String,
    val name: String,
    val type: String,
    val status: String,
    val coordinates: Pair<Double, Double>,
    val urlImage: String,
    val lastIrrigationAction: Date,
    val createdAt: Date,
    val updatedAt: Date
)
