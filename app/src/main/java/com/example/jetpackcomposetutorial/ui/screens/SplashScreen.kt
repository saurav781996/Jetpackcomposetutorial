package com.example.jetpackcomposetutorial.ui.screens


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.delay
import kotlin.random.Random
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.FastOutSlowInEasing

data class MenuIcon(
    val icon: ImageVector,
    val title: String,
    val delay: Int
)

@Composable
fun SplashScreen(navController: NavController) {
    // Hide system UI (status bar and navigation bar)
    val view = LocalView.current
    DisposableEffect(view) {
        val window = (view.context as android.app.Activity).window
        val windowInsetsController = WindowCompat.getInsetsController(window, view)
        
        // Hide both the status bar and the navigation bar
        windowInsetsController.apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            // Show system bars when leaving the splash screen
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    // Menu icons with their titles and animation delays
    val menuIcons = listOf(
        MenuIcon(Icons.Default.Work, "WorkManager", 0),
        MenuIcon(Icons.Default.DataArray, "LiveData", 200),
        MenuIcon(Icons.Default.Storage, "ViewModel", 400),
        MenuIcon(Icons.Default.CallSplit, "MVI Pattern", 600),
        MenuIcon(Icons.Default.Storage, "Room Database", 800),
        MenuIcon(Icons.Default.Settings, "DataStore", 1000),
        MenuIcon(Icons.Default.Navigation, "Navigation", 1200),
        MenuIcon(Icons.Default.Build, "Hilt", 1400),
        MenuIcon(Icons.Default.BugReport, "Testing", 1600),
        MenuIcon(Icons.Default.List, "Paging Library", 1800),
        MenuIcon(Icons.Default.PlayArrow, "CoroutineWorker", 2000)
    )

    var showIcons by remember { mutableStateOf(false) }
    var hideIcons by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var titleComplete by remember { mutableStateOf(false) }
    
    // Title animation states
    var jetpackVisible by remember { mutableStateOf(false) }
    var composeVisible by remember { mutableStateOf(false) }
    var tutorialVisible by remember { mutableStateOf(false) }

    // Animation for icons appearing
    val iconScale by animateFloatAsState(
        targetValue = if (showIcons && !hideIcons) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "Icon Scale"
    )

    // Animation for icons disappearing
    val iconAlpha by animateFloatAsState(
        targetValue = if (hideIcons) 0f else 1f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "Icon Alpha"
    )

    // Title animations
    val jetpackOffsetY by animateFloatAsState(
        targetValue = if (jetpackVisible) 0f else -100f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "Jetpack Slide"
    )

    val composeOffsetY by animateFloatAsState(
        targetValue = if (composeVisible) 0f else 100f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "Compose Slide"
    )

    val tutorialAlpha by animateFloatAsState(
        targetValue = if (tutorialVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "Tutorial Fade"
    )

    val titleScale by animateFloatAsState(
        targetValue = if (titleComplete) 1f else 0.8f,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "Title Scale"
    )

    val splashBgColor = Color(0xFFE9EAF3)
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
    val iconSizePx = with(density) { 48.dp.toPx() }
    val paddingPx = with(density) { 24.dp.toPx() }
    val minSpacingPx = with(density) { 20.dp.toPx() }
    val iconPositions = remember { mutableStateListOf<Pair<Float, Float>>() }

    // Launch animation sequence
    LaunchedEffect(key1 = true) {
        // Start showing icons
        showIcons = true
        delay(3000L) // Show icons for 3 seconds
        
        // Hide icons
        hideIcons = true
        delay(800L) // Wait for icons to fade out
        
        // Show title
        showTitle = true
        delay(300L)
        
        // Animate title parts
        jetpackVisible = true
        delay(400L)
        composeVisible = true
        delay(400L)
        tutorialVisible = true
        delay(600L)
        
        // Complete title animation
        titleComplete = true
        delay(1000L) // Show final state
        
        // Navigate to dashboard
        navController.navigate("dashboard") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(splashBgColor)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        // Animated menu icons scattered around the screen
        if (showIcons) {
            iconPositions.clear()
            menuIcons.forEachIndexed { index, menuIcon ->
                // Find a random position with at least 20dp spacing from all previous icons
                val position = remember {
                    var tryCount = 0
                    var pos: Pair<Float, Float>
                    do {
                        val x = Random.nextFloat() * (screenWidthPx - 2 * paddingPx - iconSizePx) + paddingPx
                        val y = Random.nextFloat() * (screenHeightPx - 2 * paddingPx - iconSizePx) + paddingPx
                        pos = x to y
                        tryCount++
                    } while (iconPositions.any { (px, py) ->
                        val dx = px - pos.first
                        val dy = py - pos.second
                        val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                        dist < iconSizePx + minSpacingPx
                    } && tryCount < 20)
                    iconPositions.add(pos)
                    pos
                }
                val (randomX, randomY) = position
                val randomDelay = remember { Random.nextInt(0, 500) }
                var iconVisible by remember { mutableStateOf(false) }
                LaunchedEffect(showIcons) {
                    delay(menuIcon.delay.toLong() + randomDelay.toLong())
                    iconVisible = true
                }
                val individualIconScale by animateFloatAsState(
                    targetValue = if (iconVisible) 1f else 0f,
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = FastOutSlowInEasing
                    ),
                    label = "Individual Icon Scale"
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopStart
                ) {
                    Surface(
                        modifier = Modifier
                            .absoluteOffset(
                                x = with(density) { randomX.toDp() },
                                y = with(density) { randomY.toDp() }
                            )
                            .scale(individualIconScale * iconScale)
                            .alpha(iconAlpha),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = menuIcon.icon,
                                contentDescription = menuIcon.title,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        // Title animation
        if (showTitle) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .scale(titleScale)
                    .padding(16.dp)
            ) {
                // Jetpack text sliding from top
                Text(
                    text = "Jetpack",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .offset(y = jetpackOffsetY.dp)
                )

                // Compose text sliding from bottom
                Text(
                    text = "Compose",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .offset(y = composeOffsetY.dp)
                )

                // Tutorial text fading in
                Text(
                    text = "Tutorial",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .alpha(tutorialAlpha)
                )
            }
        }

        // Add after the title animation in the Box
        if (titleComplete) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = "Made by Saurav",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
} 