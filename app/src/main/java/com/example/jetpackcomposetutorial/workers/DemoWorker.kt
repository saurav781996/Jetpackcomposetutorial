package com.example.jetpackcomposetutorial.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class DemoWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Simulate a long-running task with progress updates
        for (progress in 0..100 step 10) {
            if (isStopped) {
                return Result.failure()
            }
            // Update progress
            setProgress(workDataOf("progress" to progress))
            // Simulate work
            delay(1000) // 1 second delay between progress updates
        }
        return Result.success()
    }
} 