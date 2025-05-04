package com.example.khadra.presentation.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PermissionDialog(
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    permissionTextProvider: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "إذن مطلوب")
        },
        text = {
            Text(text = permissionTextProvider)
        },
        confirmButton = {
            Button(onClick = onOkClick) {
                Text(text = "حسناً")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "لاحقاً")
            }
        }
    )
}
