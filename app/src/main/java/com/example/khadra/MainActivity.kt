package com.example.khadra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khadra.ui.theme.KhadraTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KhadraTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) { innerPadding ->
                    FirstUI(modifier = Modifier.padding(innerPadding))
                }
            }
        }
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
            .padding(22.dp)
            .fillMaxSize()
    ) {
        SearchInputBar(
            textValue = textValue,
            onTextValueChange = { newText -> textValue = newText },
            onAddItem = { newItem ->
                if (newItem.isNotBlank()) {
                    allItems.removeAll { it.isBlank() }
                    allItems.add(newItem)
                    textValue = ""
                }
            },
            onSearch = { newQuery -> searchQuery = newQuery }
        )
        CardsList(displayedItems) { item ->
            allItems.remove(item)
        }
    }
}

@Composable
fun SearchInputBar(
    textValue: String,
    onTextValueChange: (String) -> Unit,
    onAddItem: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    Column {
        BasicTextField(
            value = textValue,
            onValueChange = onTextValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(35.dp))
                .padding(16.dp),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (textValue.isEmpty()) {
                        Text("      ENTER YOUR TREE...", color = Color.Gray)

                    }
                    innerTextField()
                }
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { onAddItem(textValue) },
                modifier = Modifier
                    .size(56.dp)
                    .shadow(4.dp, RoundedCornerShape(50)),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "ADD TREE",
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(
                onClick = { onSearch(textValue) },
                modifier = Modifier
                    .size(56.dp)
                    .shadow(4.dp, RoundedCornerShape(50)),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "SEARCH YOUR TREE",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun CardsList(displayedItems: List<String>, onDelete: (String) -> Unit) {
    if (displayedItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(2.dp, Color.Red, shape = RoundedCornerShape(12.dp))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Warning",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "⚠️ NOT FOND!",
                    color = Color.Red,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(displayedItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = { onDelete(item) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "DELETE",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KhadraTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFAFEF5)
        ) {
            FirstUI()
        }
    }
}

