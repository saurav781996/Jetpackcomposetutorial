package com.example.jetpackcomposetutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposetutorial.ui.theme.JetpackComposeTutorialTheme
import com.example.jetpackcomposetutorial.ui.screens.SplashScreen
import com.example.jetpackcomposetutorial.ui.screens.DashboardScreen
import com.example.jetpackcomposetutorial.ui.screens.WorkManagerTutorialScreen
import com.example.jetpackcomposetutorial.ui.screens.LiveDataTutorialScreen
import com.example.jetpackcomposetutorial.ui.screens.ViewModelTutorialScreen
import com.example.jetpackcomposetutorial.ui.screens.MVITutorialScreen
import com.example.jetpackcomposetutorial.ui.screens.RoomDBTutorialScreen
import com.example.jetpackcomposetutorial.ui.screens.DataStoreTutorialScreen
import com.example.jetpackcomposetutorial.ui.screens.NavigationTutorialScreen
import com.example.jetpackcomposetutorial.ui.screens.HiltTutorialScreen
import com.example.jetpackcomposetutorial.ui.screens.TestingTutorialScreen
import com.example.jetpackcomposetutorial.ui.screens.PagingTutorialScreen
import com.example.jetpackcomposetutorial.ui.screens.CoroutineWorkerTutorialScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeTutorialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("dashboard") {
            DashboardScreen(navController = navController)
        }
        composable("workmanager") {
            WorkManagerTutorialScreen(navController = navController)
        }
        composable("livedata") {
            LiveDataTutorialScreen(navController = navController)
        }
        composable("viewmodel") {
            ViewModelTutorialScreen(navController = navController)
        }
        composable("mvi") {
            MVITutorialScreen(navController = navController)
        }
        composable("roomdb") {
            RoomDBTutorialScreen(navController = navController)
        }
        composable("datastore") {
            DataStoreTutorialScreen(navController = navController)
        }
        composable("navigation") {
            NavigationTutorialScreen(navController = navController)
        }
        composable("hilt") {
            HiltTutorialScreen(navController = navController)
        }
        composable("testing") {
            TestingTutorialScreen(navController = navController)
        }
        composable("paging") {
            PagingTutorialScreen(navController = navController)
        }
        composable("coroutineworker") {
            CoroutineWorkerTutorialScreen(navController = navController)
        }
    }
}