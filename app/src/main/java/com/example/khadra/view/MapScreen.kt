package com.example.khadra.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun MapScreen() {
    val initialLocation = LatLng(36.75, 3.06) // الجزائر العاصمة
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(initialLocation, 12f, 0f, 0f)
    }

    val trees = listOf(
        LatLng(36.7528, 3.0421),
        LatLng(36.7516, 3.0673),
        LatLng(36.7453, 3.0495),
        LatLng(36.7549, 3.0562),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الأشجار القريبة منك") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            cameraPositionState = cameraPositionState
        ) {
            trees.forEach { location ->
                Marker(
                    state = MarkerState(position = location),
                    title = "شجرة",
                    snippet = "شجرة مغروسة في هذا الموقع"
                )
            }
        }
    }
}
