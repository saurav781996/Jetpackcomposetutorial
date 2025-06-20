package com.example.jetpackcomposetutorial.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {
    private val _currentRoute = MutableStateFlow("navigation_tutorial")
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()
    
    private val _navigationHistory = MutableStateFlow<List<String>>(emptyList())
    val navigationHistory: StateFlow<List<String>> = _navigationHistory.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    fun navigateTo(route: String) {
        _currentRoute.value = route
        addToHistory(route)
        _message.value = "Navigated to: $route"
    }
    
    private fun addToHistory(route: String) {
        val currentHistory = _navigationHistory.value.toMutableList()
        if (!currentHistory.contains(route)) {
            currentHistory.add(route)
            if (currentHistory.size > 10) {
                currentHistory.removeAt(0)
            }
            _navigationHistory.value = currentHistory
        }
    }
    
    fun clearHistory() {
        _navigationHistory.value = emptyList()
        _message.value = "Navigation history cleared"
    }
    
    fun clearMessage() {
        _message.value = null
    }
} 