package com.example.khadra.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khadra.R

data class Tree(val name: String, val location: String, val status: String, val imageRes: Int)

@Composable
fun IrrigationScreen(modifier: Modifier = Modifier) {
    val trees = listOf(
        Tree("شجرة زيتون الأخضر", "أمام المدرسة، الحي الجنوبي", "بحاجة إلى الري", R.drawable.tree1),
        Tree("شجرة زيتون الأخضر", "أمام المدرسة، الحي الجنوبي", "بحاجة إلى الري", R.drawable.tree1),
        Tree("شجرة زيتون الأخضر", "أمام المدرسة، الحي الجنوبي", "بحاجة إلى الري", R.drawable.tree1),
        Tree("شجرة زيتون الأخضر", "أمام المدرسة، الحي الجنوبي", "بحاجة إلى الري", R.drawable.tree1),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "أشجار تحتاج سقي",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trees) { tree ->
                TreeItem(tree)
            }
        }
    }
}

@Composable
fun TreeItem(tree: Tree) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = tree.imageRes),
                contentDescription = "صورة الشجرة",
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = tree.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = tree.location, fontSize = 14.sp, color = Color.Gray)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "الحالة: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = tree.status, color = Color.Red)
                }
            }
        }
    }
}
