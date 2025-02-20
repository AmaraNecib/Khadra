package com.example.khadra.view

import android.widget.AdapterView.OnItemClickListener
import android.window.SplashScreen
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.khadra.model.Tree
import com.example.khadra.viewmodel.TreeViewModel


@Composable
fun MainScreen(
    treeViewModel: TreeViewModel,
    modifier: Modifier = Modifier
) {
    val treeState by treeViewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var filteredTrees by remember { mutableStateOf(treeState.trees) } //يعرض جميع الاشجار تلقائيا

    //****** تحديث القائمة عند تغيير البحث******
    LaunchedEffect(searchText, treeState.trees) {
        filteredTrees = treeState.trees.filter {
            it.name.contains(searchText, ignoreCase = true) ||
                    it.type.contains(searchText, ignoreCase = true)
        }
    }
        //الشريط السفلي
    var CurrentRoute by remember { mutableStateOf("home") }
    val items = listOf(
        ButtomNavigationBar(
            "Home",
            "home",
            Icons.Default.Home
        ),
        ButtomNavigationBar(
            "Task",
            "task",
            Icons.Default.Star
        ),
        ButtomNavigationBar(
            "Notification",
            "notification",
            Icons.Default.Notifications
        ),
        ButtomNavigationBar(
            "Profile",
            "profile",
            Icons.Default.Person
        )
    )
    Scaffold (
        bottomBar = {
            ButtomNavBar(items = items,currentScreen =CurrentRoute){
                CurrentRoute=it
            }
        }
    ){
            paddingValues ->
        when(CurrentRoute){
            "home" -> SecreenOne(paddingValues,Color.White)
            "task"-> SecreenOne(paddingValues,Color.Blue)
            "notification"-> SecreenOne(paddingValues,Color.DarkGray)
            "profile" -> SecreenOne(paddingValues,Color.Yellow)
        }
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            // عنوان التطبيق
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "خضراء",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // شريط البحث
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "بحث",
                    modifier = Modifier.clickable { }
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("ابحث...") },
                    singleLine = true
                )
            }

            // قائمة الأشجار
            LazyColumn(
                contentPadding = paddingValues, // يأخذ في الاعتبار شريط التنقل السفلي
                modifier = Modifier.padding(horizontal = 16.dp),
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
                    else -> {
                        items(filteredTrees) { tree ->
                            TreeCard(tree = tree)
                        }
                    }
                }
            }
        }

    }

}

@Composable
fun TreeCard(
    tree: Tree,
    modifier: Modifier = Modifier,
) {
    val statusColor = when (tree.status) {
        "Healthy" -> Color(0xFF4CAF50)
        "Moderate" -> Color(0xFFFFC107)
        "Low" -> Color(0xFFFF9800)
        "Critical" -> Color(0xFFF44336)
        else -> Color(0xFF9E9E9E)
    }

    val fillPercentage = when (tree.status) {
        "Healthy" -> 1.0f
        "Moderate" -> 0.7f
        "Low" -> 0.4f
        "Critical" -> 0.2f
        else -> 0.5f
    }



    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tree.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "الزراعة: ${tree.type}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "منذ ${tree.createdAt} سنوات",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "الحالة:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fillPercentage)
                                .background(statusColor, RoundedCornerShape(4.dp))
                        )
                    }
                }
            }
            Image(
                painter = rememberAsyncImagePainter(tree.urlImage),
                contentDescription = tree.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

//الدوال الخاصة بالشريط السفلي
@Composable
fun ButtomNavBar(
    items:List<ButtomNavigationBar> =listOf(),
    currentScreen: String,
    onItemClick: (String) -> Unit
){
 NavigationBar {
     items.forEach{item ->
         NavigationBarItem(
             selected = currentScreen == item.route  ,
             onClick = {onItemClick(item.route)},
             label = {Text(text=item.title)},
             icon = {Icon(imageVector =  item.icon, contentDescription = "")}
         )
     }
 }
}
data class ButtomNavigationBar(
    val title:String,
    val route:String,
    val icon :ImageVector
)


//الدالة التي تعرض عندما نظغط على ايقونات الشريط السفلي
@Composable
fun SecreenOne(paddingValues: PaddingValues,color: Color) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
    ){

    }
}
