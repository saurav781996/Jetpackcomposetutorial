package com.example.jetpackcomposetutorial.data

data class BackgroundTask(
    val id: Int,
    val name: String,
    val duration: Int,
    val status: String,
    val progress: Int,
    val startTime: Long
) 