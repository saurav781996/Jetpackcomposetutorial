package com.example.jetpackcomposetutorial.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

data class TripStatus(
    val currentStation: String,
    val nextStation: String,
    val timeToNextStation: Int,
    val isDelayed: Boolean,
    val delayMinutes: Int = 0,
    val passengerCount: Int
)

class LiveDataTutorialViewModel : ViewModel() {
    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter.asStateFlow()

    private val _randomNumber = MutableStateFlow(0)
    val randomNumber: StateFlow<Int> = _randomNumber.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _tripStatus = MutableStateFlow(
        TripStatus(
            currentStation = "Central Station",
            nextStation = "Downtown",
            timeToNextStation = 5,
            isDelayed = false,
            passengerCount = 120
        )
    )
    val tripStatus: StateFlow<TripStatus> = _tripStatus.asStateFlow()

    private val _isTripSimulating = MutableStateFlow(false)
    val isTripSimulating: StateFlow<Boolean> = _isTripSimulating.asStateFlow()

    private val stations = listOf(
        "Central Station",
        "Downtown",
        "University",
        "Shopping Mall",
        "Airport",
        "Beach Front",
        "Business District",
        "Residential Area"
    )

    fun incrementCounter() {
        _counter.value = _counter.value + 1
    }

    fun decrementCounter() {
        _counter.value = _counter.value - 1
    }

    fun startGeneratingNumbers() {
        if (_isGenerating.value) return
        _isGenerating.value = true
        viewModelScope.launch {
            while (_isGenerating.value) {
                _randomNumber.value = (0..100).random()
                delay(1000)
            }
        }
    }

    fun stopGeneratingNumbers() {
        _isGenerating.value = false
    }

    fun startTripSimulation() {
        if (_isTripSimulating.value) return
        _isTripSimulating.value = true
        viewModelScope.launch {
            var currentStationIndex = 0
            while (_isTripSimulating.value) {
                val currentStatus = _tripStatus.value
                val nextStationIndex = (currentStationIndex + 1) % stations.size
                
                val isDelayed = Random.nextFloat() < 0.2f
                val delayMinutes = if (isDelayed) Random.nextInt(1, 10) else 0
                
                _tripStatus.value = currentStatus.copy(
                    currentStation = stations[currentStationIndex],
                    nextStation = stations[nextStationIndex],
                    timeToNextStation = Random.nextInt(2, 8),
                    isDelayed = isDelayed,
                    delayMinutes = delayMinutes,
                    passengerCount = Random.nextInt(50, 200)
                )
                
                delay(5000)
                currentStationIndex = nextStationIndex
            }
        }
    }

