package com.example.jetpackcomposetutorial.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Data class for tasks
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val priority: Priority,
    val dueDate: LocalDateTime,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class Priority {
    LOW, MEDIUM, HIGH;

    @Composable
    fun toColor(): Color {
        return when (this) {
            LOW -> MaterialTheme.colorScheme.tertiary
            MEDIUM -> MaterialTheme.colorScheme.primary
            HIGH -> MaterialTheme.colorScheme.error
        }
    }
}

enum class TaskFilter {
    ALL, ACTIVE, COMPLETED, HIGH_PRIORITY
}

enum class TaskSort {
    DUE_DATE, PRIORITY, CREATED_AT
}

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _filter = MutableStateFlow(TaskFilter.ALL)
    val filter: StateFlow<TaskFilter> = _filter.asStateFlow()

    private val _sort = MutableStateFlow(TaskSort.DUE_DATE)
    val sort: StateFlow<TaskSort> = _sort.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    // Computed property for filtered and sorted tasks
    val filteredAndSortedTasks: StateFlow<List<Task>> = combine(
        _tasks, _filter, _sort
    ) { tasks, filter, sort ->
        tasks.filter { task ->
            when (filter) {
                TaskFilter.ALL -> true
                TaskFilter.ACTIVE -> !task.isCompleted
                TaskFilter.COMPLETED -> task.isCompleted
                TaskFilter.HIGH_PRIORITY -> task.priority == Priority.HIGH
            }
        }.sortedWith { task1, task2 ->
            when (sort) {
                TaskSort.DUE_DATE -> task1.dueDate.compareTo(task2.dueDate)
                TaskSort.PRIORITY -> task2.priority.ordinal.compareTo(task1.priority.ordinal)
                TaskSort.CREATED_AT -> task2.createdAt.compareTo(task1.createdAt)
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        // Simulate loading initial tasks
        loadInitialTasks()
    }

    private fun loadInitialTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1000) // Simulate network delay
            _tasks.value = listOf(
                Task(
                    id = 1,
                    title = "Complete Project Documentation",
                    description = "Write comprehensive documentation for the new feature",
                    priority = Priority.HIGH,
                    dueDate = LocalDateTime.now().plusDays(2)
                ),
                Task(
                    id = 2,
                    title = "Code Review",
                    description = "Review pull requests from team members",
                    priority = Priority.MEDIUM,
                    dueDate = LocalDateTime.now().plusDays(1)
                ),
                Task(
                    id = 3,
                    title = "Update Dependencies",
                    description = "Check and update project dependencies",
                    priority = Priority.LOW,
                    dueDate = LocalDateTime.now().plusDays(3)
                )
            )
            _isLoading.value = false
        }
    }

    fun addTask(title: String, description: String, priority: Priority, dueDate: LocalDateTime) {
        val newTask = Task(
            id = (_tasks.value.maxOfOrNull { it.id } ?: 0) + 1,
            title = title,
            description = description,
            priority = priority,
            dueDate = dueDate
        )
        _tasks.value = _tasks.value + newTask
    }

    fun toggleTaskCompletion(taskId: Int) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) task.copy(isCompleted = !task.isCompleted)
            else task
        }
    }

    fun deleteTask(taskId: Int) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }

    fun setFilter(filter: TaskFilter) {
        _filter.value = filter
    }

    fun setSort(sort: TaskSort) {
        _sort.value = sort
    }

    fun showAddTaskDialog() {
        _showAddDialog.value = true
    }

    fun hideAddTaskDialog() {
        _showAddDialog.value = false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewModelTutorialScreen(
    navController: NavController,
    viewModel: TaskViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var showCode by remember { mutableStateOf(false) }
    val tasks by viewModel.filteredAndSortedTasks.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val sort by viewModel.sort.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val showAddDialog by viewModel.showAddDialog.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ViewModel Tutorial") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showCode = !showCode }) {
                        Icon(
                            imageVector = if (showCode) Icons.Default.Code else Icons.Default.CodeOff,
                            contentDescription = if (showCode) "Hide Code" else "Show Code",
                            tint = if (showCode) 
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                            else 
                                MaterialTheme.colorScheme.onPrimary
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddTaskDialog() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Add Task")
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
            // ViewModel Explanation
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "What is ViewModel?",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Key Concepts
                        Text(
                            text = "Key Concepts:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• State Management: ViewModel is designed to store and manage UI-related data in a lifecycle conscious way\n" +
                                  "• Lifecycle Awareness: Survives configuration changes like screen rotations\n" +
                                  "• Separation of Concerns: Keeps UI logic separate from UI components\n" +
                                  "• Data Persistence: Maintains data during configuration changes\n" +
                                  "• Coroutine Support: Built-in support for Kotlin coroutines",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Benefits
                        Text(
                            text = "Benefits:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• No Memory Leaks: Automatically handles lifecycle events\n" +
                                  "• Configuration Changes: Data survives screen rotations\n" +
                                  "• UI State Management: Centralized state management\n" +
                                  "• Testing: Easier to test business logic\n" +
                                  "• Code Organization: Better separation of concerns",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // When to Use
                        Text(
                            text = "When to Use ViewModel:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Managing UI state that needs to survive configuration changes\n" +
                                  "• Handling complex data operations\n" +
                                  "• Coordinating between different UI components\n" +
                                  "• Implementing business logic\n" +
                                  "• Managing data that needs to be shared between screens",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Practical Example Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Practical Example: Task Manager",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "A real-world example showing ViewModel in action with state management, filtering, and sorting",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Filter and Sort Controls
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
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Filter Tasks",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = filter == TaskFilter.ALL,
                                onClick = { viewModel.setFilter(TaskFilter.ALL) },
                                label = { Text("All") }
                            )
                            FilterChip(
                                selected = filter == TaskFilter.ACTIVE,
                                onClick = { viewModel.setFilter(TaskFilter.ACTIVE) },
                                label = { Text("Active") }
                            )
                            FilterChip(
                                selected = filter == TaskFilter.COMPLETED,
                                onClick = { viewModel.setFilter(TaskFilter.COMPLETED) },
                                label = { Text("Completed") }
                            )
                            FilterChip(
                                selected = filter == TaskFilter.HIGH_PRIORITY,
                                onClick = { viewModel.setFilter(TaskFilter.HIGH_PRIORITY) },
                                label = { Text("High Priority") }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Sort By",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = sort == TaskSort.DUE_DATE,
                                onClick = { viewModel.setSort(TaskSort.DUE_DATE) },
                                label = { Text("Due Date") }
                            )
                            FilterChip(
                                selected = sort == TaskSort.PRIORITY,
                                onClick = { viewModel.setSort(TaskSort.PRIORITY) },
                                label = { Text("Priority") }
                            )
                            FilterChip(
                                selected = sort == TaskSort.CREATED_AT,
                                onClick = { viewModel.setSort(TaskSort.CREATED_AT) },
                                label = { Text("Created At") }
                            )
                        }
                    }
                }
            }

            // Task List
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggleComplete = { viewModel.toggleTaskCompletion(task.id) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }

            // Code Example
            if (showCode) {
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
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Implementation Code",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // Data Classes
                            Text(
                                text = "1. Data Classes:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = """
                                    data class Task(
                                        val id: Int,
                                        val title: String,
                                        val description: String,
                                        val priority: Priority,
                                        val dueDate: LocalDateTime,
                                        val isCompleted: Boolean = false,
                                        val createdAt: LocalDateTime = LocalDateTime.now()
                                    )

                                    enum class Priority {
                                        LOW, MEDIUM, HIGH
                                    }

                                    enum class TaskFilter {
                                        ALL, ACTIVE, COMPLETED, HIGH_PRIORITY
                                    }

                                    enum class TaskSort {
                                        DUE_DATE, PRIORITY, CREATED_AT
                                    }
                                """.trimIndent(),
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // ViewModel
                            Text(
                                text = "2. ViewModel Implementation:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = """
                                    class TaskViewModel : ViewModel() {
                                        private val _tasks = MutableStateFlow<List<Task>>(emptyList())
                                        val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

                                        private val _filter = MutableStateFlow(TaskFilter.ALL)
                                        val filter: StateFlow<TaskFilter> = _filter.asStateFlow()

                                        private val _sort = MutableStateFlow(TaskSort.DUE_DATE)
                                        val sort: StateFlow<TaskSort> = _sort.asStateFlow()

                                        // Computed property for filtered and sorted tasks
                                        val filteredAndSortedTasks: StateFlow<List<Task>> = 
                                            combine(_tasks, _filter, _sort) { tasks, filter, sort ->
                                                tasks.filter { task ->
                                                    when (filter) {
                                                        TaskFilter.ALL -> true
                                                        TaskFilter.ACTIVE -> !task.isCompleted
                                                        TaskFilter.COMPLETED -> task.isCompleted
                                                        TaskFilter.HIGH_PRIORITY -> 
                                                            task.priority == Priority.HIGH
                                                    }
                                                }.sortedWith { task1, task2 ->
                                                    when (sort) {
                                                        TaskSort.DUE_DATE -> 
                                                            task1.dueDate.compareTo(task2.dueDate)
                                                        TaskSort.PRIORITY -> 
                                                            task2.priority.ordinal
                                                                .compareTo(task1.priority.ordinal)
                                                        TaskSort.CREATED_AT -> 
                                                            task2.createdAt
                                                                .compareTo(task1.createdAt)
                                                    }
                                                }
                                            }.stateIn(
                                                viewModelScope,
                                                SharingStarted.WhileSubscribed(5000),
                                                emptyList()
                                            )
                                    }
                                """.trimIndent(),
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // UI Implementation
                            Text(
                                text = "3. UI Implementation:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = """
                                    @Composable
                                    fun ViewModelTutorialScreen(
                                        viewModel: TaskViewModel = viewModel()
                                    ) {
                                        val tasks by viewModel.filteredAndSortedTasks
                                            .collectAsStateWithLifecycle()
                                        val filter by viewModel.filter
                                            .collectAsStateWithLifecycle()
                                        val sort by viewModel.sort
                                            .collectAsStateWithLifecycle()

                                        // UI implementation using the collected states
                                    }
                                """.trimIndent(),
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { viewModel.hideAddTaskDialog() },
            onAddTask = { title, description, priority, dueDate ->
                viewModel.addTask(title, description, priority, dueDate)
                viewModel.hideAddTaskDialog()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggleComplete() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (task.isCompleted)
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (task.isCompleted)
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Due Date",
                            modifier = Modifier.size(16.dp),
                            tint = task.priority.toColor()
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Due: ${task.dueDate.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = task.priority.toColor()
                        )
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Task",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (String, String, Priority, LocalDateTime) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var dueDate by remember { mutableStateOf(LocalDateTime.now().plusDays(1)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Task") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Priority.values().forEach { priorityOption ->
                        FilterChip(
                            selected = priority == priorityOption,
                            onClick = { priority = priorityOption },
                            label = { Text(priorityOption.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAddTask(title, description, priority, dueDate)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Add Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 