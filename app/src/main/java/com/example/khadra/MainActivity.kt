package com.example.khadra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.khadra.presentation.view.AuthScreen
import com.example.khadra.presentation.view.MainScreen
import com.example.khadra.presentation.view.TreeDetailsScreen
import com.example.khadra.presentation.viewmodel.AuthViewModel
import com.example.khadra.presentation.viewmodel.TreeViewModel
import com.example.khadra.ui.theme.KhadraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhadraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel = hiltViewModel<AuthViewModel>()
                    val treeViewModel = hiltViewModel<TreeViewModel>()

                    NavHost(
                        navController = navController,
                        startDestination = "auth"
                    ) {
                        composable("auth") {
                            AuthScreen(
                                onNavigateToMain = {
                                    navController.navigate("main") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                },
                                viewModel = authViewModel
                            )
                        }

                        composable("main") {
                            MainScreen(
                                treeViewModel = treeViewModel,
                                onNavigateToTreeDetails = { treeId ->
                                    navController.navigate("tree_details/$treeId")
                                },
                                modifier = Modifier,
                                onNavigateToAuth = {
                                    navController.navigate("auth") {
                                        popUpTo("main") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(
                            route = "tree_details/{treeId}",
                            arguments = listOf(navArgument("treeId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val treeId = backStackEntry.arguments?.getString("treeId")
                            TreeDetailsScreen(
                                treeId = treeId ?: "",
                                onNavigateBack = { navController.navigateUp() },
                                viewModel = treeViewModel,
                                onEditTree = {  }
                            )
                        }
                    }
                }
            }
        }
    }
}