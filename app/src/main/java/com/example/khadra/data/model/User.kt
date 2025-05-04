package com.example.khadra.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val fullName: String? = null,
    val avatarUrl: String? = null,
    val treesPlanted: Int = 0,
    val treesWatered: Int = 0
)