package com.example.khadra.domain.usecase

import com.example.khadra.data.repository.TreeRepository
import javax.inject.Inject

class GetTreesUseCase @Inject constructor(private val treeRepository: TreeRepository) {

    operator fun invoke() = treeRepository.getTrees()

}