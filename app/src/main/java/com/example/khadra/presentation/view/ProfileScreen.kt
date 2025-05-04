package com.example.khadra.presentation.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.khadra.R
import com.example.khadra.presentation.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onNavigateToAuth: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfilePicture(it) }
    }
    Spacer(Modifier.height(50.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 140.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(user?.avatarUrl ?: R.drawable.guy1)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .clickable { imagePicker.launch("image/*") },
                contentScale = ContentScale.Crop
            )
            
            IconButton(
                onClick = { imagePicker.launch("image/*") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile Picture",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Info
        Text(
            text = user?.fullName ?: "No Name Set",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = user?.email ?: "",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Stats Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(
                title = "Trees Planted",
                value = user?.treesPlanted ?: 0,
                color = MaterialTheme.colorScheme.primaryContainer
            )
            
            StatCard(
                title = "Trees Watered",
                value = user?.treesWatered ?: 0,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout Button
        Button(
            onClick = {
                viewModel.logout()
                onNavigateToAuth()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Logout", color = MaterialTheme.colorScheme.onError)
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(150.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                fontSize = 14.sp
            )
        }
    }
}