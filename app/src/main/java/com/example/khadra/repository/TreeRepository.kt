package com.example.khadra.repository

import com.example.khadra.model.Tree
import java.util.Date

object TreeRepository {
    fun getTrees(): List<Tree> {
        return listOf(
            Tree(
                id = "1",
                name = "شجرة الزيتون الاخظر",
                type = "Fruit",
                status = "Healthy",
                coordinates = Pair(37.7749, -122.4194),
                urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "2",
                name = "شجرة التفاح ",
                type = "Ornamental",
                status = "Moderate",
                coordinates = Pair(34.0522, -118.2437),
                urlImage = "https://images.pexels.com/photos/1080400/pexels-photo-1080400.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "3",
                name = "شجرة البرتقال",
                type = "Evergreen",
                status = "Low",
                coordinates = Pair(40.7128, -74.0060),
                urlImage = "https://images.pexels.com/photos/53435/tree-oak-landscape-view-53435.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "4",
                name = "شجرة التفاح ",
                type = "Ornamental",
                status = "Critical",
                coordinates = Pair(51.5074, -0.1278),
                urlImage = "https://images.pexels.com/photos/2360670/pexels-photo-2360670.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "5",
                name = "شجرة البرتقال",
                type = "Fruit",
                status = "Low",
                coordinates = Pair(48.8566, 2.3522),
                urlImage = "https://images.pexels.com/photos/1067333/pexels-photo-1067333.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),

        )
    }
}
