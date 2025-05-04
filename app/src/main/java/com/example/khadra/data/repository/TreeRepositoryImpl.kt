package com.example.khadra.data.repository

import android.net.Uri
import com.example.khadra.data.model.IrrigationHistory
import com.example.khadra.data.model.Location
import com.example.khadra.data.model.Tree
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import android.util.Log
import com.example.khadra.data.source.TreeDataSource

class TreeRepositoryImpl @Inject constructor(
    private val treeDataSource: TreeDataSource
) : TreeRepository {

    override suspend fun getTrees(): List<Tree> {
        return treeDataSource.getTrees()
    }

    override suspend fun addTree(tree: Tree, imageUri: Uri?): Tree {
        try {
            Log.d("TreeRepositoryImpl", "Adding tree via TreeDataSource: $tree")
            val addedTree = treeDataSource.addTree(tree, imageUri)
            Log.i("TreeRepositoryImpl", "Tree added successfully via TreeDataSource")
            return addedTree
        } catch (e: Exception) {
            Log.e("TreeRepositoryImpl", "Error adding tree via TreeDataSource: ${e.message}", e)
            throw e
        }
    }

    override suspend fun updateTree(tree: Tree): Tree {
        return treeDataSource.updateTree(tree)
    }

    override suspend fun deleteTree(treeId: String) {
        treeDataSource.deleteTree(treeId)
    }

    override suspend fun getCurrentLocation(): Location {
        return treeDataSource.getCurrentLocation()
    }

    override suspend fun addIrrigationHistory(history: IrrigationHistory): IrrigationHistory {
        return treeDataSource.addIrrigationHistory(history)
    }

    override suspend fun getIrrigationHistory(treeId: String): List<IrrigationHistory> {
        return treeDataSource.getIrrigationHistory(treeId)
    }
}
