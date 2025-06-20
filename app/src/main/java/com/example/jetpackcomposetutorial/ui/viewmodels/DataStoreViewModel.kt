package com.example.jetpackcomposetutorial.ui.viewmodels

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val Application.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreViewModel(application: Application) : AndroidViewModel(application) {
    private val _settings = MutableStateFlow<Map<String, String>>(emptyMap())
    val settings: StateFlow<Map<String, String>> = _settings.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            try {
                getApplication<Application>().dataStore.data.collect { preferences ->
                    val settingsMap = mutableMapOf<String, String>()
                    preferences.asMap().forEach { (key, value) ->
                        settingsMap[key.name] = when (value) {
                            is String -> value
                            is Int -> value.toString()
                            is Boolean -> value.toString()
                            is Long -> value.toString()
                            is Float -> value.toString()
                            is Double -> value.toString()
                            else -> value.toString()
                        }
                    }
                    _settings.value = settingsMap
                }
            } catch (e: Exception) {
                _message.value = "Error loading settings: ${e.message}"
            }
        }
    }
    
    fun saveSetting(key: String, value: String) {
        viewModelScope.launch {
            try {
                getApplication<Application>().dataStore.edit { preferences ->
                    // Try to determine the type and save accordingly
                    when {
                        isBoolean(value) -> {
                            preferences[booleanPreferencesKey(key)] = value.toBoolean()
                        }
                        value.toIntOrNull() != null -> {
                            preferences[intPreferencesKey(key)] = value.toInt()
                        }
                        value.toLongOrNull() != null -> {
                            preferences[longPreferencesKey(key)] = value.toLong()
                        }
                        value.toFloatOrNull() != null -> {
                            preferences[floatPreferencesKey(key)] = value.toFloat()
                        }
                        value.toDoubleOrNull() != null -> {
                            preferences[doublePreferencesKey(key)] = value.toDouble()
                        }
                        else -> {
                            preferences[stringPreferencesKey(key)] = value
                        }
                    }
                }
                _message.value = "Setting saved successfully!"
            } catch (e: Exception) {
                _message.value = "Error saving setting: ${e.message}"
            }
        }
    }
    
    private fun isBoolean(value: String): Boolean {
        return value.equals("true", ignoreCase = true) || value.equals("false", ignoreCase = true)
    }
    
    fun deleteSetting(key: String) {
        viewModelScope.launch {
            try {
                getApplication<Application>().dataStore.edit { preferences ->
                    preferences.remove(stringPreferencesKey(key))
                    preferences.remove(intPreferencesKey(key))
                    preferences.remove(booleanPreferencesKey(key))
                    preferences.remove(longPreferencesKey(key))
                    preferences.remove(floatPreferencesKey(key))
                    preferences.remove(doublePreferencesKey(key))
                }
                _message.value = "Setting deleted successfully!"
            } catch (e: Exception) {
                _message.value = "Error deleting setting: ${e.message}"
            }
        }
    }
    
    fun clearMessage() {
        _message.value = null
    }
} 