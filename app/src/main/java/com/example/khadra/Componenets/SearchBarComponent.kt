package com.example.khadra.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchBarComponent(
    searchText: String,
    onSearchTextChanged: (String) -> Unit
) {
     
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)  
            .padding(horizontal = 16.dp, vertical = 8.dp)  
    ) {
        TextField(
            value = searchText,
            onValueChange = { onSearchTextChanged(it) },
            placeholder = { Text("Search Trees", color = Color.Gray) },  
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .border(1.dp, Color.Gray, RoundedCornerShape(32.dp)),  
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,  
                unfocusedContainerColor = Color.White,  
                focusedTextColor = Color.Black,  
                unfocusedTextColor = Color.Black,  
                focusedIndicatorColor = Color.Transparent,  
                unfocusedIndicatorColor = Color.Transparent,  
                cursorColor = Color.Black  
            ),
            singleLine = true,  
            shape = RoundedCornerShape(12.dp)  
        )
    }
}