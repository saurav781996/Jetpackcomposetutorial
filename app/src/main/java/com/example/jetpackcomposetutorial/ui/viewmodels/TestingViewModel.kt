package com.example.jetpackcomposetutorial.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class TestingViewModel : ViewModel() {
    private val _testResults = MutableStateFlow<List<com.example.jetpackcomposetutorial.data.TestResult>>(emptyList())
    val testResults: StateFlow<List<com.example.jetpackcomposetutorial.data.TestResult>> = _testResults.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    private var nextId = 1
    
    fun runMockkTests() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Simulate running Mockk tests
                delay(1000)
                
                val mockkTests = listOf(
                    com.example.jetpackcomposetutorial.data.TestResult(
                        id = nextId++,
                        name = "UserRepository.getUser()",
                        description = "Test user retrieval with Mockk",
                        status = "PASSED",
                        framework = "Mockk",
                        duration = 45
                    ),
                    com.example.jetpackcomposetutorial.data.TestResult(
                        id = nextId++,
                        name = "UserService.createUser()",
                        description = "Test user creation with Mockk",
                        status = "PASSED",
                        framework = "Mockk",
                        duration = 32
                    ),
                    com.example.jetpackcomposetutorial.data.TestResult(
                        id = nextId++,
                        name = "UserViewModel.loadUsers()",
                        description = "Test ViewModel with Mockk",
                        status = "PASSED",
                        framework = "Mockk",
                        duration = 67
                    )
                )
                
                val currentResults = _testResults.value.toMutableList()
                currentResults.addAll(mockkTests)
                _testResults.value = currentResults
                _message.value = "Mockk tests completed successfully!"
            } catch (e: Exception) {
                _message.value = "Error running Mockk tests: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun runMockitoTests() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Simulate running Mockito tests
                delay(1200)
                
                val mockitoTests = listOf(
                    com.example.jetpackcomposetutorial.data.TestResult(
                        id = nextId++,
                        name = "UserRepository.getUser()",
                        description = "Test user retrieval with Mockito",
                        status = "PASSED",
                        framework = "Mockito",
                        duration = 52
                    ),
                    com.example.jetpackcomposetutorial.data.TestResult(
                        id = nextId++,
                        name = "UserService.createUser()",
                        description = "Test user creation with Mockito",
                        status = "FAILED",
                        framework = "Mockito",
                        duration = 28
                    ),
                    com.example.jetpackcomposetutorial.data.TestResult(
                        id = nextId++,
                        name = "UserViewModel.loadUsers()",
                        description = "Test ViewModel with Mockito",
                        status = "PASSED",
                        framework = "Mockito",
                        duration = 71
                    )
                )
                
                val currentResults = _testResults.value.toMutableList()
                currentResults.addAll(mockitoTests)
                _testResults.value = currentResults
                _message.value = "Mockito tests completed!"
            } catch (e: Exception) {
                _message.value = "Error running Mockito tests: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun addCustomTest(name: String, description: String, framework: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                delay(500) // Simulate test execution
                
                val customTest = com.example.jetpackcomposetutorial.data.TestResult(
                    id = nextId++,
                    name = name,
                    description = description.ifBlank { "Custom test case" },
                    status = listOf("PASSED", "FAILED").random(),
                    framework = framework,
                    duration = (20..100).random()
                )
                
                val currentResults = _testResults.value.toMutableList()
                currentResults.add(customTest)
                _testResults.value = currentResults
                _message.value = "Custom test added successfully!"
            } catch (e: Exception) {
                _message.value = "Error adding custom test: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteTestResult(testId: Int) {
        val currentResults = _testResults.value.toMutableList()
        currentResults.removeAll { it.id == testId }
        _testResults.value = currentResults
        _message.value = "Test result deleted successfully!"
    }
    
    fun clearMessage() {
        _message.value = null
    }
} 