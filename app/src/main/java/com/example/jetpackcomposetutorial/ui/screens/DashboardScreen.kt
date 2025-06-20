package com.example.jetpackcomposetutorial.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val description: String,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    // Set status bar text color to white
    val view = LocalView.current
    DisposableEffect(view) {
        val window = (view.context as android.app.Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        onDispose {}
    }

    val menuItems = listOf(
        MenuItem(
            title = "WorkManager",
            icon = Icons.Default.Work,
            description = "Schedule background tasks",
            route = "workmanager"
        ),
        MenuItem(
            title = "LiveData",
            icon = Icons.Default.DataArray,
            description = "Observe data changes",
            route = "livedata"
        ),
        MenuItem(
            title = "ViewModel",
            icon = Icons.Default.Storage,
            description = "Manage UI state",
            route = "viewmodel"
        ),
        MenuItem(
            title = "MVI Pattern",
            icon = Icons.Default.CallSplit,
            description = "Unidirectional data flow",
            route = "mvi"
        ),
        MenuItem(
            title = "Room Database",
            icon = Icons.Default.Storage,
            description = "Local data persistence",
            route = "roomdb"
        ),
        MenuItem(
            title = "DataStore",
            icon = Icons.Default.Settings,
            description = "Modern data storage",
            route = "datastore"
        ),
        MenuItem(
            title = "Navigation",
            icon = Icons.Default.Navigation,
            description = "Screen transitions",
            route = "navigation"
        ),
        MenuItem(
            title = "Hilt",
            icon = Icons.Default.Build,
            description = "Dependency injection",
            route = "hilt"
        ),
        MenuItem(
            title = "Testing",
            icon = Icons.Default.BugReport,
            description = "Mocks and stubs",
            route = "testing"
        ),
        MenuItem(
            title = "Paging Library",
            icon = Icons.Default.List,
            description = "Jetpack Compose Pagination",
            route = "paging"
        ),
        MenuItem(
            title = "CoroutineWorker",
            icon = Icons.Default.PlayArrow,
            description = "Kotlin Coroutine-compatible worker class",
            route = "coroutineworker"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Jetpack Compose Tutorial",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(menuItems) { item ->
                Card(
                    onClick = { navController.navigate(item.route) },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(200.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(64.dp)
                                .padding(bottom = 16.dp),
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(36.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
} 