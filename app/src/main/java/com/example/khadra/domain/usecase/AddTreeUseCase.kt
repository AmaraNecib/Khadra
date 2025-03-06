package com.example.khadra.domain.usecase

import com.example.khadra.data.model.Tree
import com.example.khadra.data.repository.TreeRepository
import javax.inject.Inject

class AddTreeUseCase @Inject constructor(private val treeRepository: TreeRepository) {

    suspend fun addTree(tree: Tree) {
        treeRepository.addTree(tree)
    }
}