package com.example.khadra.view

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.khadra.R
import com.example.khadra.model.Tree
import com.example.khadra.viewmodel.TreeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    treeViewModel: TreeViewModel,
    modifier: Modifier = Modifier
) {
    val treeState by treeViewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }

    val filteredTrees = remember(searchText, treeState.trees) {
        treeState.trees.filter { it.name.contains(searchText, ignoreCase = true) }
    }
    Column(
        modifier = modifier.fillMaxSize().background(Color.White)
    ) {
        // العنوان
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .background(Color(0xFF4CAF50)),

            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "خضراء",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(32.dp),
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
                containerColor = Color.Transparent, // Transparent for image visibility
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {})
        )
        // شريط البحث
        /*var searchText by remember { mutableStateOf("") }
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(40.dp))
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(40.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("بحث...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
        }*/
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp), contentAlignment = Alignment.CenterEnd) {
            Text(":الأشجار المغروسة", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
        // قائمة الأشجار
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when {
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
                filteredTrees.isEmpty() -> {
                    item {
                        Text(
                            text = "لا توجد نتائج مطابقة.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
                else -> {
                    items(filteredTrees) { tree ->
                        TreeCard(tree = tree)
                    }
                }
            }
        }

        // شريط التنقل السفلي
        NavigationBar(
            containerColor  = Color.White,
            tonalElevation   = 8.dp
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile",tint = Color.Gray,) },
                selected = false,
                onClick = { }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Place, contentDescription = "Map",tint = Color.Gray,) },
                selected = false,
                onClick = { }
            )
            NavigationBarItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color(0xFF4CAF50), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                    }
                },
                selected = false,
                onClick = { }
            )
            NavigationBarItem(
                icon = { Icon(
                    painter = painterResource(id = R.drawable.water_drop),
                    contentDescription = "Water Drap",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                ) },
                selected = false,
                onClick = { }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home",tint = Color.Gray,) },
                selected = false,
                onClick = { }
            )
        }
    }
}

@Composable
fun TreeCard(
    tree: Tree,
    modifier: Modifier = Modifier
) {
    val statusColor = when (tree.status) {
        "Healthy" -> Color(0xFF4CAF50)
        "Moderate" -> Color(0xFFFFC107)
        "Low" -> Color(0xFFFF9800)
        "Critical" -> Color(0xFFF44336)
        else -> Color(0xFF9E9E9E)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            )
            {

            // الحالة
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .width(40.dp)
                    .height(100.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))

                    .fillMaxHeight()

            ) {
                Text( text = ":الحالة ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.zIndex(100f).fillMaxWidth().align(Alignment.End),
                    textAlign = TextAlign.Center)
                LinearProgressIndicator(
                    progress = { 0.5f },
                    color = statusColor,
                    trackColor = Color.White,
                    modifier = Modifier
                        .width(50.dp)
                        .height(6.dp)
                        .fillMaxWidth()


                )
            }

            // معلومات الشجرة
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp)

            ) {
                Text(
                    text = tree.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "الوادي,بلدية الوادي",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "أسلام سليماني",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = " 32 شجرة",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            // صورة الشجرة
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(tree.urlImage)
                        .apply {
                            crossfade(true)
                            placeholder(R.drawable.ic_placeholder)
                            error(R.drawable.ic_error)
                        }.build()
                ),
                contentDescription = "Tree Image",
                modifier = Modifier
                    .size(70.dp)
                    .clip(RectangleShape)
                    .border(2.dp, Color.White,RectangleShape)
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxHeight()
                    .aspectRatio(1f),

                contentScale = ContentScale.Crop
            )
        }
    }
}
