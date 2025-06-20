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
import com.example.jetpackcomposetutorial.ui.viewmodels.PagingViewModel
import com.example.jetpackcomposetutorial.data.Post
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagingTutorialScreen(navController: NavController) {
    val viewModel: PagingViewModel = viewModel()
    val posts by viewModel.posts.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var showCodeExamples by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    
    var postTitle by remember { mutableStateOf("") }
    var postContent by remember { mutableStateOf("") }
    
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
                        "Paging Library",
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
                Icon(Icons.Default.Add, contentDescription = "Add Post")
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
                            text = "What is Paging Library?",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "The Paging Library helps you load and display large datasets efficiently. It works seamlessly with Jetpack Compose to provide smooth scrolling and optimal memory usage. Paging handles data loading, caching, and UI updates automatically, making it perfect for displaying large lists of data like social media feeds, search results, or any paginated content.",
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
                                text = "Paging Implementation:",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                                        append("// PagingSource\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("class ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("PostPagingSource : PagingSource<Int, Post>() {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("    override suspend fun ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("load(params: LoadParams<Int>): LoadResult<Int, Post> {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("        return try {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("            val posts = repository.getPosts(params.key ?: 0)\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("            LoadResult.Page(\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("                data = posts,\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("                prevKey = null,\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("                nextKey = posts.size + 1\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("            )\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("        } catch (e: Exception) {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("            LoadResult.Error(e)\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("        }\n"
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
                                        append("// Compose Paging\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFF4EC9B0))) {
                                        append("val posts = ")
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("postsFlow.collectAsLazyPagingItems()\n\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("LazyColumn {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    items(posts) { post ->\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("        PostCard(post)\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    }\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    posts.apply {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("        when {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("            loadState.refresh is LoadState.Loading -> {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("                LoadingItem()\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("            }\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("            loadState.append is LoadState.Loading -> {\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFDCDCAA))) {
                                        append("                LoadingItem()\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("            }\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("        }\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
                                        append("    }\n"
                                        )
                                    }
                                    withStyle(SpanStyle(color = Color(0xFFD7BA7D))) {
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
                                text = "Posts Feed Demo",
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
                        
                        // Posts display
                        if (posts.isNotEmpty()) {
                            posts.forEach { post ->
                                PostCard(
                                    post = post,
                                    onDelete = {
                                        viewModel.deletePost(post.id)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            
                            // Load more button (simulating pagination)
                            Button(
                                onClick = { viewModel.loadMorePosts() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Load More Posts")
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
                                        imageVector = Icons.Default.Article,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No posts yet",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Tap the + button to add your first post",
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
                            text = "Paging Benefits:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val benefits = listOf(
                            "• Memory Efficiency: Load only visible data",
                            "• Smooth Scrolling: Optimized performance",
                            "• Network Optimization: Reduce API calls",
                            "• Caching: Automatic data caching",
                            "• Error Handling: Built-in error states",
                            "• Compose Integration: Native Compose support"
                        )
                        
                        benefits.forEach { benefit ->
                            Text(
                                text = benefit,
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

    // Add Post Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Post") },
            text = {
                Column {
                    OutlinedTextField(
                        value = postTitle,
                        onValueChange = { postTitle = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter post title") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = postContent,
                        onValueChange = { postContent = it },
                        label = { Text("Content") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        placeholder = { Text("Enter post content") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (postTitle.isNotBlank() && postContent.isNotBlank()) {
                            viewModel.addPost(postTitle, postContent)
                            postTitle = ""
                            postContent = ""
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
fun PostCard(
    post: Post,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = post.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Post #${post.id} • ${dateFormat.format(Date(post.timestamp))}",
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
} 