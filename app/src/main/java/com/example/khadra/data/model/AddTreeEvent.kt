package com.example.khadra.data.model

import android.net.Uri

sealed class AddTreeEvent {
    data class NameChanged(val name: String) : AddTreeEvent()
    data class LocationChanged(val location: String) : AddTreeEvent() // تغيير من Coordinates
    data class ImageSelected(val uri: Uri) : AddTreeEvent() // تغيير من ImageUrl
    data object Submit : AddTreeEvent()
}