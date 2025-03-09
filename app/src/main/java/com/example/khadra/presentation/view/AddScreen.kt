package com.example.khadra.presentation.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.khadra.R
import com.example.khadra.data.model.AddTreeEvent
import com.example.khadra.presentation.viewmodel.AddTreeViewModel
import com.example.khadra.ui.theme.KhadraGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<AddTreeViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onEvent(AddTreeEvent.ImageSelected(it)) }

    }




    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 55.dp), contentAlignment = Alignment.CenterEnd) {
        Text(": اسم الشجرة", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(Modifier.height(330.dp))

    }

    Column(

        modifier = modifier
            .fillMaxSize()
            .padding(45.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(Modifier.height(130.dp))

        OutlinedTextField(
            value = state.value.name,
            onValueChange = { viewModel.onEvent(AddTreeEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 1.dp),
            shape = RoundedCornerShape(32.dp),
            singleLine = true,
            placeholder = { Text("مثال: شجرة الليمون", fontSize = 16.sp, color = Color.Gray) },
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(42.dp),
                    painter =  painterResource(id=R.drawable.plant),
                    contentDescription = "Search Icon",
                    tint = Color.Black
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent, // Transparent for image visibility
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                focusedTextColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)

        )

        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), contentAlignment = Alignment.CenterEnd) {
            Text(": الموقع ", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)

        }

        // حقل الموقع

        OutlinedTextField(
            value = state.value.location,
            onValueChange = { viewModel.onEvent(AddTreeEvent.LocationChanged(it)) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 1.dp),
            shape = RoundedCornerShape(32.dp),
            singleLine = true,
            placeholder = { Text(" مثال: حاسي مسعود", fontSize = 16.sp, color = Color.Gray) },
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(42.dp),
                    imageVector = Icons.Default.Place,
                    contentDescription = "Search Icon",
                    tint = Color.Black
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent, // Transparent for image visibility
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                focusedTextColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )




        ImageUploadBox(
            imageUri = state.value.imageUri,
            onClick = { imagePicker.launch("image/*") }
        )


        Button(
            onClick = { viewModel.onEvent(AddTreeEvent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KhadraGreen)
        ) {
            Text("غرس الشجرة", fontSize = 18.sp)
        }

        // حالة التحميل
        if (state.value.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // عرض الأخطاء
        state.value.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ImageUploadBox(
    imageUri: Uri?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Gray)
            .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "صورة الشجرة",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = "إضافة صورة",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(30.dp)
                )
                Text("اضغط لإضافة صورة", color = Color.DarkGray)
            }
        }
    }
}


