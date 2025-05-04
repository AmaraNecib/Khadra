package com.example.khadra.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.khadra.data.model.Tree
import com.example.khadra.presentation.components.IrrigationHistoryItem
import com.example.khadra.presentation.viewmodel.TreeViewModel
import com.example.khadra.ui.theme.KhadraGreen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeDetailsScreen(
    treeId: String,
    viewModel: TreeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onEditTree: (Tree) -> Unit
) {
    val trees by viewModel.uiState.collectAsState()
    val tree = trees.trees.find { it.id == treeId }
    var showEditDialog by remember { mutableStateOf(false) }
    var editedTree by remember { mutableStateOf<Tree?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(treeId) {
        viewModel.loadTrees()
        viewModel.loadTreeIrrigationHistory(treeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(colors = TopAppBarColors(
                containerColor = KhadraGreen,
                scrolledContainerColor = Color.Unspecified,
                navigationIconContentColor = Color.Unspecified,
                titleContentColor = Color.Unspecified,
                actionIconContentColor = Color.Unspecified
            ),
                title = { Text(tree?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { editedTree = tree; showEditDialog = true }) {
                        Icon(Icons.Default.Edit, "Edit")
                    }
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Delete, "Delete")
                    }
                }
            )
        }
    ) { padding ->
        if (tree == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding)
            ) {
                tree?.let { currentTree ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            if (currentTree.imageUrl != null) {
                                AsyncImage(
                                    model = currentTree.imageUrl,
                                    contentDescription = "Tree Image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        item {
                            TreeDetailsCard(tree = currentTree)
                        }

                        item {
                            Text(
                                "موقع الشجرة",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            TreeLocationMap(
                                location = LatLng(currentTree.coordinatesLat, currentTree.coordinatesLng),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }

                        item {
                            Text(
                                "سجل الري",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(currentTree.irrigationHistory ?: emptyList()) { history ->
                            IrrigationHistoryItem(history)
                        }
                    }
                } ?: run {
                    Text(
                        "لم يتم العثور على الشجرة",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("حذف الشجرة") },
            text = { Text("هل أنت متأكد من حذف هذه الشجرة؟") },
            confirmButton = {
                Button(
                    onClick = {
                        tree?.let { 
                            viewModel.deleteTree(it.id) {
                                showDeleteDialog = false
                                onNavigateBack()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("حذف")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    if (showEditDialog && editedTree != null) {
        EditTreeDialog(
            tree = editedTree!!,
            onDismiss = { showEditDialog = false },
            onConfirm = { updatedTree ->
                viewModel.updateTree(updatedTree)
                showEditDialog = false
            }
        )
    }
}

@Composable
private fun TreeLocationMap(
    location: LatLng,
    modifier: Modifier = Modifier,
    onLocationChanged: ((LatLng) -> Unit)? = null,
    isEditable: Boolean = false
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }
    var markerPosition by remember { mutableStateOf(location) }
    
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = false),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            scrollGesturesEnabled = isEditable
        ),
        onMapClick = { latLng ->
            if (isEditable) {
                markerPosition = latLng
                onLocationChanged?.invoke(latLng)
            }
        }
    ) {
        Marker(
            state = MarkerState(position = markerPosition),
            title = "موقع الشجرة",
            draggable = isEditable,
            onInfoWindowClick = {},
            onClick = { false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTreeDialog(
    tree: Tree,
    onDismiss: () -> Unit,
    onConfirm: (Tree) -> Unit
) {
    var name by remember { mutableStateOf(tree.name) }
    var type by remember { mutableStateOf(tree.type) }
    var status by remember { mutableStateOf(tree.status) }
    var location by remember { mutableStateOf(tree.location) }
    var treeLocation by remember { mutableStateOf(LatLng(tree.coordinatesLat, tree.coordinatesLng)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("تعديل معلومات الشجرة") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("الاسم") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                TreeTypeDropDown(
                    selectedType = type,
                    onTypeSelected = { type = it },
                    modifier = Modifier.fillMaxWidth()
                )
                
                TreeStatusDropDown(
                    selectedStatus = status,
                    onStatusSelected = { status = it },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("الموقع") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "حدد موقع الشجرة على الخريطة",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                TreeLocationMap(
                    location = treeLocation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    onLocationChanged = { treeLocation = it },
                    isEditable = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(tree.copy(
                        name = name,
                        type = type,
                        status = status,
                        location = location,
                        coordinatesLat = treeLocation.latitude,
                        coordinatesLng = treeLocation.longitude,
                        updatedAt = Date()
                    ))
                }
            ) {
                Text("حفظ")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

@Composable
private fun TreeDetailsCard(tree: Tree) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailItem("النوع", tree.type)
            DetailItem("الحالة", tree.status)
            DetailItem("الموقع", tree.location)
            DetailItem("آخر ري", formatDate(tree.lastIrrigationAction))
            DetailItem("تاريخ الإضافة", formatDate(tree.createdAt))
            DetailItem("آخر تحديث", formatDate(tree.updatedAt))
            DetailItem("خط العرض", tree.coordinatesLat.toString())
            DetailItem("خط الطول", tree.coordinatesLng.toString())
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale("ar"))
    return formatter.format(date)
}
