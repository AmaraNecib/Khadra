package com.example.khadra.repository

import com.example.khadra.model.Tree
import java.util.Date

object TreeRepository {
    fun getTrees(): List<Tree> {
        return listOf(
            Tree(
                id = "1",
                name = "Apple Tree",
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
                name = "Oak Tree",
                type = "Ornamental",
                status = "Moderate",
                coordinates = Pair(34.0522, -118.2437),
                urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "3",
                name = "Pine Tree",
                type = "Evergreen",
                status = "Low",
                coordinates = Pair(40.7128, -74.0060),
                urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "4",
                name = "Maple Tree",
                type = "Ornamental",
                status = "Critical",
                coordinates = Pair(51.5074, -0.1278),
                urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "5",
                name = "Cherry Tree",
                type = "Fruit",
                status = "Low",
                coordinates = Pair(48.8566, 2.3522),
                urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "6",
                name = "Willow Tree",
                type = "Ornamental",
                status = "Healthy",
                coordinates = Pair(35.6895, 139.6917),
                urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "7",
                name = "Palm Tree",
                type = "Evergreen",
                status = "Moderate",
                coordinates = Pair(25.7617, -80.1918),
                urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "8",
                name = "Birch Tree",
                type = "Ornamental",
                status = "Low",
                coordinates = Pair(55.7558, 37.6173),
                urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "9",
                name = "Cedar Tree",
                type = "Evergreen",
                status = "Critical",
                coordinates = Pair(45.4215, -75.6972),
                urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "10",
                name = "Olive Tree",
                type = "Fruit",
                status = "Healthy",
                coordinates = Pair(41.9028, 12.4964),
                urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "11",
                name = "Redwood Tree",
                type = "Evergreen",
                status = "Moderate",
                coordinates = Pair(37.7749, -122.4194),
                urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            ),
            Tree(
                id = "12",
                name = "Magnolia Tree",
                type = "Ornamental",
                status = "Low",
                coordinates = Pair(34.0522, -118.2437),
                urlImage = "https://images.pexels.com/photos/1459495/pexels-photo-1459495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                lastIrrigationAction = Date(),
                createdAt = Date(),
                updatedAt = Date()
            )
        )
    }
}
