package com.example.jetpackcomposetutorial.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackcomposetutorial.ui.viewmodels.NavigationViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTutorialScreen(navController: NavController) {
    val viewModel: NavigationViewModel = viewModel()
    val currentRoute by viewModel.currentRoute.collectAsState()
    val navigationHistory by viewModel.navigationHistory.collectAsState()
    val message by viewModel.message.collectAsState()
    
    var showCodeExamples by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    
    var screenName by remember { mutableStateOf("") }
    var screenDescription by remember { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }

    // Show message as Snackbar
    LaunchedEffect(message) {
        message?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Navigation",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showCodeExamples = !showCodeExamples }) {
                        Icon(
                            imageVector = if (showCodeExamples) Icons.Default.Code else Icons.Default.CodeOff,
                            contentDescription = "Toggle Code Examples"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Screen")
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "What is Navigation?",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Jetpack Compose Navigation is a library for handling navigation between screens in your app. It provides a type-safe way to navigate between composables, handle deep links, and manage the back stack. Navigation supports arguments, nested graphs, and complex navigation patterns.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            if (showCodeExamples) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E1E)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Navigation Implementation:",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("// Navigation Setup\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("val ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("navController = rememberNavController()\n\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("NavHost(\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("    navController = navController,\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    startDestination = \"home\"\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append(") {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    composable(\"home\") {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("        HomeScreen(navController)\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    }\n\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    composable(\"profile/{userId}\") { backStackEntry ->\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("        val ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("userId = backStackEntry.arguments?.getString(\"userId\")\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("        ProfileScreen(userId)\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    }\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("}\n\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("// Navigation Actions\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("navController.navigate(\"profile/123\")\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("navController.navigateUp()\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("navController.popBackStack()\n")
                                    }
                                },
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Navigation Demo",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Current route display
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Current Route:",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = currentRoute,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Navigation buttons
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.navigateTo("home") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Home, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Home")
                            }
                            
                            Button(
                                onClick = { viewModel.navigateTo("profile/123") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Profile")
                            }
                            
                            Button(
                                onClick = { viewModel.navigateTo("settings") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Settings, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Settings")
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Navigation history
                        if (navigationHistory.isNotEmpty()) {
                            Text(
                                text = "Navigation History:",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            navigationHistory.forEach { route ->
                                NavigationHistoryCard(
                                    route = route,
                                    onNavigate = { viewModel.navigateTo(route) }
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Navigation,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No navigation history",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Use the buttons above to navigate",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Navigation Features:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val features = listOf(
                            "• Type-safe Navigation: Compile-time route checking",
                            "• Arguments: Pass data between screens",
                            "• Deep Links: Handle external navigation",
                            "• Nested Graphs: Complex navigation hierarchies",
                            "• Back Stack Management: Automatic back navigation",
                            "• Animations: Smooth screen transitions"
                        )
                        
                        features.forEach { feature ->
                            Text(
                                text = feature,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Screen Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Screen") },
            text = {
                Column {
                    OutlinedTextField(
                        value = screenName,
                        onValueChange = { screenName = it },
                        label = { Text("Screen Route") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., details, about") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = screenDescription,
                        onValueChange = { screenDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Details screen") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (screenName.isNotBlank()) {
                            viewModel.navigateTo(screenName)
                            screenName = ""
                            screenDescription = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Navigate")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun NavigationHistoryCard(
    route: String,
    onNavigate: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = route,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                modifier = Modifier.weight(1f)
            )
            
            IconButton(onClick = onNavigate) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = "Navigate",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
} 