package com.example.khadra.domain.usecase

import com.example.khadra.data.repository.TreeTypeRepository
import javax.inject.Inject

class GetTreeTypesUseCase @Inject constructor(private val treeTypeRepository: TreeTypeRepository) {

    operator fun invoke() = treeTypeRepository.getTreeTypes()
}