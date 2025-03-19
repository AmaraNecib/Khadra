package com.example.khadra.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khadra.R

@Composable
fun AddScreen(modifier: Modifier = Modifier) {
    var treeName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var imageSelected by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "غرس شجرة",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // إضافة صورة Placeholder وزر رفع صورة
        Box(
            modifier = Modifier
                .size(200.dp)
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { imageSelected = true }, // محاكاة اختيار صورة
            contentAlignment = Alignment.Center
        ) {
            if (imageSelected) {
                Image(
                    painter = painterResource(id = R.drawable.tree1), // استبدلها بصورة فعلية
                    contentDescription = "صورة مضافة"
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "إضافة صورة", fontSize = 16.sp)
                    Text(text = "+", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // حقل إدخال اسم الشجرة
        OutlinedTextField(
            value = treeName,
            onValueChange = { treeName = it },
            label = { Text("اسم الشجرة") },
            placeholder = { Text("مثال: شجرة اللوز") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // حقل إدخال الموقع
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("الموقع") },
            placeholder = { Text("مثال: حاسي خليفة، الوادي") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // زر الغرس
        Button(
            onClick = { /* تنفيذ العملية عند الضغط */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // لون أخضر
        ) {
            Text("غرس", fontSize = 18.sp, color = Color.White)
        }
    }
}
