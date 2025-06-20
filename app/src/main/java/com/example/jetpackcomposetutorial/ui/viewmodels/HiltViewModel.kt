package com.example.jetpackcomposetutorial.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class HiltViewModel : ViewModel() {
    private val _userData = MutableStateFlow<List<com.example.jetpackcomposetutorial.data.User>>(emptyList())
    val userData: StateFlow<List<com.example.jetpackcomposetutorial.data.User>> = _userData.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    private var nextId = 1
    
    fun addUser(name: String, email: String) {
        _isLoading.value = true
        try {
            val newUser = com.example.jetpackcomposetutorial.data.User(
                id = nextId++,
                name = name,
                email = email,
                createdAt = System.currentTimeMillis()
            )
            val currentUsers = _userData.value.toMutableList()
            currentUsers.add(newUser)
            _userData.value = currentUsers
            _message.value = "User added successfully!"
        } catch (e: Exception) {
            _message.value = "Error adding user: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
    
    fun deleteUser(userId: Int) {
        _isLoading.value = true
        try {
            val currentUsers = _userData.value.toMutableList()
            currentUsers.removeAll { it.id == userId }
            _userData.value = currentUsers
            _message.value = "User deleted successfully!"
        } catch (e: Exception) {
            _message.value = "Error deleting user: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
    
    fun clearMessage() {
        _message.value = null
    }
} 