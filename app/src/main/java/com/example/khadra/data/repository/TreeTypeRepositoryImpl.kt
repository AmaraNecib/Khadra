package com.example.khadra.data.repository

import com.example.khadra.data.model.TreeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class TreeTypeRepositoryImpl @Inject constructor() :TreeTypeRepository{

    private val _treeTypes = mutableListOf(
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
        ),
        TreeType(
            id = "4",
            name = "Palm",
            description = "A large, single-stemmed palm with stout, straight or slightly curved trunk, rising from a swollen base surrounded by a mass of roots"
        ),
        TreeType(
            id = "4",
            name = "Vegetable",
            description = "Trees that bear edible Vegetables."
        )
    )

    // MutableSharedFlow with replay = 1 ensures new collectors get the latest list.
    private val _treeTypeFlow = MutableSharedFlow<List<TreeType>>(replay = 1)

    init {
        // Emit the initial list.
        _treeTypeFlow.tryEmit(_treeTypes.toList())
    }

    // Expose the flow as a read-only Flow<List<TreeType>>.
    override fun getTreeTypes(): Flow<List<TreeType>> = _treeTypeFlow

    // Function to add a new TreeType and update the flow.
    override suspend fun addTreeType(treeType: TreeType) {
        _treeTypes.add(treeType)
        _treeTypeFlow.emit(_treeTypes.toList())
    }
}
