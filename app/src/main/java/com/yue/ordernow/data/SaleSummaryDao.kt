package com.yue.ordernow.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yue.ordernow.utilities.getDayStart
import com.yue.ordernow.utilities.getMonthStart
import com.yue.ordernow.utilities.getWeekStart
import java.util.*

@Dao
interface SaleSummaryDao {
    /**
     * Get all sale summaries from the database
     *
     * @return A LiveData object holding a list of all sale summaries
     */
    @Query("SELECT * FROM `sale-summary` ORDER BY `date`")
    fun getSaleSummaries(): LiveData<List<SaleSummary>>

    /**
     * Get all sale summaries based on the given type
     *
     * @param type The type of sale summaries
     * @return A LiveData object holding a list of same-type sale summaries
     */
    @Query("SELECT * FROM `sale-summary` WHERE `type` == :type ORDER BY `date`")
    fun getSaleSummaries(type: Report.Type): LiveData<List<SaleSummary>>

    /**
     * Get a sale summary
     * This method should be used internally only, there are other wrappers for this function
     * that can be used for specific scenarios, like querying for a daily sale summary or a
     * weekly sale summary
     *
     * @param time A time stamp
     * @param type The type of sale summary
     * @return A Sale summary
     */
    @Query("SELECT * FROM `sale-summary` WHERE `date` == :time AND `type` == :type ORDER BY `date`")
    fun getSaleSummary(time: Calendar, type: Report.Type): LiveData<SaleSummary>

    /**
     * Get a daily sale summary
     *
     * @param time A time stamp
     * @return A daily sale summary
     */
    fun getDailySaleSummary(time: Calendar): LiveData<SaleSummary> {
        return getSaleSummary(getDayStart(time), Report.Type.TODAY)
    }

    /**
     * Get a weekly sale summary
     *
     * @param time A time stamp
     * @return A weekly sale summary
     */
    fun getWeeklySaleSummary(time: Calendar): LiveData<SaleSummary> {
        return getSaleSummary(getWeekStart(time), Report.Type.THIS_WEEK)
    }

    /**
     * Get a monthly sale summary
     *
     * @param time A time stamp
     * @return A monthly sale summary
     */
    fun getMonthlySaleSummary(time: Calendar): LiveData<SaleSummary> {
        return getSaleSummary(getMonthStart(time), Report.Type.THIS_MONTH)
    }

    /**
     * Inert a sale summary to the database
     *
     * @param saleSummary The sale summary that will be inserted
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(saleSummary: SaleSummary)

    /**
     * Update the sale summary within the database
     *
     * @param saleSummary The sale summary that will be updated
     */
    @Update
    suspend fun update(saleSummary: SaleSummary)
}