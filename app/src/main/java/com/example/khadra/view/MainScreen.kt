package com.example.khadra.view

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.khadra.R
import com.example.khadra.model.NavItem
import com.example.khadra.ui.theme.Inter
import com.example.khadra.ui.theme.KhadraGreen
import com.example.khadra.ui.theme.KhadraTheme
import com.example.khadra.viewmodel.TreeViewModel


@Composable

fun MainScreen(
    treeViewModel: TreeViewModel,
    modifier: Modifier = Modifier
) {
    val navItemsList = listOf(
        NavItem("Profile", painterResource(R.drawable.ic_outline_person_outline_24)),
        NavItem("Map", painterResource(R.drawable.ic_outline_map_24)),
        NavItem("Add", painterResource(R.drawable.ic_outline_add_circle_outline_24)),
        NavItem("Irrigation", painterResource(R.drawable.ic_water_drop)),
        NavItem("Home", painterResource(R.drawable.ic_outline_home_24))
    )



    var selectedIndex by remember { mutableIntStateOf(4) } // Default screen is Home
    val mod = modifier.fillMaxWidth()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
         topBar = { TopBar(selectedIndex) },
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
                NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                    navItemsList.forEachIndexed { index, item ->
                        if (index == 2) {
                            // Custom Add Button (Bigger + Colored)
                            NavigationBarItem(
                                selected = selectedIndex == index,
                                onClick = { selectedIndex = index },
                                icon = {
                                    Box(
                                        modifier = Modifier
                                            .size(70.dp) // ✅ Bigger button
                                            .background(KhadraGreen, shape = CircleShape) // ✅ Custom color
                                            .padding(10.dp) // ✅ Adjust padding for better appearance
                                    ) {
                                        Icon(
                                            item.icon,
                                            contentDescription = item.label,
                                            modifier = Modifier.size(50.dp), // ✅ Bigger icon
                                            tint = Color.White // ✅ White icon for contrast
                                        )
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = KhadraGreen,
                                    selectedTextColor = KhadraGreen,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        } else {
                            // Regular Navigation Item
                            NavigationBarItem(
                                selected = selectedIndex == index,
                                onClick = { selectedIndex = index },
                                icon = {
                                    Icon(item.icon, contentDescription = item.label, modifier = Modifier.size(38.dp))
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = KhadraGreen,
                                    selectedTextColor = KhadraGreen,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex = selectedIndex,
            treeViewModel = treeViewModel
        )
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int, treeViewModel: TreeViewModel) {
    when (selectedIndex) {
        0 -> ProfileScreen()
        1 -> MapScreen()
        2 -> AddScreen()
        3 -> IrrigationScreen()
        4 -> HomeScreen(modifier,treeViewModel) // ✅ Fixed: No infinite recursion
    }
}
//hell
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier,treeViewModel: TreeViewModel) {

    val uiState by treeViewModel.uiState.collectAsState()
    val treesList = uiState.trees
    var searchQuery by remember { mutableStateOf("") }
    val filteredTrees = treesList.filter { tree ->
        tree.name.contains(searchQuery, ignoreCase = true)
                ||
                tree.id.equals(searchQuery)
                ||tree.type.contains(searchQuery, ignoreCase = true)||tree.status.contains(searchQuery, ignoreCase = true)||tree.type.contains(searchQuery, ignoreCase = true)// Case-insensitive search
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 140.dp),
    ) {
        Column {
            // Search Bar (TextField)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { sq -> searchQuery = sq },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                shape = RoundedCornerShape(32.dp), // ✅ Matches Box clipping
                singleLine = true,
                placeholder = { Text("Search...", fontSize = 16.sp, color = Color.Black) },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.size(42.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Black
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent, // ✅ Transparent so image is visible
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {

                })
            )

            Spacer(modifier = Modifier.height(10.dp))
            Box (modifier=Modifier.fillMaxWidth().padding(horizontal =6.dp ), contentAlignment = Alignment.CenterEnd){
                Text(":الأشجار المغروسة", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            }

            Spacer(modifier = Modifier.height(16.dp))

          /*  Card(
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(8.dp), colors = CardColors(containerColor = Color.White
                    , contentColor = Color.Black, disabledContentColor = Color.White, disabledContainerColor = Color.White)
            ) {
                Row () {
                    Box(modifier = Modifier.weight(0.25f).height(100.dp).border(1.5.dp, color = Color.Black.copy(alpha = 0.25f), shape = RoundedCornerShape(9.dp))){

                    }

                    Box(modifier = Modifier.weight(0.5f).height(100.dp)){

                    }
                    Box(modifier = Modifier.weight(0.25f).height(100.dp)){

                    }

                }
            }*/
            if(uiState.isLoading){
                Box(
                    modifier = Modifier
                        .fillMaxSize().padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                        CircularProgressIndicator(
                            color = Color.Gray, // Customize the color
                            modifier = Modifier.size(100.dp) // Set the size of the loader
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(text = "Loading...", fontSize = 24.sp, fontWeight = FontWeight.Light, color = Color.Gray)

                    }

                }
            }else{
                if(searchQuery.isNotEmpty() && filteredTrees.isNotEmpty()){
                    LazyColumn (modifier = Modifier.fillMaxSize().padding(bottom = 110.dp)){items(filteredTrees){tree->
                        TreeCard(tree.name,tree.status,tree.urlImage)
                        Spacer(Modifier.height(12.dp))

                    }
                }
            }else if (filteredTrees.isEmpty()){
                Column (modifier=Modifier.fillMaxSize().padding(bottom = 100.dp),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

                    Image(modifier = Modifier.size(80.dp),painter = painterResource(R.drawable.ic_error), contentDescription = "Error")
                    Spacer(modifier=Modifier.height(8.dp))
                    Text(text = "No Results Found!", fontSize = 32.sp, fontWeight = FontWeight.Light, color = Color.Gray)


                }

                }else{
                    LazyColumn (modifier = Modifier.fillMaxSize().padding(bottom = 110.dp)){items(treesList){tree->
                        TreeCard(tree.name,tree.status,tree.urlImage)
                        Spacer(Modifier.height(12.dp))

                    }
                    }
                }


            }



        }


    }
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
                    color = KhadraGreen,
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

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun PreviewMainScreen() {
    val mockViewModel = TreeViewModel() // If your ViewModel requires parameters, use a mock/fake implementation

    KhadraTheme  {
        MainScreen(treeViewModel = mockViewModel)
    }
}


@Composable
fun TreeCard(treeName:String,status:String="healthy",imageUrl:String="") {

    Surface( modifier = Modifier.wrapContentSize().padding(horizontal = 16.dp)
        .shadow(
            elevation = 6.dp, // Shadow elevation
            shape = RoundedCornerShape(10.dp),
            clip = true, // Clip the shadow to the shape
        ), shape = RoundedCornerShape(10.dp))
    {
        Card(

            border = BorderStroke(1.dp, color = Color.Black.copy(alpha = 0.25f)),
            shape = RoundedCornerShape(9.dp), colors = CardColors(containerColor = Color(0x00FFFFFF)
                , contentColor = Color(0xFFFFFFFF), disabledContentColor = Color.White, disabledContainerColor = Color.White)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Space 1 (25%)
                Box(modifier = Modifier.weight(0.25f).height(100.dp).border(1.dp, color = Color.Black.copy(alpha = 0.25f), shape = RoundedCornerShape(9.dp))){
                    Column (modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                        Text(
                            text = ":الحالة ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .zIndex(100f).fillMaxWidth().align(Alignment.End),
                            textAlign = TextAlign.Center // Ensures text alignment within its own bounds
                        )

                        StatusBar(status)

                    }


                }

                // Space 2 (50%)
                Box(
                    modifier = Modifier.height(100.dp).fillMaxSize()
                        .weight(0.5f)
                ) {
                    Column (modifier=Modifier.height(100.dp).fillMaxWidth()){
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                            Text(
                                text = treeName,
                                fontSize = 20.sp,
                                color = Color.Black, fontWeight = FontWeight.ExtraBold, lineHeight = 2.sp
                            )
                        }

                        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                            Text(
                                text = " البياضة، الوادي، الوادي",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.End, fontWeight = FontWeight.Light
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_outline_location_on), // Replace with your icon resource
                                contentDescription = "Example Icon",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Gray // Optional: Set icon color
                            )


                        }


                        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                            Text(
                                text = "هيثم بكاري: 10 شجرة",
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
                                    contentDescription = "Circular Image with Border",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape), // Clip the image to a circular shape
                                    contentScale = ContentScale.Crop // Ensures the image fills the circle
                                )
                            }



                        }
                    }
                }
                val localContext = LocalContext.current

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
                        model =imageUrl, // Replace with your image resource
                        contentDescription = "Image 2",
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp)), // Apply rounded corners,
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
        "healthy" -> KhadraGreen
        else -> Color.Gray // Default color for unknown status
    }

    val progress = when (status.lowercase()) {
        "critical" -> 20
        "low" -> 30
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