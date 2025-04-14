package com.example.khadra.data.model

import android.net.Uri
import java.net.URI
import java.util.Date

data class Tree(
    val id: String,
    val name: String,
    val type: String,
    val status: String,
    val coordinates: Pair<Double, Double>, val location:String,
    val urlImage: String,
    val imageUri:Uri=Uri.EMPTY,
    val lastIrrigationAction: Date,
    val createdAt: Date,
    val updatedAt: Date
)
