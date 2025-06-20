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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.work.*
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.jetpackcomposetutorial.workers.DemoWorker
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkManagerTutorialScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val workManager = WorkManager.getInstance(context)
    var workStatus by remember { mutableStateOf("No work scheduled") }
    var workProgress by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    var currentWorkId by remember { mutableStateOf<UUID?>(null) }
    var showCodeExample by remember { mutableStateOf(false) }

    // Observe work status
    LaunchedEffect(currentWorkId) {
        currentWorkId?.let { workId ->
            workManager.getWorkInfoByIdLiveData(workId)
                .observe(lifecycleOwner) { workInfo ->
                    when (workInfo?.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            workStatus = "Work completed successfully!"
                            workProgress = 100
                            currentWorkId = null
                        }
                        WorkInfo.State.FAILED -> {
                            workStatus = "Work failed!"
                            currentWorkId = null
                        }
                        WorkInfo.State.RUNNING -> {
                            workProgress = workInfo.progress.getInt("progress", 0)
                            workStatus = "Work in progress..."
                        }
                        else -> {}
                    }
                }
        }
    }

    // Clean up when the composable is disposed
    DisposableEffect(lifecycleOwner) {
        onDispose {
            currentWorkId?.let { workId ->
                workManager.getWorkInfoByIdLiveData(workId).removeObservers(lifecycleOwner)
            }
        }
    }

    val tutorialSteps = listOf(
        TutorialStep(
            title = "What is WorkManager?",
            description = "WorkManager is an API that makes it easy to schedule deferrable, asynchronous tasks that are expected to run even if the app exits or the device restarts. It's the recommended solution for persistent work.",
            icon = Icons.Default.Info
        ),
        TutorialStep(
            title = "Key Features",
            description = "• Guaranteed execution: WorkManager ensures your work completes, even if the app is closed\n" +
                    "• Battery-friendly: Respects system battery optimizations\n" +
                    "• Respects system constraints: Network, battery, storage, etc.\n" +
                    "• Works with or without Google Play Services\n" +
                    "• Supports both one-time and periodic work\n" +
                    "• Supports chaining of work",
            icon = Icons.Default.Star
        ),
        TutorialStep(
            title = "Types of Work",
            description = "• OneTimeWorkRequest: For one-off tasks\n" +
                    "• PeriodicWorkRequest: For recurring tasks\n" +
                    "• WorkRequest.Builder: For custom work requests with constraints\n" +
                    "• WorkContinuation: For chaining multiple work requests",
            icon = Icons.Default.List
        ),
        TutorialStep(
            title = "Constraints",
            description = "You can specify constraints like:\n" +
                    "• Network connectivity (metered, unmetered, connected)\n" +
                    "• Battery status (not low, charging)\n" +
                    "• Device charging state\n" +
                    "• Storage space\n" +
                    "• Device idle state",
            icon = Icons.Default.Settings
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WorkManager Tutorial") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showCodeExample = !showCodeExample }) {
                        Icon(
                            if (showCodeExample) Icons.Default.CodeOff else Icons.Default.Code,
                            "Toggle Code Example"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tutorialSteps) { step ->
                TutorialCard(step = step)
            }

            if (showCodeExample) {
                item {
                    CodeExampleCard()
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Try it out!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Schedule a background task",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Status and progress section
                        when (workStatus) {
                            "Work in progress..." -> {
                                Text(
                                    text = workStatus,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                LinearProgressIndicator(
                                    progress = workProgress / 100f,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = "$workProgress%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            "Work completed successfully!" -> {
                                Text(
                                    text = workStatus,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            "Work failed!" -> {
                                Text(
                                    text = workStatus,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            "All work cancelled" -> {
                                Text(
                                    text = workStatus,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            else -> {
                                Text(
                                    text = "Click 'Start Work' to begin",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        val workRequest = OneTimeWorkRequestBuilder<DemoWorker>()
                                            .setConstraints(
                                                Constraints.Builder()
                                                    .setRequiresBatteryNotLow(true)
                                                    .build()
                                            )
                                            .build()
                                        
                                        workManager.enqueue(workRequest)
                                        currentWorkId = workRequest.id
                                        workStatus = "Work scheduled!"
                                    }
                                }
                            ) {
                                Icon(Icons.Default.PlayArrow, "Start")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Start Work")
                            }
                            
                            Button(
                                onClick = {
                                    scope.launch {
                                        workManager.cancelAllWork()
                                        currentWorkId = null
                                        workStatus = "All work cancelled"
                                        workProgress = 0
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Stop, "Stop")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Cancel Work")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CodeExampleCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Code Examples",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Worker Class Example
            Text(
                text = "1. Create a Worker Class:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = """
                    class DemoWorker(
                        context: Context,
                        params: WorkerParameters
                    ) : CoroutineWorker(context, params) {
                        
                        override suspend fun doWork(): Result {
                            // Simulate work with progress updates
                            for (progress in 0..100 step 10) {
                                if (isStopped) return Result.failure()
                                
                                setProgress(workDataOf("progress" to progress))
                                delay(1000) // 1 second delay
                            }
                            return Result.success()
                        }
                    }
                """.trimIndent(),
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Work Request Example
            Text(
                text = "2. Create and Enqueue Work:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = """
                    // Create work request with constraints
                    val workRequest = OneTimeWorkRequestBuilder<DemoWorker>()
                        .setConstraints(
                            Constraints.Builder()
                                .setRequiresBatteryNotLow(true)
                                .build()
                        )
                        .build()
                    
                    // Enqueue the work
                    WorkManager.getInstance(context)
                        .enqueue(workRequest)
                """.trimIndent(),
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Observe Work Status Example
            Text(
                text = "3. Observe Work Status:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = """
                    // Observe work status
                    WorkManager.getInstance(context)
                        .getWorkInfoByIdLiveData(workRequest.id)
                        .observe(lifecycleOwner) { workInfo ->
                            when (workInfo?.state) {
                                WorkInfo.State.SUCCEEDED -> {
                                    // Work completed successfully
                                }
                                WorkInfo.State.FAILED -> {
                                    // Work failed
                                }
                                WorkInfo.State.RUNNING -> {
                                    // Work in progress
                                    val progress = workInfo.progress
                                        .getInt("progress", 0)
                                }
                                else -> {}
                            }
                        }
                """.trimIndent(),
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "How WorkManager Works:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "1. When you enqueue work, WorkManager stores the work request in a local database\n" +
                    "2. WorkManager then schedules the work based on the constraints you've set\n" +
                    "3. When the constraints are met, WorkManager starts your worker\n" +
                    "4. The worker executes in a background thread\n" +
                    "5. WorkManager handles process death and device restarts\n" +
                    "6. You can observe the work status through LiveData\n" +
                    "7. WorkManager ensures your work completes, even if the app is closed",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TutorialCard(step: TutorialStep) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = step.icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = step.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = step.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

data class TutorialStep(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) 