package com.example.khadra.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.khadra.data.model.Tree
import com.example.khadra.presentation.components.KhadraTextField
import com.example.khadra.presentation.viewmodel.TreeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.khadra.presentation.view.MainScreen

@Composable
fun IrrigationScreen(
    viewModel: TreeViewModel = hiltViewModel()
) {
    val trees by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf<Tree?>(null) }
    var selectedFilter by remember { mutableStateOf("all") }
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredTrees = trees.trees.filter { tree ->
        val matchesSearch = tree.name.contains(searchQuery, ignoreCase = true) ||
                tree.type.contains(searchQuery, ignoreCase = true) ||
                tree.location.contains(searchQuery, ignoreCase = true)
                
        val matchesStatus = when (selectedFilter) {
            "critical" -> tree.status.lowercase() == "critical"
            "low" -> tree.status.lowercase() == "low"
            "moderate" -> tree.status.lowercase() == "moderate"
            else -> tree.status.lowercase() in listOf("low", "critical", "moderate")
        }
        
        matchesSearch && matchesStatus
    }.sortedByDescending { 
        when (it.status.lowercase()) {
            "critical" -> 3
            "low" -> 2
            "moderate" -> 1
            else -> 0
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White).padding(top = 80.dp)
    ) {
        Text(
            "الأشجار التي تحتاج إلى ري",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        KhadraTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = "بحث",
            trailingIcon = Icons.Default.Search
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Status filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == "all",
                onClick = { selectedFilter = "all" },
                label = { Text("الكل") }
            )
            FilterChip(
                selected = selectedFilter == "critical",
                onClick = { selectedFilter = "critical" },
                label = { Text("حرج") }
            )
            FilterChip(
                selected = selectedFilter == "low",
                onClick = { selectedFilter = "low" },
                label = { Text("منخفض") }
            )
            FilterChip(
                selected = selectedFilter == "moderate",
                onClick = { selectedFilter = "moderate" },
                label = { Text("متوسط") }
            )
        }

        // Statistics Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    "إحصائيات الري",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatItem("حرج", filteredTrees.count { it.status.lowercase() == "critical" })
                    StatItem("منخفض", filteredTrees.count { it.status.lowercase() == "low" })
                    StatItem("متوسط", filteredTrees.count { it.status.lowercase() == "moderate" })
                }
            }
        }

        if (filteredTrees.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "لا توجد أشجار تحتاج إلى ري حالياً",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTrees) { tree ->
                    TreeCard(
                        tree = tree,
                        onCardClick = { showDialog = tree }
                    )
                }
            }
        }
    }

    showDialog?.let { tree ->
        AlertDialog(
            onDismissRequest = { showDialog = null },
            title = { Text(tree.name) },
            text = {
                Column {
                    Text("النوع: ${tree.type}")
                    Text("الحالة: ${tree.status}")
                    Text("الموقع: ${tree.location}")
                    Text("آخر ري: ${formatDate(tree.lastIrrigationAction)}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("هل تريد تحديث حالة الري؟")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val currentTime = Date()
                        val updatedTree = tree.copy(
                            lastIrrigationAction = currentTime,
                            status = "healthy",
                            updatedAt = currentTime
                        )
                        viewModel.updateTree(updatedTree)
                        viewModel.addIrrigationHistory(tree.id, "تم ري الشجرة")
                        showDialog = null
                    }
                ) {
                    Text("تم الري")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = null }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
fun StatItem(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            count.toString(),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun formatDate(date: Date): String {
    val format = SimpleDateFormat("yyyy/MM/dd", Locale("ar"))
    return format.format(date)
}