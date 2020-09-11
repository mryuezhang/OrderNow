package com.yue.ordernow.data

import androidx.lifecycle.LiveData
import com.yue.ordernow.utilities.getDayStart
import com.yue.ordernow.utilities.getMonthStart
import com.yue.ordernow.utilities.getWeekStart
import java.util.*

class SaleSummaryRepository private constructor(private val saleSummaryDao: SaleSummaryDao) {

    fun getAll(): LiveData<List<SaleSummary>> =
        saleSummaryDao.getSaleSummaries()

    fun getDailySaleSummary(time: Calendar): LiveData<SaleSummary> =
        saleSummaryDao.getDailySaleSummary(time)

    fun getWeeklySaleSummary(time: Calendar): LiveData<SaleSummary> =
        saleSummaryDao.getWeeklySaleSummary(time)

    fun getMonthlySaleSummary(time: Calendar): LiveData<SaleSummary> =
        saleSummaryDao.getMonthlySaleSummary(time)

    fun getAllRelatedSaleSummaries(time: Calendar): LiveData<List<SaleSummary>> =
        saleSummaryDao.getAllRelatedSaleSummaries(getDayStart(time),
            getWeekStart(time),
            getMonthStart(time))

    suspend fun insert(saleSummary: SaleSummary) {
        saleSummaryDao.insert(saleSummary)
    }

    suspend fun update(saleSummary: SaleSummary) {
        saleSummaryDao.update(saleSummary)
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: SaleSummaryRepository? = null

        fun getInstance(saleSummaryDao: SaleSummaryDao) =
            instance ?: synchronized(this) {
                instance ?: SaleSummaryRepository(saleSummaryDao).also { instance = it }
            }
    }
}