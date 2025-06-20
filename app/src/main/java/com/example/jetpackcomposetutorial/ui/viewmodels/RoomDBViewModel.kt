package com.example.jetpackcomposetutorial.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposetutorial.data.AppDatabase
import com.example.jetpackcomposetutorial.data.Note
import com.example.jetpackcomposetutorial.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoomDBViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = NoteRepository(database.noteDao())
        loadNotes()
    }
    
    private fun loadNotes() {
        viewModelScope.launch {
            try {
                repository.allNotes.collect { notesList ->
                    _notes.value = notesList
                }
            } catch (e: Exception) {
                _message.value = "Error loading notes: ${e.message}"
            }
        }
    }
    
    fun addNote(title: String, content: String) {
        if (title.isBlank() && content.isBlank()) {
            _message.value = "Please enter a title or content"
            return
        }
        
        viewModelScope.launch {
            try {
                val note = Note(
                    title = title.ifBlank { "Untitled" },
                    content = content.ifBlank { "No content" }
                )
                repository.insert(note)
                _message.value = "Note added successfully!"
            } catch (e: Exception) {
                _message.value = "Error adding note: ${e.message}"
            }
        }
    }
    
    fun updateNote(note: Note) {
        viewModelScope.launch {
            try {
                repository.update(note)
                _message.value = "Note updated successfully!"
            } catch (e: Exception) {
                _message.value = "Error updating note: ${e.message}"
            }
        }
    }
    
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                repository.delete(note)
                _message.value = "Note deleted successfully!"
            } catch (e: Exception) {
                _message.value = "Error deleting note: ${e.message}"
            }
        }
    }
    
    fun clearMessage() {
        _message.value = null
    }
} 