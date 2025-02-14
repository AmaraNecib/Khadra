package com.example.khadra

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khadra.ui.theme.KhadraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KhadraTheme {

                Scaffold( modifier = Modifier.fillMaxSize(),
                    topBar = { AppHeader() }
                ) { innerPadding ->
                    // TODO 0: Call the UI composable function
                    FirstUI(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2E7D32)) // لون أخضر داكن
            .padding(25.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "KAHDRA",
            color = Color.White,
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
@Composable
fun FirstUI(modifier: Modifier = Modifier) {
    var textValue by remember { mutableStateOf("") }
    val allItems = remember { mutableStateListOf<String>() }
    var searchQuery by remember { mutableStateOf("") }



    val displayedItems = if (searchQuery.isEmpty()) {
        allItems
    } else {
        allItems.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .background(Color(0xFFEFEFEF))
            .padding(25.dp)
            .fillMaxSize()
    ) {
        SearchInputBar(
            textValue = searchQuery,
            onTextValueChange = { searchQuery = it },
        )
        Spacer(modifier = Modifier.height(16.dp))
        AddItemBar(
            textValue = textValue,
            onTextValueChange = { textValue = it },
            onAddItem = {
                if (textValue.isNotBlank()) {
                    allItems.add(textValue)
                    textValue = ""
                }
            })


        CardsList(
            items = displayedItems,
            onDelete = { item -> allItems.remove(item) },
        )

    }
}




@Composable
fun SearchInputBar(textValue: String, onTextValueChange: (String) -> Unit) {
    TextField(
        value = textValue,
        onValueChange = onTextValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search...") },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.LightGray,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Green,
            cursorColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black
        )

    )
}
@Composable
fun AddItemBar(
    textValue: String,
    onTextValueChange: (String) -> Unit,
    onAddItem: () -> Unit
) {
    Column {
        TextField(
            value = textValue,
            onValueChange = onTextValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter text...") } ,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.LightGray,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Green,
                cursorColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = onAddItem,
                enabled = textValue.isNotBlank(),
                modifier = Modifier.padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.LightGray
                )
            ) {
                Text("Add")
            }
        }
    }
}
@Composable
fun CardsList(items: List<String>, onDelete: (String) -> Unit) {
    if (items.isEmpty()) {
        Text(
            "No items found",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center ,
            color = Color.Black,
            fontSize = 18.sp

        )
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = item, modifier = Modifier.weight(1f))
                        IconButton(onClick = { onDelete(item) }) {
                            Icon(Icons.Default.Delete, "Delete")
                        }
                    }
                }
            }
        }
    }
}
