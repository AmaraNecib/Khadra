package com.example.khadra.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.khadra.presentation.view.components.DropdownSelector
import com.example.khadra.presentation.viewmodel.*

@Composable
fun AddScreen(viewModel: AddTreeViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(AddTreeEvent.EnterName(it)) },
            label = { Text("اسم الشجرة") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownSelector(
            label = "نوع الشجرة",
            options = listOf("زيتون", "نخيل", "ليمون"),
            selectedOption = state.type,
            onOptionSelected = { viewModel.onEvent(AddTreeEvent.EnterType(it)) }
        )

        DropdownSelector(
            label = "حالة الشجرة",
            options = listOf("جيدة", "تحتاج رعاية", "ميتة"),
            selectedOption = state.status,
            onOptionSelected = { viewModel.onEvent(AddTreeEvent.EnterStatus(it)) }
        )

        OutlinedTextField(
            value = state.latitude,
            onValueChange = { viewModel.onEvent(AddTreeEvent.EnterLatitude(it)) },
            label = { Text("خط العرض") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.longitude,
            onValueChange = { viewModel.onEvent(AddTreeEvent.EnterLongitude(it)) },
            label = { Text("خط الطول") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.imageUrl,
            onValueChange = { viewModel.onEvent(AddTreeEvent.EnterImageUrl(it)) },
            label = { Text("رابط الصورة") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.onEvent(AddTreeEvent.Submit) },
            enabled = !state.isLoading
        ) {
            Text("إضافة الشجرة")
        }

        if (state.isSuccess) {
            Text("تمت الإضافة بنجاح!", color = MaterialTheme.colorScheme.primary)
        }

        state.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
