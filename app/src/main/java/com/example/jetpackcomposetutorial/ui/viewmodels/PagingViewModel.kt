package com.example.jetpackcomposetutorial.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class PagingViewModel : ViewModel() {
    private val _posts = MutableStateFlow<List<com.example.jetpackcomposetutorial.data.Post>>(emptyList())
    val posts: StateFlow<List<com.example.jetpackcomposetutorial.data.Post>> = _posts.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    private var nextId = 1
    private var currentPage = 0
    private val pageSize = 10
    
    init {
        loadInitialPosts()
    }
    
    private fun loadInitialPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                delay(500) // Simulate network delay
                
                val initialPosts = generatePosts(0, pageSize)
                _posts.value = initialPosts
                currentPage = 1
                _message.value = "Initial posts loaded successfully!"
            } catch (e: Exception) {
                _message.value = "Error loading posts: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadMorePosts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                delay(800) // Simulate network delay
                
                val morePosts = generatePosts(currentPage * pageSize, pageSize)
                val currentPosts = _posts.value.toMutableList()
                currentPosts.addAll(morePosts)
                _posts.value = currentPosts
                currentPage++
                _message.value = "Loaded ${morePosts.size} more posts!"
            } catch (e: Exception) {
                _message.value = "Error loading more posts: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun addPost(title: String, content: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                delay(300) // Simulate network delay
                
                val newPost = com.example.jetpackcomposetutorial.data.Post(
                    id = nextId++,
                    title = title,
                    content = content,
                    timestamp = System.currentTimeMillis()
                )
                
                val currentPosts = _posts.value.toMutableList()
                currentPosts.add(0, newPost) // Add to beginning
                _posts.value = currentPosts
                _message.value = "Post added successfully!"
            } catch (e: Exception) {
                _message.value = "Error adding post: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deletePost(postId: Int) {
        val currentPosts = _posts.value.toMutableList()
        currentPosts.removeAll { it.id == postId }
        _posts.value = currentPosts
        _message.value = "Post deleted successfully!"
    }
    
    private fun generatePosts(startIndex: Int, count: Int): List<com.example.jetpackcomposetutorial.data.Post> {
        val sampleTitles = listOf(
            "Getting Started with Jetpack Compose",
            "Understanding State Management",
            "Building Responsive UIs",
            "Working with Navigation",
            "Implementing Material Design",
            "Testing Compose Components",
            "Performance Optimization Tips",
            "Best Practices for Compose",
            "Integrating with Existing Views",
            "Deploying Compose Apps"
        )
        
        val sampleContents = listOf(
            "Jetpack Compose is a modern toolkit for building native Android UI. It simplifies and accelerates UI development on Android with less code, powerful tools, and intuitive Kotlin APIs.",
            "State management is crucial in Compose. Learn how to use remember, mutableStateOf, and derivedStateOf to manage your app's state effectively.",
            "Create responsive UIs that adapt to different screen sizes and orientations. Use BoxWithConstraints and other layout components.",
            "Navigation in Compose is straightforward with the Navigation Compose library. Set up navigation graphs and handle deep links easily.",
            "Material Design 3 is fully supported in Compose. Use MaterialTheme and Material components to create beautiful, consistent UIs.",
            "Testing Compose components is essential for maintaining code quality. Use Compose testing APIs to verify your UI behavior.",
            "Performance is key in mobile apps. Learn techniques to optimize your Compose UI for smooth scrolling and fast rendering.",
            "Follow best practices to write maintainable Compose code. Use proper architecture patterns and avoid common pitfalls.",
            "Compose can work alongside existing View-based UIs. Learn how to integrate Compose into your existing Android app.",
            "Deploying Compose apps requires understanding the build process and optimization techniques for production releases."
        )
        
        return (0 until count).map { index ->
            val actualIndex = startIndex + index
            val titleIndex = actualIndex % sampleTitles.size
            val contentIndex = actualIndex % sampleContents.size
            
            com.example.jetpackcomposetutorial.data.Post(
                id = nextId++,
                title = "${sampleTitles[titleIndex]} #${actualIndex + 1}",
                content = sampleContents[contentIndex],
                timestamp = System.currentTimeMillis() - (actualIndex * 60000) // Stagger timestamps
            )
        }
    }
    
    fun clearMessage() {
        _message.value = null
    }
} 