    fun stopTripSimulation() {
        _isTripSimulating.value = false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveDataTutorialScreen(
    navController: NavController,
    viewModel: LiveDataTutorialViewModel = viewModel()
) {
    val counter by viewModel.counter.collectAsStateWithLifecycle()
    val randomNumber by viewModel.randomNumber.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGenerating.collectAsStateWithLifecycle()
    val tripStatus by viewModel.tripStatus.collectAsStateWithLifecycle()
    val isTripSimulating by viewModel.isTripSimulating.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var showCode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LiveData Tutorial") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showCode = !showCode }
                    ) {
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
            // Example 1 Header
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
                            text = "Example 1: Real-time Trip Commuter",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "A complex example showing real-time updates and state management",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Trip Commuter Example
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
                        // Current Station
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Current Station",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = tripStatus.currentStation,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Train,
                                contentDescription = "Train",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Next Station and Time
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Next Station",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = tripStatus.nextStation,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Time to Next",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${tripStatus.timeToNextStation} min",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (tripStatus.isDelayed) 
                                        MaterialTheme.colorScheme.error 
                                    else 
                                        MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        
                        if (tripStatus.isDelayed) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Delay",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Delayed by ${tripStatus.delayMinutes} minutes",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Passenger Count
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = "Passengers",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${tripStatus.passengerCount} passengers",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Control Button
                        Button(
                            onClick = {
                                if (isTripSimulating) {
                                    viewModel.stopTripSimulation()
                                } else {
                                    viewModel.startTripSimulation()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isTripSimulating) 
                                    MaterialTheme.colorScheme.error 
                                else 
                                    MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                if (isTripSimulating) Icons.Default.Stop else Icons.Default.PlayArrow,
                                if (isTripSimulating) "Stop" else "Start"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isTripSimulating) "Stop Trip" else "Start Trip")
                        }
                    }
                }
            }

            // Trip Example Explanation
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "How the Trip Commuter Works",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "1. Real-time Updates:\n" +
                                  "   • Trip status updates every 5 seconds\n" +
                                  "   • Simulates a train moving between stations\n" +
                                  "   • Shows real-time passenger count\n" +
                                  "   • Randomly generates delays\n\n" +
                                  "2. State Management:\n" +
                                  "   • Uses StateFlow for reactive updates\n" +
                                  "   • TripStatus data class holds all trip information\n" +
                                  "   • UI automatically updates when state changes\n\n" +
                                  "3. Features Demonstrated:\n" +
                                  "   • Real-time data updates\n" +
                                  "   • State preservation\n" +
                                  "   • Error states (delays)\n" +
                                  "   • Dynamic UI updates\n" +
                                  "   • Background processing\n\n" +
                                  "Try it out:\n" +
                                  "• Start the trip to see real-time updates\n" +
                                  "• Watch for random delays\n" +
                                  "• Observe passenger count changes\n" +
                                  "• Notice how the UI updates smoothly\n" +
                                  "• Try rotating the device to see state preservation",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Trip Code Example (Conditional)
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
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Data Class
                            Text(
                                text = "1. Data Class:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = """
                                    data class TripStatus(
                                        val currentStation: String,
                                        val nextStation: String,
                                        val timeToNextStation: Int,
                                        val isDelayed: Boolean,
                                        val delayMinutes: Int = 0,
                                        val passengerCount: Int
                                    )
                                """.trimIndent(),
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // ViewModel State
                            Text(
                                text = "2. ViewModel State:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = """
                                    class TripViewModel : ViewModel() {
                                        private val _tripStatus = MutableStateFlow(
                                            TripStatus(
                                                currentStation = "Central Station",
                                                nextStation = "Downtown",
                                                timeToNextStation = 5,
                                                isDelayed = false,
                                                passengerCount = 120
                                            )
                                        )
                                        val tripStatus: StateFlow<TripStatus> = 
                                            _tripStatus.asStateFlow()
                                        
                                        private val _isTripSimulating = 
                                            MutableStateFlow(false)
                                        val isTripSimulating: StateFlow<Boolean> = 
                                            _isTripSimulating.asStateFlow()
                                    }
                                """.trimIndent(),
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Update Function
                            Text(
                                text = "3. Update Function:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = """
                                    fun startTripSimulation() {
                                        if (_isTripSimulating.value) return
                                        _isTripSimulating.value = true
                                        viewModelScope.launch {
                                            while (_isTripSimulating.value) {
                                                // Update trip status
                                                _tripStatus.value = currentStatus.copy(
                                                    currentStation = stations[currentIndex],
                                                    nextStation = stations[nextIndex],
                                                    timeToNextStation = Random.nextInt(2, 8),
                                                    isDelayed = Random.nextFloat() < 0.2f,
                                                    delayMinutes = if (isDelayed) 
                                                        Random.nextInt(1, 10) else 0,
                                                    passengerCount = 
                                                        Random.nextInt(50, 200)
                                                )
                                                delay(5000) // 5 second delay
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
                                text = "4. UI Implementation:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = """
                                    @Composable
                                    fun TripCommuterScreen(
                                        viewModel: TripViewModel = viewModel()
                                    ) {
                                        val tripStatus by 
                                            viewModel.tripStatus.collectAsStateWithLifecycle()
                                        val isSimulating by 
                                            viewModel.isTripSimulating
                                                .collectAsStateWithLifecycle()
                                        
                                        // UI implementation using tripStatus
                                        // and isSimulating values
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

            // Visual Separator
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
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
                            text = "Example 2: Simple Counter",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "A basic example showing state management with a simple counter",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Counter Example
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
                            text = counter.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { viewModel.decrementCounter() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(Icons.Default.Remove, "Decrease")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Decrease")
                            }
                            Button(
                                onClick = { viewModel.incrementCounter() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(Icons.Default.Add, "Increase")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Increase")
                            }
                        }
                    }
                }
            }

            // Counter Code Example (Conditional)
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
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // ViewModel State
                            Text(
                                text = "1. ViewModel State:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = """
                                    class CounterViewModel : ViewModel() {
                                        private val _counter = MutableStateFlow(0)
                                        val counter: StateFlow<Int> = 
                                            _counter.asStateFlow()
                                        
                                        fun incrementCounter() {
                                            _counter.value = _counter.value + 1
                                        }
                                        
                                        fun decrementCounter() {
                                            _counter.value = _counter.value - 1
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
                                text = "2. UI Implementation:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = """
                                    @Composable
                                    fun CounterScreen(
                                        viewModel: CounterViewModel = viewModel()
                                    ) {
                                        val counter by 
                                            viewModel.counter.collectAsStateWithLifecycle()
                                        
                                        Column {
                                            Text(
                                                text = counter.toString(),
                                                style = MaterialTheme
                                                    .typography.displayLarge
                                            )
                                            Row {
                                                Button(
                                                    onClick = { 
                                                        viewModel.decrementCounter() 
                                                    }
                                                ) {
                                                    Text("Decrease")
                                                }
                                                Button(
                                                    onClick = { 
                                                        viewModel.incrementCounter() 
                                                    }
                                                ) {
                                                    Text("Increase")
                                                }
                                            }
                                        }
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

            // Random Number Generator Example
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
                            text = "Real-time Updates Example",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = randomNumber.toString(),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (isGenerating) {
                                    viewModel.stopGeneratingNumbers()
                                } else {
                                    viewModel.startGeneratingNumbers()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isGenerating) 
                                    MaterialTheme.colorScheme.error 
                                else 
                                    MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(
                                if (isGenerating) Icons.Default.Stop else Icons.Default.PlayArrow,
                                if (isGenerating) "Stop" else "Start"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isGenerating) "Stop Generation" else "Start Generation")
                        }
                    }
                }
            }

            // LiveData Benefits
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
                            text = "LiveData Benefits",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Lifecycle Awareness: Automatically handles lifecycle states\n" +
                                  "• No Memory Leaks: Observers are bound to lifecycle objects\n" +
                                  "• No Crashes: Stops updating if the observer's lifecycle is inactive\n" +
                                  "• Always Up-to-date: If the lifecycle becomes active again, it receives the latest data\n" +
                                  "• Proper Configuration Changes: Automatically handles configuration changes",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
} 