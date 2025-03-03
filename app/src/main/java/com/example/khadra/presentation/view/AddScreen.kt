package com.example.khadra.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.khadra.presentation.viewmodel.AddTreeViewModel
import com.example.khadra.presentation.viewmodel.AddTreeEvent
import com.example.khadra.ui.theme.KhadraGreen


@Composable
fun AddScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<AddTreeViewModel>()
    val state = viewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            OutlinedTextField(
                value = state.value.name,
                onValueChange = { viewModel.onEvent(AddTreeEvent.NameChanged(it)) },
                label = { Text(text = "اسم الشجرة") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                textStyle = TextStyle(
                    textAlign = TextAlign.Right,
                )
            )
        }

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        OutlinedTextField(
            value = state.value.type,
            onValueChange = { viewModel.onEvent(AddTreeEvent.TypeSelected(it)) },
            label = { Text("Tree Type") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
                    textStyle = TextStyle(
                    textAlign = TextAlign.Right,
        )
        )
        }

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            OutlinedTextField(
                value = state.value.status,
                onValueChange = { viewModel.onEvent(AddTreeEvent.StatusSelected(it)) },
                label = { Text("Status") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                textStyle = TextStyle(
                    textAlign = TextAlign.Right,
                )
            )
        }

        // Coordinates Input
        OutlinedTextField(
            value = "${state.value.coordinates.first}, ${state.value.coordinates.second}",
            onValueChange = {
                val parts = it.split(",")
                if (parts.size == 2) {
                    val latitude = parts[0].trim().toDoubleOrNull() ?: 0.0
                    val longitude = parts[1].trim().toDoubleOrNull() ?: 0.0
                    viewModel.onEvent(AddTreeEvent.CoordinatesChanged(latitude, longitude))
                }
            },
            label = { Text("Coordinates (Latitude, Longitude)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        // Image URL Input
        OutlinedTextField(
            value = state.value.imageUrl,
            onValueChange = { viewModel.onEvent(AddTreeEvent.ImageUrlChanged(it)) },
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            )
        )

        // Submit Button
        Button(
            onClick = { viewModel.onEvent(AddTreeEvent.Submit) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = KhadraGreen)
        ) {
            Text("Submit")
        }

        // Loading State
        if (state.value.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // Error Message
        state.value.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Success Message
        if (state.value.isSuccess) {
            Text(
                text = "Tree added successfully!",
                color = KhadraGreen,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}