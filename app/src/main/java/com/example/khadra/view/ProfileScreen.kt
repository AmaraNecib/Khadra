package com.example.khadra.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column (modifier=Modifier.fillMaxSize(),
        verticalArrangement =  Arrangement.Center,
        horizontalAlignment =  Alignment.CenterHorizontally)
    {

        Text("Profile Screen", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)


    }
}