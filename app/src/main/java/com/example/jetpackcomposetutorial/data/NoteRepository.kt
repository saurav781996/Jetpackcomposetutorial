package com.example.jetpackcomposetutorial.data

import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()
    
    suspend fun insert(note: Note) {
        noteDao.insertNote(note)
    }
    
    suspend fun update(note: Note) {
        noteDao.updateNote(note)
    }
    
    suspend fun delete(note: Note) {
        noteDao.deleteNote(note)
    }
    
    suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }
} 