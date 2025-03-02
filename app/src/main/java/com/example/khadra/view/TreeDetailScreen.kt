package com.example.khadra.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.khadra.model.Tree
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TreeDetailScreen(
    tree: Tree,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // صورة الشجرة
        AsyncImage(
            model = tree.urlImage,
            contentDescription = tree.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        // اسم الشجرة
        Text(
            text = tree.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // معلومات إضافية
        InfoRow(label = "النوع", value = tree.type)
        InfoRow(label = "الحالة", value = tree.status)
        InfoRow(label = "آخر ري", value = tree.lastIrrigationAction.formatDate())
        InfoRow(label = "الإحداثيات", value = "${tree.coordinates.first}, ${tree.coordinates.second}")
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// دالة مساعدة لتنسيق التاريخ
fun Date.formatDate(): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
    return formatter.format(this)
}