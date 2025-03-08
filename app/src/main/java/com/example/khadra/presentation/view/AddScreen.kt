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
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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

        Spacer(modifier = Modifier.height(16.dp))

        // Image Picker Button
        Button(
            onClick = {
                // Launch the image picker
                imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pick Image")
        }


        if (state.imageUri.isNotEmpty()) {
            Text(
                text = "Selected Image: ${state.imageUri}",
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = { viewModel.onEvent(AddTreeViewModel.AddTreeEvent.Submit) },
            enabled = !state.isLoading
        ) {
            Text(if (state.isLoading) "Adding..." else "Add Tree")
        }

        // Error Message
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