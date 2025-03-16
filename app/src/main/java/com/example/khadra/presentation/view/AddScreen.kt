package com.example.khadra.presentation.view

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.khadra.presentation.viewmodel.AddTreeViewModel
import com.example.khadra.ui.theme.KhadraGreen
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddScreen(viewModel: AddTreeViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(50.dp))
        Text("إضافة شجرة جديدة", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = KhadraGreen)

        PickImage(viewModel)

        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(AddTreeViewModel.AddTreeEvent.NameChanged(it)) },
            //label = { Text("اسم الشجرة") },
            placeholder = { Text("اسم الشجرة", textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())},

            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 18.sp,
                textDirection = TextDirection.Rtl // Align text to the right
            ),shape = RoundedCornerShape(14.dp), singleLine = true
        )

        TreeTypeDropDown(viewModel)

        TreeHealthDropdown(viewModel)


        OutlinedTextField(
            value = state.location,
            onValueChange = {viewModel.onEvent(AddTreeViewModel.AddTreeEvent.LocationChanged(it))},
            modifier = Modifier.fillMaxWidth(),
            readOnly = false,shape = RoundedCornerShape(14.dp),
            placeholder = { Text("موقع الشجرة مثال: البياضة", textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())},
            textStyle = TextStyle(
                textDirection = TextDirection.Rtl
            ), singleLine = true

        )

        Button(
            onClick = { viewModel.onEvent(AddTreeViewModel.AddTreeEvent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
            ,
            colors = ButtonDefaults.buttonColors(containerColor = KhadraGreen, contentColor = Color.White),
            enabled = !state.isLoading
        ) {
            Text(if (state.isLoading) "إضافة..." else "غرس", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }

}

@Composable
fun PickImage(viewModel: AddTreeViewModel) {
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri.value = uri
        uri?.let { viewModel.onEvent(AddTreeViewModel.AddTreeEvent.ImageUriChanged(it)) }
    }

    Box(
        modifier = Modifier
            .size(170.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(BorderStroke(2.dp, Color.Gray), shape = RoundedCornerShape(20.dp))
            .clickable { pickImageLauncher.launch("image/*") },
        contentAlignment = Alignment.Center
    ) {
        imageUri.value?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillHeight
            )
        } ?: Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Outlined.Add, contentDescription = "Add Image", tint = Color.Gray)
            Text("اضافة صورة", fontSize = 16.sp, color = Color.Gray)
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeHealthDropdown(viewModel: AddTreeViewModel) {
    val options = listOf("Healthy", "Moderate", "Low", "Critical")
    var expanded by remember { mutableStateOf(false) }
    val selectedOption = viewModel.state.collectAsState().value.status

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(

            value = selectedOption,
            onValueChange = { },

            readOnly = true,
            placeholder = { Text("حالة الشجرة", textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth()) },

            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Expand"
                )

            }, textStyle = TextStyle(
                textDirection = TextDirection.Rtl
            ), singleLine = true,

            modifier = Modifier.menuAnchor().fillMaxWidth(),

            )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        viewModel.onEvent(AddTreeViewModel.AddTreeEvent.StatusSelected(option))
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeTypeDropDown(viewModel: AddTreeViewModel) {
    val options = listOf("Fruit", "Palm", "Evergreen", "Ornamental", "Vegetable")
    var expanded by remember { mutableStateOf(false) }
    val selectedOption = viewModel.state.collectAsState().value.type

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {  },
            readOnly = true,
            placeholder = { Text("نوع الشجرة", textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth()) },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Expand"
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
                        viewModel.onEvent(AddTreeViewModel.AddTreeEvent.TypeSelected(option))
                        expanded = false
                    }
                )
            }
        }
    }
}