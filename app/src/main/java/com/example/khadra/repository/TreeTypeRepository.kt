package com.example.khadra.repository

import com.example.khadra.model.TreeType

object TreeTypeRepository {
    fun getTreeTypes(): List<TreeType> {
        return listOf(
            TreeType(
                id = "1",
                name = "Fruit",
                description = "Trees that bear edible fruits."
            ),
            TreeType(
                id = "2",
                name = "Ornamental",
                description = "Trees grown for decorative purposes."
            ),
            TreeType(
                id = "3",
                name = "Evergreen",
                description = "Trees that retain their leaves year-round."
            )
        )
    }
}