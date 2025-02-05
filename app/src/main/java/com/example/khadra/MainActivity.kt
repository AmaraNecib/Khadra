package com.example.khadra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.khadra.ui.theme.KhadraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KhadraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // TODO 0: Call the UI composable function (FirstUI)
                    // FirstUI (modifier=Modifier.padding(innerPadding))
                    FirstUI(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun FirstUI(modifier: Modifier = Modifier) {
    // TODO 1: Create a state for text input using `remember` and `mutableStateOf`
    var textValue by remember { mutableStateOf("") }

    // TODO 2: Create a list to hold all items (stateful)
    val allItems = remember { mutableStateListOf<String>() }

    // TODO 3: Create a state for the search query using `remember`
    var searchQuery by remember { mutableStateOf("") }

    // TODO 4: Create a filtered list based on the search query
    val displayedItems = if (searchQuery.isEmpty()) {
        allItems
    } else {
        allItems.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    // Column to organize UI components
    Column(
        modifier = modifier
            .padding(25.dp)
            .fillMaxSize()
    ) {
        // TODO 5: Implement the SearchInputBar and pass the state and event handlers to it
        SearchInputBar(
            textValue = textValue,
            onTextValueChange = { textValue = it },
            onAddItem = {
                if (textValue.isNotBlank()) {
                    allItems.add(textValue)
                    textValue = ""
                }
            },
            onSearch = { searchQuery = it }
        )

        // TODO 6: Display list of items using CardsList composable
        CardsList(displayedItems = displayedItems)
    }
}

@Composable
fun SearchInputBar(
    textValue: String,
    onTextValueChange: (String) -> Unit,
    onAddItem: () -> Unit,
    onSearch: (String) -> Unit
) {
    // This composable contains the search input and buttons for adding and searching.
    Column {
        // Text field to take user input
        TextField(
            value = textValue,
            onValueChange = onTextValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter text...") }
        )

        // Row to align the "Add" and "Search" buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Add button that adds an item to the list if text is not blank
            Button(onClick = { onAddItem() }) {
                Text("Add")
            }

            // Search button that updates the search query
            Button(onClick = { onSearch(textValue) }) {
                Text("Search")
            }
        }
    }
}

@Composable
fun CardsList(displayedItems: List<String>) {
    // Displays all the items passed to it in a LazyColumn for efficient scrolling
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(displayedItems) { item ->
            // Card for each item in the list
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(text = item, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // Preview function to display the UI in the IDE
    KhadraTheme {
        FirstUI(modifier = Modifier.fillMaxSize())
    }
}
