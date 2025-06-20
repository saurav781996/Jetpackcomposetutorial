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

// MVI Components
sealed class SearchIntent {
    data class Search(val query: String) : SearchIntent()
    data class SelectItem(val item: SearchItem) : SearchIntent()
    object ClearSearch : SearchIntent()
}

data class SearchState(
    val query: String = "",
    val items: List<SearchItem> = emptyList(),
    val selectedItem: SearchItem? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class SearchItem(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val lastUpdated: LocalDateTime
)

sealed class SearchEffect {
    data class ShowToast(val message: String) : SearchEffect()
    data class NavigateToDetail(val itemId: Int) : SearchEffect()
}

class SearchViewModel : ViewModel() {
    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchEffect>()
    val effect: SharedFlow<SearchEffect> = _effect.asSharedFlow()

    // Sample data
    private val sampleItems = listOf(
        SearchItem(
            id = 1,
            title = "Android Development",
            description = "Learn modern Android development with Kotlin",
            category = "Programming",
            lastUpdated = LocalDateTime.now().minusDays(2)
        ),
        SearchItem(
            id = 2,
            title = "Jetpack Compose",
            description = "Build native UI with modern toolkit",
            category = "UI/UX",
            lastUpdated = LocalDateTime.now().minusDays(1)
        ),
        SearchItem(
            id = 3,
            title = "Kotlin Coroutines",
            description = "Asynchronous programming with Kotlin",
            category = "Programming",
            lastUpdated = LocalDateTime.now()
        ),
        SearchItem(
            id = 4,
            title = "Material Design 3",
            description = "Create beautiful, usable interfaces",
            category = "UI/UX",
            lastUpdated = LocalDateTime.now().minusHours(5)
        ),
        SearchItem(
            id = 5,
            title = "Android Architecture",
            description = "Build scalable and maintainable apps",
            category = "Architecture",
            lastUpdated = LocalDateTime.now().minusDays(3)
        )
    )

    fun processIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.Search -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        query = intent.query,
                        isLoading = true,
                        error = null
                    )
                    
                    // Simulate network delay
                    delay(500)
                    
                    try {
                        val filteredItems = if (intent.query.isEmpty()) {
                            emptyList()
                        } else {
                            sampleItems.filter { item ->
                                item.title.contains(intent.query, ignoreCase = true) ||
                                item.description.contains(intent.query, ignoreCase = true) ||
                                item.category.contains(intent.query, ignoreCase = true)
                            }
                        }
                        
                        _state.value = _state.value.copy(
                            items = filteredItems,
                            isLoading = false
                        )
                        
                        if (filteredItems.isEmpty() && intent.query.isNotEmpty()) {
                            _effect.emit(SearchEffect.ShowToast("No results found"))
                        }
                    } catch (e: Exception) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Error searching: ${e.message}"
                        )
                    }
                }
            }
            is SearchIntent.SelectItem -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(selectedItem = intent.item)
                    _effect.emit(SearchEffect.NavigateToDetail(intent.item.id))
                }
            }
            is SearchIntent.ClearSearch -> {
                _state.value = SearchState()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MVITutorialScreen(
    navController: NavController,
    viewModel: SearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var showCode by remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)
    
    // Handle effects
    LaunchedEffect(effect) {
        effect?.let { currentEffect ->
            when (currentEffect) {
                is SearchEffect.ShowToast -> {
                    // In a real app, you would show a toast here
                    println("Toast: ${currentEffect.message}")
                }
                is SearchEffect.NavigateToDetail -> {
                    // In a real app, you would navigate to detail screen
                    println("Navigate to item ${currentEffect.itemId}")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MVI Pattern Tutorial") },
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
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // MVI Explanation
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
                            text = "What is MVI Pattern?",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Key Concepts
                        Text(
                            text = "Key Components:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Model: Represents the state of the UI\n" +
                                  "• View: The UI that displays the state\n" +
                                  "• Intent: User actions that trigger state changes\n" +
                                  "• Effect: Side effects like navigation or toasts\n" +
                                  "• Unidirectional Flow: Data flows in one direction",
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
                            text = "• Predictable State: State changes are explicit and traceable\n" +
                                  "• Testable: Easy to test each component in isolation\n" +
                                  "• Maintainable: Clear separation of concerns\n" +
                                  "• Debuggable: State changes are easy to track\n" +
                                  "• Scalable: Pattern works well for complex apps",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Flow
                        Text(
                            text = "Data Flow:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "1. User performs an action (Intent)\n" +
                                  "2. ViewModel processes the Intent\n" +
                                  "3. State is updated based on the Intent\n" +
                                  "4. UI updates to reflect the new State\n" +
                                  "5. Side effects are handled separately",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Search Example Header
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
                            text = "Practical Example: Search Feature",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "A real-world example showing MVI pattern with search functionality",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Search Bar
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
                        OutlinedTextField(
                            value = state.query,
                            onValueChange = { viewModel.processIntent(SearchIntent.Search(it)) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Search items...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, "Search")
                            },
                            trailingIcon = {
                                if (state.query.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.processIntent(SearchIntent.ClearSearch) }) {
                                        Icon(Icons.Default.Clear, "Clear")
                                    }
                                }
                            },
                            singleLine = true
                        )
                    }
                }
            }

            // Loading State
            if (state.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            // Error State
            state.error?.let { error ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            // Search Results
            items(state.items) { item ->
                SearchItemCard(
                    item = item,
                    onClick = { viewModel.processIntent(SearchIntent.SelectItem(item)) }
                )
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

                            // MVI Components
                            Text(
                                text = "1. MVI Components:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = """
                                    // Intent - User actions
                                    sealed class SearchIntent {
                                        data class Search(val query: String) : SearchIntent()
                                        data class SelectItem(val item: SearchItem) : SearchIntent()
                                        object ClearSearch : SearchIntent()
                                    }

                                    // State - UI state
                                    data class SearchState(
                                        val query: String = "",
                                        val items: List<SearchItem> = emptyList(),
                                        val selectedItem: SearchItem? = null,
                                        val isLoading: Boolean = false,
                                        val error: String? = null
                                    )

                                    // Effect - Side effects
                                    sealed class SearchEffect {
                                        data class ShowToast(val message: String) : SearchEffect()
                                        data class NavigateToDetail(val itemId: Int) : SearchEffect()
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
                                    class SearchViewModel : ViewModel() {
                                        private val _state = MutableStateFlow(SearchState())
                                        val state: StateFlow<SearchState> = _state.asStateFlow()

                                        private val _effect = MutableSharedFlow<SearchEffect>()
                                        val effect: SharedFlow<SearchEffect> = _effect.asSharedFlow()

                                        fun processIntent(intent: SearchIntent) {
                                            when (intent) {
                                                is SearchIntent.Search -> {
                                                    viewModelScope.launch {
                                                        _state.value = _state.value.copy(
                                                            query = intent.query,
                                                            isLoading = true
                                                        )
                                                        // Process search...
                                                    }
                                                }
                                                is SearchIntent.SelectItem -> {
                                                    viewModelScope.launch {
                                                        _effect.emit(
                                                            SearchEffect.NavigateToDetail(
                                                                intent.item.id
                                                            )
                                                        )
                                                    }
                                                }
                                                // Handle other intents...
                                            }
                                        }
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
                                    fun SearchScreen(
                                        viewModel: SearchViewModel = viewModel()
                                    ) {
                                        val state by viewModel.state
                                            .collectAsStateWithLifecycle()
                                        val effect by viewModel.effect
                                            .collectAsStateWithLifecycle()

                                        // Handle effects
                                        LaunchedEffect(effect) {
                                            effect?.let { currentEffect ->
                                                when (currentEffect) {
                                                    is SearchEffect.ShowToast -> {
                                                        // Show toast
                                                    }
                                                    is SearchEffect.NavigateToDetail -> {
                                                        // Navigate
                                                    }
                                                }
                                            }
                                        }

                                        // UI implementation using state
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchItemCard(
    item: SearchItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
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
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text(item.category) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Category,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                Text(
                    text = "Updated: ${item.lastUpdated.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 