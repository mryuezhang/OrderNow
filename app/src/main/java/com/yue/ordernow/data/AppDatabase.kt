package com.yue.ordernow.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.yue.ordernow.utils.DATABASE_NAME
import com.yue.ordernow.worker.DataBaseWorker

@Database(entities = [MenuItem::class, Order::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao
    abstract fun orderDao(): OrderDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<DataBaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }
    }
}