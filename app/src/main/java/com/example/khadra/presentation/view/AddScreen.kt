package com.example.khadra.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.khadra.R
import com.example.khadra.data.model.Tree
import com.example.khadra.presentation.components.KhadraTextField
import com.example.khadra.presentation.viewmodel.TreeViewModel
import com.example.khadra.ui.theme.KhadraGreen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    viewModel: TreeViewModel,
    onNavigateBack: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var location by remember { mutableStateOf("") }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    LaunchedEffect(isSubmitting) {
        if (isSubmitting) {
            val tree = Tree(
                id = UUID.randomUUID().toString(),
                name = name,
                type = selectedType,
                status = selectedStatus,
                location = location,
                coordinatesLat = userLocation?.latitude ?: 0.0,
                coordinatesLng = userLocation?.longitude ?: 0.0,
                lastIrrigationAction = Date()
            )
            viewModel.addTree(tree, selectedImageUri)
            // Reset all fields
            name = ""
            selectedType = ""
            selectedStatus = ""
            selectedImageUri = null
            location = ""
            userLocation = null
            currentStep = 0
            isSubmitting = false
            onNavigateBack()
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri = it }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.getCurrentLocation { loc ->
                userLocation = LatLng(loc.latitude, loc.longitude)
                viewModel.getAddressFromLocation(
                    loc.latitude,
                    loc.longitude
                ) { address ->
                    if (address.isNotEmpty()) {
                        location = address
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .padding(top = 120.dp)
        ) {
            StepIndicator(currentStep = currentStep)

            when (currentStep) {
                0 -> BasicInfoStep(
                    name = name,
                    onNameChange = { name = it },
                    selectedImageUri = selectedImageUri,
                    onImageSelected = { imagePickerLauncher.launch("image/*") }
                )
                1 -> TreeTypeStep(
                    selectedType = selectedType,
                    onTypeSelected = { selectedType = it },
                    selectedStatus = selectedStatus,
                    onStatusSelected = { selectedStatus = it }
                )
                2 -> LocationStep(
                    location = location,
                    onLocationChange = { location = it },
                    userLocation = userLocation,
                    onLocationSelected = { latLng ->
                        userLocation = latLng
                        viewModel.getAddressFromLocation(
                            latLng.latitude,
                            latLng.longitude
                        ) { address ->
                            if (address.isNotEmpty()) {
                                location = address
                            }
                        }
                    },
                    onGetCurrentLocation = {
                        val permission = Manifest.permission.ACCESS_FINE_LOCATION
                        when {
                            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                                viewModel.getCurrentLocation { loc ->
                                    userLocation = LatLng(loc.latitude, loc.longitude)
                                    viewModel.getAddressFromLocation(
                                        loc.latitude,
                                        loc.longitude
                                    ) { address ->
                                        if (address.isNotEmpty()) {
                                            location = address
                                        }
                                    }
                                }
                            }
                            else -> showPermissionDialog = true
                        }
                    }
                )
                3 -> SubmitStep(
                    name = name,
                    type = selectedType,
                    status = selectedStatus,
                    location = location,
                    userLocation = userLocation,
                    selectedImageUri = selectedImageUri,
                    onSubmit = { isSubmitting = true }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth().padding(top = 0.dp), contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (currentStep > 0) {
                        Button(
                            onClick = { currentStep-- },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("السابق")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    if (currentStep < 3) {
                        Button(
                            onClick = { currentStep++ },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("التالي")
                        }
                    }
                }
            }
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("إذن الموقع") },
            text = { Text("نحتاج إلى إذن الوصول إلى موقعك لتحديد موقع الشجرة") },
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
}

@Composable
fun StepIndicator(currentStep: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(4) { step ->
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (step <= currentStep) KhadraGreen else Color.Gray.copy(alpha = 0.5f))
            )
            if (step < 3) {
                Spacer(
                    modifier = Modifier
                        .width(24.dp)
                        .height(2.dp)
                        .align(Alignment.CenterVertically)
                        .background(if (step < currentStep) KhadraGreen else Color.Gray.copy(alpha = 0.5f))
                )
            }
        }
    }
}

@Composable
fun BasicInfoStep(
    name: String,
    onNameChange: (String) -> Unit,
    selectedImageUri: Uri?,
    onImageSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "معلومات الشجرة",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        KhadraTextField(
            value = name,
            onValueChange = onNameChange,
            label = "اسم الشجرة",
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = onImageSelected),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Text("اضغط لإضافة صورة")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeTypeStep(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    selectedStatus: String,
    onStatusSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "نوع الشجرة",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        TreeTypeDropDown(
            selectedType = selectedType,
            onTypeSelected = onTypeSelected,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            "حالة الشجرة",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        TreeStatusDropDown(
            selectedStatus = selectedStatus,
            onStatusSelected = onStatusSelected,
            modifier = Modifier.fillMaxWidth(1f)
        )
    }
}

@Composable
fun LocationStep(
    location: String,
    onLocationChange: (String) -> Unit,
    userLocation: LatLng?,
    onLocationSelected: (LatLng) -> Unit,
    onGetCurrentLocation: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "موقع الشجرة",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        KhadraTextField(
            value = location,
            onValueChange = onLocationChange,
            label = "موقع الشجرة",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onGetCurrentLocation,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("استخدم موقعي الحالي")
        }

        if (userLocation != null) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .weight(1f, fill = false)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
                    },
                    onMapClick = onLocationSelected
                ) {
                    Marker(
                        state = MarkerState(position = userLocation),
                        title = "موقع الشجرة", draggable = true,
                    )
                }
            }
        }
    }
}

@Composable
fun SubmitStep(
    name: String,
    type: String,
    status: String,
    location: String,
    userLocation: LatLng?,
    selectedImageUri: Uri?,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "مراجعة المعلومات",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Review Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ReviewItem("الاسم", name)
                ReviewItem("النوع", type)
                ReviewItem("الحالة", status)
                ReviewItem("الموقع", location)
                
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Tree Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("إضافة الشجرة")
        }
    }
}

@Composable
fun ReviewItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeTypeDropDown(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Fruit", "Palm", "Vegetable", "Evergreen", "Ornamental")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedType,
            onValueChange = {},
            readOnly = true,
            label = { Text("نوع الشجرة") },
            trailingIcon = {
                Icon(
                    if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            },
            textStyle = TextStyle(textDirection = TextDirection.Rtl),
            singleLine = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onTypeSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeStatusDropDown(
    selectedStatus: String,
    onStatusSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("Healthy", "Moderate", "Low", "Critical")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedStatus,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            label = { Text("اختر حالة الشجرة") }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            statusOptions.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status) },
                    onClick = {
                        onStatusSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}
