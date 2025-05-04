package com.example.khadra.domain.usecase

import com.example.khadra.data.model.Tree
import com.example.khadra.data.repository.TreeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import android.util.Log
import javax.inject.Inject

class GetTreesUseCase @Inject constructor(
    private val repository: TreeRepository
) {
    suspend operator fun invoke(): Flow<List<Tree>> = flow {
        try {
            val trees = repository.getTrees()
            emit(trees)
        } catch (e: Exception) {
            Log.e("GetTreesUseCase", "Error getting trees", e)
            emit(emptyList())
        }
    }
}