package com.example.khadra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.khadra.ui.theme.KhadraTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.khadra.view.MainScreen
import com.example.khadra.viewmodel.TreeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KhadraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val treeViewModel = viewModel<TreeViewModel>()

                    MainScreen(
                        treeViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}