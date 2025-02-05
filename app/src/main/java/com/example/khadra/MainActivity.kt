package com.example.khadra

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khadra.ui.theme.GreenJc
import com.example.khadra.ui.theme.KhadraTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ItemViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KhadraTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    MyTopAppBar(viewModel)
                    Spacer(modifier = Modifier.height(100.dp))
                    MyOutLineTextField(viewModel)
                    Spacer(modifier = Modifier.height(18.dp))
                    MyButton(viewModel)
                    Spacer(modifier = Modifier.height(16.dp))
                    ItemList(viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(viewModel: ItemViewModel) {
    val context = LocalContext.current.applicationContext
    TopAppBar(title = { Text(text = "NourEddine", fontSize = 30.sp) }, navigationIcon = {
        IconButton(onClick = {
            Toast.makeText(context, "NourEddine", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.plant),
                contentDescription = "NourEddine icon"
            )
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = GreenJc,
        titleContentColor = Color.White,
        navigationIconContentColor = Color.Black,
    ), actions = {
        IconButton(onClick = {
            Toast.makeText(context, "Profile", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile",
                tint = Color.Black
            )
        }
        IconButton(onClick = {
            Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color.Black
            )
        }
        IconButton(onClick = {
            Toast.makeText(context, "Menu", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Menu",
                tint = Color.Black
            )
        }
    })
}


@Composable
fun MyOutLineTextField(viewModel: ItemViewModel) {
    val textValue = remember { mutableStateOf("") }

    OutlinedTextField(
        value = textValue.value,
        onValueChange = { text ->
            textValue.value = text
            viewModel.updateSearchQuery(text)
        },
        label = {
            Text(text = stringResource(id = R.string.search), color = Color.Black)
        },
    )
}


@Composable
fun MyButton(viewModel: ItemViewModel) {
    val textValue = remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = textValue.value,
            onValueChange = { text -> textValue.value = text },
            label = {
                Text(
                    text = stringResource(id = R.string.my_out_lined_text), color = Color.Black
                )
            },

            )
        Button(
            onClick = {
                if (textValue.value.isNotBlank()) {
                    viewModel.addItem(textValue.value)
                    textValue.value = ""
                }
            }, colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.teal_200),
                contentColor = colorResource(id = R.color.black)
            ),

            border = BorderStroke(
                width = 6.dp, color = colorResource(id = R.color.purple_700)
            ), modifier = Modifier.padding(2.dp)
        ) {
            Text(text = stringResource(id = R.string.add_button))


        }
    }
}


@Composable
fun ItemList(viewModel: ItemViewModel) {
    val items by viewModel.items.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val filteredItems = if (searchQuery.isEmpty()) {
        items
    } else {
        items.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(filteredItems) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}