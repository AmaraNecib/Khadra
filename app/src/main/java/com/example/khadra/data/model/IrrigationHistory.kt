package com.example.khadra.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class IrrigationHistory(
    val id: String = "",
    @SerialName("tree_id")
    val treeId: String,
    @Serializable(with = DateSerializer::class)
    @SerialName("irrigation_date")
    val irrigationDate: Date,
    val notes: String? = null,
    @Serializable(with = DateSerializer::class)
    @SerialName("created_at")
    val createdAt: Date = Date(),
    @Serializable(with = DateSerializer::class)
    @SerialName("updated_at")
    val updatedAt: Date = Date()
)
