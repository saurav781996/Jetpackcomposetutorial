package com.example.jetpackcomposetutorial.data

data class TestResult(
    val id: Int,
    val name: String,
    val description: String,
    val status: String, // PASSED, FAILED, RUNNING
    val framework: String, // Mockk, Mockito
    val duration: Int // milliseconds
) 