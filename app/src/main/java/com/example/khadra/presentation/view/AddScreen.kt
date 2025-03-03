package com.example.khadra.presentation.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.khadra.R
import com.example.khadra.presentation.viewmodel.AddTreeViewModel
import com.example.khadra.presentation.viewmodel.AddTreeEvent
import com.example.khadra.ui.theme.KhadraGreen

@Composable
fun ImageUploadBox(
    imageUri: String?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri.isNullOrEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter =  painterResource(R.drawable.ic_outline_add_circle_outline_24),
                    contentDescription = "Add Image",
                    tint = Color.Black,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = "إضافة صورة",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            AsyncImage(
                model = imageUri,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
fun AddScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<AddTreeViewModel>()
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<String?>(state.value.imageUrl) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val uriString = it.toString()
            imageUri.value = uriString
            viewModel.onEvent(AddTreeEvent.ImageUrlChanged(uriString))
        }
    }

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
                textStyle = TextStyle(textAlign = TextAlign.Right)
            )
        }

        // Type Input
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
                textStyle = TextStyle(textAlign = TextAlign.Right)
            )
        }

        // Status Input
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
                textStyle = TextStyle(textAlign = TextAlign.Right)
            )
        }

        // Coordinates Input
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
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
                ),
                textStyle = TextStyle(textAlign = TextAlign.Right)
            )
        }

        // ✅ Image Picker UI (Upload Box)
        ImageUploadBox(
            imageUri = imageUri.value,
            onClick = { imagePickerLauncher.launch("image/*") }
        )

        // Submit Button
        Button(
            onClick = { viewModel.onEvent(AddTreeEvent.Submit) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = KhadraGreen)
        ) {
            Text("Submit")
        }

        // Loading & Error Messages
        if (state.value.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        state.value.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        if (state.value.isSuccess) {
            Text(
                text = "Tree added successfully!",
                color = KhadraGreen,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
