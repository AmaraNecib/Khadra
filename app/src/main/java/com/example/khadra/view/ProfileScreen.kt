package com.example.khadra.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.khadra.R

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // خلفية العلوية
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFF2E7D32)), // لون أخضر مشابه للتصميم
            contentAlignment = Alignment.Center
        ) {
            // صورة البروفايل
            Image(
                painter = painterResource(id = R.drawable.guy1), // تأكد من وجود الصورة في `res/drawable`
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // اسم المستخدم
        Text(text = "رياض محرز", fontSize = 22.sp, style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(4.dp))

        // عدد الأشجار وحالات السقي
        Text(text = "عدد الأشجار المزروعة: 15 🌱", fontSize = 16.sp)
        Text(text = "عدد حالات السقي: 70 💧", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // قائمة الخيارات
        ProfileOption(icon = Icons.Default.Person, text = "تعديل معلومات الحساب")
        ProfileOption(icon = Icons.Default.List, text = "قائمة أشجاري")
        ProfileOption(icon = Icons.Default.MoreVert, text = "اختيار اللغة")
        ProfileOption(icon = Icons.Default.Person, text = "حول التطبيق")

        Spacer(modifier = Modifier.height(24.dp))

        // زر تسجيل الخروج
        Button(
            onClick = { /* تنفيذ تسجيل الخروج */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = "خروج", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "تسجيل الخروج", color = Color.White)
        }
    }
}

// عنصر فردي في القائمة
@Composable
fun ProfileOption(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = text, tint = Color.Black)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = text, fontSize = 16.sp)
        }
    }
}
