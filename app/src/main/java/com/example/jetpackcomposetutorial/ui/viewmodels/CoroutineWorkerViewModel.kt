package com.example.jetpackcomposetutorial.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class CoroutineWorkerViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<com.example.jetpackcomposetutorial.data.BackgroundTask>>(emptyList())
    val tasks: StateFlow<List<com.example.jetpackcomposetutorial.data.BackgroundTask>> = _tasks.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    private var nextId = 1
    private val runningTasks = mutableMapOf<Int, kotlinx.coroutines.Job>()
    
    fun startTask(taskName: String, durationSeconds: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val task = com.example.jetpackcomposetutorial.data.BackgroundTask(
                    id = nextId++,
                    name = taskName,
                    duration = durationSeconds,
                    status = "Running",
                    progress = 0,
                    startTime = System.currentTimeMillis()
                )
                
                val currentTasks = _tasks.value.toMutableList()
                currentTasks.add(task)
                _tasks.value = currentTasks
                
                _message.value = "Task '${taskName}' started successfully!"
                
                // Simulate background work
                val job = viewModelScope.launch {
                    try {
                        val totalSteps = durationSeconds * 2 // Update progress every 500ms
                        repeat(totalSteps) { step ->
                            delay(500) // Simulate work
                            
                            val progress = ((step + 1) * 100) / totalSteps
                            updateTaskProgress(task.id, progress)
                        }
                        
                        // Mark as completed
                        updateTaskStatus(task.id, "Completed", 100)
                        _message.value = "Task '${taskName}' completed successfully!"
                        
                    } catch (e: Exception) {
                        updateTaskStatus(task.id, "Failed", 0)
                        _message.value = "Task '${taskName}' failed: ${e.message}"
                    } finally {
                        runningTasks.remove(task.id)
                    }
                }
                
                runningTasks[task.id] = job
                
            } catch (e: Exception) {
                _message.value = "Error starting task: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun cancelTask(taskId: Int) {
        viewModelScope.launch {
            try {
                runningTasks[taskId]?.cancel()
                runningTasks.remove(taskId)
                
                updateTaskStatus(taskId, "Cancelled", 0)
                _message.value = "Task cancelled successfully!"
                
            } catch (e: Exception) {
                _message.value = "Error cancelling task: ${e.message}"
            }
        }
    }
    
    private fun updateTaskProgress(taskId: Int, progress: Int) {
        val currentTasks = _tasks.value.toMutableList()
        val taskIndex = currentTasks.indexOfFirst { it.id == taskId }
        if (taskIndex != -1) {
            currentTasks[taskIndex] = currentTasks[taskIndex].copy(progress = progress)
            _tasks.value = currentTasks
        }
    }
    
    private fun updateTaskStatus(taskId: Int, status: String, progress: Int) {
        val currentTasks = _tasks.value.toMutableList()
        val taskIndex = currentTasks.indexOfFirst { it.id == taskId }
        if (taskIndex != -1) {
            currentTasks[taskIndex] = currentTasks[taskIndex].copy(
                status = status,
                progress = progress
            )
            _tasks.value = currentTasks
        }
    }
    
    fun clearMessage() {
        _message.value = null
    }
    
    override fun onCleared() {
        super.onCleared()
        // Cancel all running tasks when ViewModel is cleared
        runningTasks.values.forEach { it.cancel() }
        runningTasks.clear()
    }
} 