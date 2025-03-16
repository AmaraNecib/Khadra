package com.example.khadra.data.model

import java.util.Date
import android.net.Uri

data class Tree(
    val id: String,
    val name: String,
    val type: String,
    val status: String,
    val coordinates: Pair<Double, Double>, val location: String,
    val urlImage: String,
    val lastIrrigationAction: Date,
    val createdAt: Date,
    val updatedAt: Date,
    val imageUri: Uri?

)
