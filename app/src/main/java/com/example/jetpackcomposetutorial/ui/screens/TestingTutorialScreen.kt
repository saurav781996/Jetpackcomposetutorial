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
import com.example.jetpackcomposetutorial.ui.viewmodels.TestingViewModel
import com.example.jetpackcomposetutorial.data.TestResult
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestingTutorialScreen(navController: NavController) {
    val viewModel: TestingViewModel = viewModel()
    val testResults by viewModel.testResults.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var showCodeExamples by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedFramework by remember { mutableStateOf("Mockk") }
    
    var testName by remember { mutableStateOf("") }
    var testDescription by remember { mutableStateOf("") }
    
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
                        "Testing with Mocks",
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
                Icon(Icons.Default.Add, contentDescription = "Add Test")
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
                            text = "What are Mocks?",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Mocks are objects that simulate the behavior of real objects in controlled ways. They are used in unit testing to isolate the code being tested and verify interactions between objects. Mockk and Mockito are popular mocking frameworks for Kotlin and Java that help create mocks and stubs for testing.",
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
                                text = "Mockk vs Mockito Examples:",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("// Mockk Example\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("val ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("userRepository = mockk<UserRepository>()\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("every { userRepository.getUser(1) } returns User(1, \"John\")\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("verify { userRepository.getUser(1) }\n\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("// Mockito Example\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("val ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("userRepository = mock(UserRepository::class.java)\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("`when`(userRepository.getUser(1)).thenReturn(User(1, \"John\"))\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("verify(userRepository).getUser(1)\n\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("// Compose Testing\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("@Test\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("fun ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("testUserScreen() {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    composeTestRule.setContent {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("        UserScreen()\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    }\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    composeTestRule.onNodeWithText(\"John\").assertExists()\n"
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
                                text = "Test Results Demo",
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
                        
                        // Framework selection
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
                                    text = "Select Testing Framework:",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    FilterChip(
                                        selected = selectedFramework == "Mockk",
                                        onClick = { 
                                            selectedFramework = "Mockk"
                                            viewModel.runMockkTests()
                                        },
                                        label = { Text("Mockk") }
                                    )
                                    
                                    FilterChip(
                                        selected = selectedFramework == "Mockito",
                                        onClick = { 
                                            selectedFramework = "Mockito"
                                            viewModel.runMockitoTests()
                                        },
                                        label = { Text("Mockito") }
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Test results display
                        if (testResults.isNotEmpty()) {
                            testResults.forEach { result ->
                                TestResultCard(
                                    result = result,
                                    onDelete = {
                                        viewModel.deleteTestResult(result.id)
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
                                        imageVector = Icons.Default.BugReport,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No test results yet",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Select a framework above or add custom tests",
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
                            text = "Testing Benefits:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val benefits = listOf(
                            "• Isolation: Test components independently",
                            "• Reliability: Verify expected behavior",
                            "• Refactoring: Safe code changes",
                            "• Documentation: Tests serve as examples",
                            "• Confidence: Ensure code quality",
                            "• Mocking: Simulate external dependencies"
                        )
                        
                        benefits.forEach { benefit ->
                            Text(
                                text = benefit,
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

    // Add Test Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Custom Test") },
            text = {
                Column {
                    OutlinedTextField(
                        value = testName,
                        onValueChange = { testName = it },
                        label = { Text("Test Name") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., UserRepositoryTest") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = testDescription,
                        onValueChange = { testDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Test user retrieval functionality") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (testName.isNotBlank()) {
                            viewModel.addCustomTest(testName, testDescription, selectedFramework)
                            testName = ""
                            testDescription = ""
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
fun TestResultCard(
    result: TestResult,
    onDelete: () -> Unit
) {
    val statusColor = when (result.status) {
        "PASSED" -> Color(0xFF4CAF50)
        "FAILED" -> Color(0xFFF44336)
        "RUNNING" -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = result.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = statusColor.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = result.status,
                            style = MaterialTheme.typography.bodySmall,
                            color = statusColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = result.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Framework: ${result.framework} • Duration: ${result.duration}ms",
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