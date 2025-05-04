package com.example.khadra.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.khadra.R
import com.example.khadra.data.model.Location
import com.example.khadra.data.model.Tree
import com.example.khadra.presentation.viewmodel.TreeViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: TreeViewModel,
    onNavigateToTreeDetails: (String) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showPermissionDialog by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(24.7136, 46.6753), // Default to Riyadh
            12f
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.getCurrentLocation { loc ->
                userLocation = LatLng(loc.latitude, loc.longitude)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    userLocation!!,
                    15f
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                viewModel.getCurrentLocation { loc ->
                    userLocation = LatLng(loc.latitude, loc.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        userLocation!!,
                        15f
                    )
                }
            }
            else -> showPermissionDialog = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = userLocation != null
            )
        ) {
            userLocation?.let { location ->
                Marker(
                    state = MarkerState(position = location),
                    title = "موقعك الحالي"
                )
            }

            uiState.trees.forEach { tree ->
                tree.coordinates?.let { coords ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(coords.first, coords.second)
                        ),
                        title = tree.name,
                        snippet = tree.type,
                        onClick = {
                            onNavigateToTreeDetails(tree.id)
                            true
                        }
                    )
                }
            }
        }

        if (showPermissionDialog) {
            AlertDialog(
                onDismissRequest = { showPermissionDialog = false },
                title = { Text("إذن الموقع") },
                text = { Text("نحتاج إلى إذن الوصول إلى موقعك لعرض الأشجار القريبة منك") },
                confirmButton = {
                    Button(onClick = {
                        showPermissionDialog = false
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }) {
                        Text("موافق")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPermissionDialog = false }) {
                        Text("لاحقاً")
                    }
                }
            )
        }

        FloatingActionButton(
            onClick = {
                if (userLocation != null) {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        userLocation!!,
                        15f
                    )
                } else {
                    showPermissionDialog = true
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_my_location),
                contentDescription = "My Location"
            )
        }
    }
}