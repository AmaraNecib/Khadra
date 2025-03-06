package com.example.khadra.presentation.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.khadra.presentation.viewmodel.AddTreeViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun AddScreen(
    modifier: Modifier = Modifier,
    viewModel: AddTreeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current


    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                // Update the state with the selected image URI
                viewModel.onEvent(AddTreeViewModel.AddTreeEvent.ImageUriChanged(uri.toString()))
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Makes it scrollable
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    if (state.imageUri.isNotEmpty()) Color(0xFF4CAF50) else Color(0xFFEEEEEE), // Green if image is selected
                    shape = RoundedCornerShape(8.dp)
                )
                .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                .clickable {
                    // Launch the image picker
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Photo",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Photo", fontSize = 18.sp, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(AddTreeViewModel.AddTreeEvent.NameChanged(it)) },
            label = { Text("Tree Name (Arabic)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = state.type,
            onValueChange = { viewModel.onEvent(AddTreeViewModel.AddTreeEvent.TypeSelected(it)) },
            label = { Text("Tree Type") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = state.status,
            onValueChange = { viewModel.onEvent(AddTreeViewModel.AddTreeEvent.StatusSelected(it)) },
            label = { Text("Status") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = "${state.coordinates.first}, ${state.coordinates.second}",
            onValueChange = {
                val parts = it.split(",")
                if (parts.size == 2) {
                    val latitude = parts[0].trim().toDoubleOrNull() ?: 0.0
                    val longitude = parts[1].trim().toDoubleOrNull() ?: 0.0
                    viewModel.onEvent(AddTreeViewModel.AddTreeEvent.CoordinatesChanged(latitude, longitude))
                }
            },
            label = { Text("Coordinates (Latitude, Longitude)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )



        // Image Picker Button





        Spacer(modifier = Modifier.height(18.dp))

        // Submit Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp) // Increased height
                .background(
                    if (state.isLoading) Color.Gray else Color(0xFF4CAF50), // Green when enabled, Gray when loading
                    shape = RoundedCornerShape(28.dp)
                )
                .clickable(enabled = !state.isLoading) {
                    viewModel.onEvent(AddTreeViewModel.AddTreeEvent.Submit)
                },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Tree",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (state.isLoading) "Adding..." else "Add Tree",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }


        if (state.error != null) {
            Text(

                text = state.error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)

            )
        }


        if (state.isSuccess) {
            Text(
                text = "Tree added successfully!",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}