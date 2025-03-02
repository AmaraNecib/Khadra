package com.example.khadra.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import com.example.khadra.R
import com.example.khadra.model.NavItem
import com.example.khadra.model.Tree
import com.example.khadra.ui.theme.Inter
import com.example.khadra.ui.theme.Green
import com.example.khadra.viewmodel.TreeViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun MainScreen(
    treeViewModel: TreeViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navItemsList = listOf(
        NavItem("Profile", painterResource(R.drawable.ic_outline_person_outline_24), "profile"),
        NavItem("Map", painterResource(R.drawable.ic_outline_map_24), "map"),
        NavItem("Add", painterResource(R.drawable.ic_outline_add_circle_outline_24), "add"),
        NavItem("Irrigation", painterResource(R.drawable.ic_water_drop), "irrigation"),
        NavItem("Home", painterResource(R.drawable.ic_outline_home_24), "home")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = { TopBar(currentRoute = currentRoute) },
        bottomBar = {
            Box(
                modifier = Modifier
                    .drawBehind {
                        drawLine(
                            color = Color.Gray.copy(alpha = 0.6f),
                            start = Offset.Zero,
                            end = Offset(size.width, 0f),
                            strokeWidth = 4f
                        )
                    }
                    .fillMaxWidth()
            ) {
                NavigationBar(containerColor = Color.Gray) {
                    navItemsList.forEachIndexed { index, item ->
                        val selected = currentRoute == item.route
                        if (index == 2) {
                            // Custom Add Button (Bigger + Colored)
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Box(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .background(Green, shape = CircleShape)
                                            .padding(10.dp)
                                    ) {
                                        Icon(
                                            item.icon,
                                            contentDescription = item.label,
                                            modifier = Modifier.size(60.dp),
                                            tint = Color.White
                                        )
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Green,
                                    selectedTextColor = Green,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        } else {
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        item.icon,
                                        contentDescription = item.label,
                                        modifier = Modifier.size(38.dp)
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Green,
                                    selectedTextColor = Green,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(modifier, treeViewModel) }
            composable("profile") { ProfileScreen(modifier) }
            composable("map") { MapScreen(modifier) }
            composable("add") { AddScreen(modifier) }
            composable("irrigation") { IrrigationScreen(modifier) }
        }
    }
}

@Composable
fun TopBar(currentRoute: String?) {
    val title = when (currentRoute) {
        "profile" -> "الحساب الشخصي"
        "map" -> "الاشجار القريبة منك"
        "add" -> "غرس شجرة"
        "irrigation" -> "اشجار تحتاج سقي"
        "home" -> "خضراء"
        else -> ""
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Green,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .size(120.dp)
        ) {
            Text(
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.4f),
                        offset = Offset(0f, 10f),
                        blurRadius = 8f
                    )
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier, treeViewModel: TreeViewModel) {
    val uiState by treeViewModel.uiState.collectAsState()
    val treesList = uiState.trees
    var searchQuery by remember { mutableStateOf("") }
    val filteredTrees = treesList.filter { tree ->
        tree.name.contains(searchQuery, ignoreCase = true) ||
                tree.id.equals(searchQuery) ||
                tree.type.contains(searchQuery, ignoreCase = true) ||
                tree.status.contains(searchQuery, ignoreCase = true)
    }


    // State to track the selected tree
    var selectedTree by remember { mutableStateOf<Tree?>(null) }


    // Show Tree Details Dialog or Screen if a tree is selected
    selectedTree?.let { tree ->
        TreeDetailsDialog(tree = tree) {
            selectedTree = null // Close the details dialog or screen
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
    ) {
        Column {

            // Search Bar (TextField)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { text -> searchQuery = text },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                shape = RoundedCornerShape(32.dp), // Matches Box clipping
                singleLine = true,
                placeholder = { Text("Search", fontSize = 16.sp, color = Color.Black) },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Black
                    )
                },

                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent, // Transparent for image visibility

                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {})

            )

            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp), contentAlignment = Alignment.CenterEnd) {
                Text(":الأشجار المغروسة", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))


            if (searchQuery.isNotEmpty() && filteredTrees.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 110.dp)) {
                    items(filteredTrees) { tree ->
                        TreeCard(tree = tree, onCardClick = { selectedTree = it }) // Pass tree and click handler
                        Spacer(Modifier.height(12.dp))
                    }
                }
            } else if (filteredTrees.isEmpty()) {
                Column(modifier = Modifier.fillMaxSize().padding(bottom = 100.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(modifier = Modifier.size(80.dp), painter = painterResource(R.drawable.ic_error), contentDescription = "Error")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "No Results Found!", fontSize = 32.sp, fontWeight = FontWeight.Light, color = Color.Gray)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 1.dp)) {
                    items(treesList) { tree ->
                        TreeCard(tree = tree, onCardClick = { selectedTree = it }) // Pass tree and click handler
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TreeDetailsDialog(tree: Tree, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tree Details") },
        text = {
            Column {
                Text("Tree Name: ${tree.name}")
                Text("Status: ${tree.status}")
                Text("Type: ${tree.type}")
                Text("Location: ${tree.coordinates.first}, ${tree.coordinates.second}")
                Text("Last Irrigation: ${tree.lastIrrigationAction}")
                // Add more fields as needed
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}



@Composable
fun TopBar(selectedIndex:Int) {

    var displayTopBar by remember { mutableStateOf(false) }
    when (selectedIndex){
        0-> displayTopBar=false
        1-> displayTopBar=true
        2-> displayTopBar=false
        3-> displayTopBar=false
        4-> displayTopBar=true

    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        //color = Color.Black,
        elevation = CardDefaults.cardElevation(8.dp) // Elevation creates a shadow effect
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Green,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .size(120.dp)
        ) {
            Box(modifier = Modifier
                .padding(top = 8.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                ))
            Text(
                text = when (selectedIndex) {
                    0 -> "الحساب الشخصي"
                    1 -> "الاشجار القريبة منك"
                    2 -> "غرس شجرة"
                    3 -> "اشجار تحتاج سقي"
                    4 -> "خضراء"
                    else -> { "" }
                },
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = Inter,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.4f), // Shadow color
                        offset = Offset(0f, 10f), // Shadow position (x, y)
                        blurRadius = 8f // Blur effect
                    )
                )

            )

        }
    }


}




