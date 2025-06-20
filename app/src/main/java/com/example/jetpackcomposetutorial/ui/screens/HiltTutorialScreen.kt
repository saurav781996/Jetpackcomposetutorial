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
import com.example.jetpackcomposetutorial.ui.viewmodels.HiltViewModel
import com.example.jetpackcomposetutorial.data.User
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HiltTutorialScreen(navController: NavController) {
    val viewModel: HiltViewModel = viewModel()
    val userData by viewModel.userData.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var showCodeExamples by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    
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
                        "Hilt",
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
                Icon(Icons.Default.Add, contentDescription = "Add User")
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
                            text = "What is Hilt?",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project. It's built on top of Dagger and provides a standard way to incorporate Dagger dependency injection into an Android application. Hilt is Compose-ready and works seamlessly with Jetpack Compose.",
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
                                text = "Hilt Implementation:",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("@HiltAndroidApp\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("class ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("MyApplication : Application()\n\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("@Module\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("@InstallIn(SingletonComponent::class)\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("object ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("AppModule {\n\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    @Provides\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    @Singleton\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("    fun ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("provideUserRepository(): UserRepository {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("        return ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("UserRepositoryImpl()\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("    }\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("}\n\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("@HiltViewModel\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("class ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("UserViewModel @Inject constructor(\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("    private val ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("userRepository: UserRepository\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append(") : ViewModel() {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("    // ViewModel implementation\n"
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "User Management Demo",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // User data display
                        if (userData.isNotEmpty()) {
                            userData.forEach { user ->
                                UserCard(
                                    user = user,
                                    onDelete = {
                                        viewModel.deleteUser(user.id)
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
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No users yet",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Tap the + button to add your first user",
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
                            text = "Hilt Advantages:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val advantages = listOf(
                            "• Reduced Boilerplate: Less manual DI code",
                            "• Compose Integration: Works seamlessly with Compose",
                            "• Standardized DI: Consistent dependency injection",
                            "• Compile-time Safety: Type-safe dependency resolution",
                            "• Scoped Dependencies: Singleton, Activity, Fragment scopes",
                            "• Testing Support: Easy to mock dependencies"
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

    // Add User Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New User") },
            text = {
                Column {
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter user name") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = userEmail,
                        onValueChange = { userEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter user email") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (userName.isNotBlank() && userEmail.isNotBlank()) {
                            viewModel.addUser(userName, userEmail)
                            userName = ""
                            userEmail = ""
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
fun UserCard(
    user: User,
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
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ID: ${user.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
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