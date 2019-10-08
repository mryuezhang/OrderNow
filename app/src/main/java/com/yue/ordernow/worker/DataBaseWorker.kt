package com.yue.ordernow.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yue.ordernow.R
import com.yue.ordernow.data.AppDatabase
import com.yue.ordernow.data.MenuItem
import kotlinx.coroutines.coroutineScope
import java.io.BufferedReader
import java.io.InputStreamReader

class DataBaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result = coroutineScope {
        try {
            val database = AppDatabase.getInstance(applicationContext)
            database.menuItemDao().insertAll(getMenuItemsFromFile())
            Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error database", ex)
            Result.failure()
        }
    }

    private fun getMenuItemsFromFile(): ArrayList<MenuItem> {

        val items = ArrayList<MenuItem>()
        val reader =
            BufferedReader(InputStreamReader(applicationContext.resources.openRawResource(R.raw.menu_items)))
        var line = reader.readLine()
        var tokens: List<String>

        while (line != null) {
            tokens = line.split(",")

            try {
                items.add(MenuItem(tokens[0], tokens[1].toFloat()))
            } catch (e: NumberFormatException) {
                items.add(MenuItem(tokens[0], 0.0F))
            }

            line = reader.readLine()
        }

        return items
    }

    companion object {
        private val TAG = DataBaseWorker::class.java.simpleName
    }

}