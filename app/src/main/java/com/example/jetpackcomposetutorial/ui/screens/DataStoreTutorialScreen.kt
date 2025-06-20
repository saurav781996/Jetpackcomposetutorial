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
import com.example.jetpackcomposetutorial.ui.viewmodels.DataStoreViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataStoreTutorialScreen(navController: NavController) {
    val viewModel: DataStoreViewModel = viewModel()
    val settings by viewModel.settings.collectAsState()
    val message by viewModel.message.collectAsState()
    
    var showCodeExamples by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    
    var key by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    
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
                        "DataStore",
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
                Icon(Icons.Default.Add, contentDescription = "Add Setting")
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
                            text = "What is DataStore?",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "DataStore is a data storage solution that replaces SharedPreferences. It provides a more robust, type-safe way to store key-value pairs and objects. DataStore uses Kotlin coroutines and Flow for asynchronous, consistent, and transactional data storage.",
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
                                text = "DataStore Implementation:",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("// DataStore Keys\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("object ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("PreferencesKeys {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF9CDCFE))) {
                                        append("    val ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("USER_NAME = stringPreferencesKey(\"user_name\")\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF9CDCFE))) {
                                        append("    val ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("DARK_MODE = booleanPreferencesKey(\"dark_mode\")\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF9CDCFE))) {
                                        append("    val ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("NOTIFICATION_COUNT = intPreferencesKey(\"notification_count\")\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("}\n\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("// DataStore Class\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("class ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("UserPreferencesRepository(\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("    private val dataStore: DataStore<Preferences>\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append(") {\n\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("    val ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("userName: Flow<String> = dataStore.data\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("        .map { preferences ->\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("            preferences[PreferencesKeys.USER_NAME] ?: \"\"\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("        }\n\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("    suspend fun ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("saveUserName(name: String) {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("        dataStore.edit { preferences ->\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("            preferences[PreferencesKeys.USER_NAME] = name\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("        }\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("    }\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("}\n")
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
                            text = "App Settings Demo",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Settings display
                        if (settings.isNotEmpty()) {
                            settings.forEach { (key, value) ->
                                SettingCard(
                                    key = key,
                                    value = value,
                                    onDelete = {
                                        viewModel.deleteSetting(key)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
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
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No settings yet",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Tap the + button to add your first setting",
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
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "DataStore Advantages:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val advantages = listOf(
                            "• Type Safety: Compile-time type checking",
                            "• Coroutines Support: Asynchronous operations",
                            "• Flow Integration: Reactive data streams",
                            "• Transactional: Atomic read/write operations",
                            "• Migration Support: Easy data schema updates",
                            "• Better Error Handling: Structured error management"
                        )
                        
                        advantages.forEach { advantage ->
                            Text(
                                text = advantage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Setting Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Setting") },
            text = {
                Column {
                    OutlinedTextField(
                        value = key,
                        onValueChange = { key = it },
                        label = { Text("Setting Key") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., user_name, dark_mode") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = value,
                        onValueChange = { value = it },
                        label = { Text("Setting Value") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., John Doe, true") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (key.isNotBlank() && value.isNotBlank()) {
                            viewModel.saveSetting(key, value)
                            key = ""
                            value = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Add")
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
fun SettingCard(
    key: String,
    value: String,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = key,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
} 