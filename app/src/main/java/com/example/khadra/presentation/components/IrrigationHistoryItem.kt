package com.example.khadra.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.khadra.data.model.IrrigationHistory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun IrrigationHistoryItem(history: IrrigationHistory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = formatDate(history.irrigationDate),
                style = MaterialTheme.typography.titleMedium
            )
            
            history.notes?.let { notes ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notes,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale("ar"))
    return formatter.format(date)
}
