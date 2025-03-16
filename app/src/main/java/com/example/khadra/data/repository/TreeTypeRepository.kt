package com.example.khadra.data.repository

import com.example.khadra.data.model.TreeType
import kotlinx.coroutines.flow.Flow

interface TreeTypeRepository {

    fun getTreeTypes(): Flow<List<TreeType>>
    suspend fun addTreeType(treeType: TreeType)

}
