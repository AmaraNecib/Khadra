package com.example.khadra.data.repository

import android.net.Uri
import com.example.khadra.data.model.IrrigationHistory
import com.example.khadra.data.model.Location
import com.example.khadra.data.model.Tree

interface TreeRepository {
    suspend fun getTrees(): List<Tree>
    suspend fun addTree(tree: Tree, imageUri: Uri? = null): Tree
    suspend fun updateTree(tree: Tree): Tree
    suspend fun deleteTree(treeId: String)
    suspend fun getCurrentLocation(): Location
    suspend fun addIrrigationHistory(history: IrrigationHistory): IrrigationHistory
    suspend fun getIrrigationHistory(treeId: String): List<IrrigationHistory>
}
