package com.example.khadra.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.khadra.R
import com.example.khadra.components.SearchBarComponent
import com.example.khadra.model.Tree
import com.example.khadra.viewmodel.TreeViewModel

@Composable
fun MainScreen(
    treeViewModel: TreeViewModel,
    modifier: Modifier = Modifier
) {
    // Collecting the UI state from the view model
    val treeState by treeViewModel.uiState.collectAsState()
    val searchText by treeViewModel.searchText.collectAsState()

    // Use a Column to stack the app bar, search bar, and the LazyColumn
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // App Bar at the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .background(Color(0xFF4CAF50), shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Khadra",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }

        // Search Bar under the app bar
        SearchBarComponent(
            searchText = searchText,
            onSearchTextChanged = { treeViewModel.updateSearchText(it) }
        )

        // Layout for displaying the tree list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when {
                // Displaying a loading indicator while the trees are being fetched
                treeState.isLoading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                // Displaying a message when no trees are available
                treeState.filteredTrees.isEmpty() -> {
                    item {
                        Text(
                            text = "No trees available",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                    }
                }

                // Displaying the tree cards once the trees are loaded
                else -> {
                    items(treeState.filteredTrees) { tree ->
                        TreeCard(
                            tree = tree
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TreeCard(
    tree: Tree,
    modifier: Modifier = Modifier
) {
    // Determining the color based on the tree's health status
    val statusColor = when (tree.status) {
        "Healthy" -> Color(0xFF4CAF50)
        "Moderate" -> Color(0xFFFFC107)
        "Low" -> Color(0xFFFF9800)
        "Critical" -> Color(0xFFF44336)
        else -> Color(0xFF9E9E9E)
    }
    val statusWidth = when (tree.status){
        "Healthy" -> 60.dp
            "Moderate" -> 45.dp
        "Low" -> 30.dp
        "Critical" -> 15.dp
        else -> 45.dp
    }

    // Card layout for each tree item
    Card(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp)) // Add shadow
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)), // Add black border
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Set card background to white
        )
    ) {
        Row(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .height(100.dp)  // Adjusting the height for the row layout
        ) {
            // Tree Image on the left with padding
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = tree.urlImage)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            placeholder(R.drawable.ic_placeholder)
                            error(R.drawable.ic_error)
                        }).build()
                ),
                contentDescription = tree.name,
                modifier = Modifier
                    .size(100.dp)  // Image size adjusted for the row layout
                    .clip(RoundedCornerShape(8.dp))
                    .padding(0.dp), // Add padding to the image
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Tree Name and Status Bar
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)  // To make sure the status bar stretches to the right
                    .align(Alignment.CenterVertically) // Center the name vertically
            ) {
                // Tree Name with black color
                Text(
                    text = tree.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black, // Set text color to black
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 30.dp)
                )
            }

            // Status Box sticking to the top and right borders
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    // Status box takes 25% of the width
                    .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                // Round only top-right and bottom-right corners
            ) {
                // Status bar inside the grey rectangle
                Text(
                    text = tree.status,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 50.dp) // Adjust padding to position text below the bar
                )
                Box(
                    modifier = Modifier
                        .width(70.dp)  // Width of the outer box
                        .height(14.dp)  // Height for the outer box
                        .background(Color.Gray, RoundedCornerShape(12.dp))  // Outer box with gray background
                        .border(1.dp, Color.Black, RoundedCornerShape(12.dp))  // Black border for the outer box
                        .align(Alignment.Center)  // Center the outer box both vertically and horizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()  // Status bar takes the full height of the outer box
                            .width(statusWidth)  // Status bar takes 80% of the outer box's width
                            .background(statusColor, RoundedCornerShape(12.dp))  // Status bar with the status color
                            .align(Alignment.CenterStart)  // Align the status bar to the left inside the outer box
                    )
                }



                // Status text below the bar

            }
        }
    }
}
