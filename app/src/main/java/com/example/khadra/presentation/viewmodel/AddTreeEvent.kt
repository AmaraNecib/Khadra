package com.example.khadra.presentation.viewmodel

sealed class AddTreeEvent {
    data class EnterName(val name: String) : AddTreeEvent()
    data class EnterType(val type: String) : AddTreeEvent()
    data class EnterStatus(val status: String) : AddTreeEvent()
    data class EnterLatitude(val latitude: String) : AddTreeEvent()
    data class EnterLongitude(val longitude: String) : AddTreeEvent()
    data class EnterImageUrl(val imageUrl: String) : AddTreeEvent()
    object Submit : AddTreeEvent()
}