@Composable
fun TreeCard(tree: Tree, onCardClick: (Tree) -> Unit) {

    Surface(
        modifier = Modifier
            .clickable { onCardClick(tree) }
            .wrapContentSize()
            .padding(horizontal = 16.dp)
            .shadow(
                elevation = 6.dp, // Shadow elevation
                shape = RoundedCornerShape(10.dp),
                clip = true, // Clip the shadow to the shape
            ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Card(
            border = BorderStroke(1.dp, color = Color.Black.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(9.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0x00FFFFFF))
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Space 1 (25%)
                Box(
                    modifier = Modifier
                        .weight(0.25f)
                        .height(100.dp)
                        .border(1.dp, color = Color.Black.copy(alpha = 0.25f), shape = RoundedCornerShape(9.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = ":الحالة ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.zIndex(100f).fillMaxWidth().align(Alignment.End),
                            textAlign = TextAlign.Center // Ensures text alignment within its own bounds
                        )
                        StatusBar(tree.status) // Assuming this is a custom composable for showing the status
                    }
                }

                // Space 2 (50%)
                Box(
                    modifier = Modifier.height(100.dp).fillMaxSize().weight(0.5f)
                ) {
                    Column(modifier = Modifier.height(100.dp).fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                            Text(
                                text = tree.name,
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 2.sp
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Text(
                                text = "الوادي",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.End,
                                fontWeight = FontWeight.Light
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_outline_location_on), // Replace with your icon resource
                                contentDescription = "Example Icon",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Gray // Optional: Set icon color
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Text(
                                text = "بن عتوس نوالدين: 32 شجرة",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.Gray
                            )
                            Box(
                                modifier = Modifier
                                    .size(20.dp) // Set the size of the circular image with border
                                    .clip(CircleShape).shadow(elevation = 4.dp) // Apply shadow // Clip the Box to a circular shape
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.guy1), // Replace with your image resource
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape), // Clip the image to a circular shape
                                    contentScale = ContentScale.Crop // Ensures the image fills the circle
                                )
                            }
                        }
                    }
                }

                // Space 3 (25%)
                Box(
                    modifier = Modifier
                        .weight(0.25f)
                        .aspectRatio(1f) // Enforce 1:1 aspect ratio for the Box
                        .wrapContentSize() // Make the Box wrap around the content
                        .padding(8.dp)
                        .border(2.dp, color = Color.Black.copy(alpha = 0.25f), shape = RoundedCornerShape(20.dp))
                ) {
                    AsyncImage(
                        model = tree.urlImage, // Use tree's image URL
                        contentDescription = "Tree Image",
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp)), // Apply rounded corners
                        contentScale = ContentScale.Crop // Ensures the image fits inside the Box
                    )
                }
            }
        }
    }
}



@Composable
fun StatusBar(status: String) {
    val color = when (status.lowercase()) {
        "critical"->Color(0xFFFF0000)// Convert to lowercase for case-insensitive comparison
        "low" -> Color(0xFFFF6F00)
        "moderate" -> Color(0xFFFFDD00)
        "healthy" -> Green
        else -> Color.Gray // Default color for unknown status
    }

    val progress = when (status.lowercase()) {
        "critical" -> 10
        "low" -> 25
        "moderate" -> 45
        "healthy" -> 60
        else -> 60 // Default color for unknown status
    }

    Box (contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .width(60.dp)
                .height(10.dp) // Height of the status bar
                .background(Color.White).border(BorderStroke(1.dp, color = Color.Gray))
        ){
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .width(progress.dp)
                    .height(10.dp) // Height of the status bar
                    .background(color).border(BorderStroke(1.dp, color = Color.Gray))
            )
        }
    }



}