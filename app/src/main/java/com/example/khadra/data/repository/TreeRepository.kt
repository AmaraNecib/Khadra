package com.example.khadra.data.repository

import com.example.khadra.data.model.Tree
import kotlinx.coroutines.flow.Flow

interface TreeRepository {

    fun getTrees(): Flow<List<Tree>>

    suspend fun addTree(tree: Tree)

}
