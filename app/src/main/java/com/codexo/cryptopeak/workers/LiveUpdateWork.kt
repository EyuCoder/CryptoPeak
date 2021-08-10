package com.codexo.cryptopeak.workers

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.codexo.cryptopeak.database.CoinDatabase
import com.codexo.cryptopeak.repository.Repository
import kotlinx.coroutines.delay
import retrofit2.HttpException

class LiveUpdateWork(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val database = CoinDatabase.getDatabase(applicationContext)
        val repository = Repository(database)

        return try {
            repository.refreshCoin()
            Log.d(LIVE_UPDATE_WORK_NAME, "refreshed")
            Toast.makeText(applicationContext, "UPDATED", Toast.LENGTH_SHORT).show()
            Result.success()
        } catch (e: HttpException) {
            Log.d(LIVE_UPDATE_WORK_NAME, "failed")
            Result.failure()
        }
    }

    companion object {
        const val LIVE_UPDATE_WORK_NAME = "RefreshDataWorker"
    }
}