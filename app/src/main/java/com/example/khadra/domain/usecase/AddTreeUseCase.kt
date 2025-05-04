package com.example.khadra.domain.usecase

import android.net.Uri
import com.example.khadra.data.model.Tree
import com.example.khadra.data.repository.TreeRepository
import javax.inject.Inject

class AddTreeUseCase @Inject constructor(private val treeRepository: TreeRepository) {

    suspend operator fun invoke(tree: Tree, imageUri: Uri?): Tree {
        return treeRepository.addTree(tree, imageUri)
    }
}