package com.example.khadra.data.repository

import com.example.khadra.data.model.Tree
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.Date
import javax.inject.Inject

class TreeRepositoryImpl @Inject constructor() : TreeRepository {

    private val _trees = mutableListOf(
        Tree(
            id = "1",
            name = "Apple Tree",
            type = "Fruit",
            status = "Healthy",
            coordinates = Pair(37.7749, -122.4194),
            location = "الوادي",
            urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg",
            lastIrrigationAction = Date(),
            createdAt = Date(),
            updatedAt = Date(),
            imageUri = null // Correction : Suppression de `currentState.imageUri`
        ),
        Tree(
            id = "2",
            name = "Oak Tree",
            type = "Ornamental",
            status = "Moderate",
            coordinates = Pair(34.0522, -118.2437),
            location = "الوادي",
            urlImage = "https://images.pexels.com/photos/1080400/pexels-photo-1080400.jpeg",
            lastIrrigationAction = Date(),
            createdAt = Date(),
            updatedAt = Date(),
            imageUri = null
        ),
        Tree(
            id = "3",
            name = "Orange Tree",
            type = "Evergreen",
            status = "Low",
            coordinates = Pair(40.7128, -74.0060),
            location = "الوادي",
            urlImage = "https://images.pexels.com/photos/53435/tree-oak-landscape-view-53435.jpeg",
            lastIrrigationAction = Date(),
            createdAt = Date(),
            updatedAt = Date(),
            imageUri = null
        ),
        Tree(
            id = "4",
            name = "Maple Tree",
            type = "Ornamental",
            status = "Critical",
            coordinates = Pair(51.5074, -0.1278),
            location = "الوادي",
            urlImage = "https://images.pexels.com/photos/2360670/pexels-photo-2360670.jpeg",
            lastIrrigationAction = Date(),
            createdAt = Date(),
            updatedAt = Date(),
            imageUri = null
        ),
        Tree(
            id = "5",
            name = "Cherry Tree",
            type = "Fruit",
            status = "Low",
            coordinates = Pair(48.8566, 2.3522),
            location = "الوادي",
            urlImage = "https://images.pexels.com/photos/1067333/pexels-photo-1067333.jpeg",
            lastIrrigationAction = Date(),
            createdAt = Date(),
            updatedAt = Date(),
            imageUri = null
        )
    )

    // Utilisation de MutableSharedFlow pour diffuser les mises à jour
    private val _treeFlow = MutableSharedFlow<List<Tree>>(replay = 1)

    init {
        _treeFlow.tryEmit(_trees.toList())
    }

    override fun getTrees(): Flow<List<Tree>> = _treeFlow

    override suspend fun addTree(tree: Tree) {
        _trees.add(tree)
        _treeFlow.emit(_trees.toList())
    }
}
