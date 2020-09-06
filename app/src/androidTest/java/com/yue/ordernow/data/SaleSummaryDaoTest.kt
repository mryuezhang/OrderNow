package com.yue.ordernow.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.yue.ordernow.utilities.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.*
import java.util.*


class SaleSummaryDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var orderDao: OrderDao
    private lateinit var saleSummaryDao: SaleSummaryDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        orderDao = database.orderDao()
        saleSummaryDao = database.saleSummaryDao()
        orderDao.insert(DummyData.order1)
        orderDao.insert(DummyData.order2)

        saleSummaryDao.insert(DummyData.dailySaleSummary)
        saleSummaryDao.insert(DummyData.weeklySaleSummary)
        saleSummaryDao.insert(DummyData.monthlySaleSummary)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetDailySaleSummary() {
        val result = getValue(saleSummaryDao.getDailySaleSummary(Calendar.getInstance()))

        Assert.assertThat(result, Matchers.equalTo(DummyData.dailySaleSummary))
    }

    @Test
    fun testGetWeeklySaleSummary() {
        val result = getValue(saleSummaryDao.getWeeklySaleSummary(Calendar.getInstance()))

        Assert.assertThat(result, Matchers.equalTo(DummyData.weeklySaleSummary))
    }

    @Test
    fun testGetMonthlySaleSummary() {
        val result = getValue(saleSummaryDao.getMonthlySaleSummary(Calendar.getInstance()))

        Assert.assertThat(result, Matchers.equalTo(DummyData.monthlySaleSummary))
    }

    @Test
    fun testSaleSummaryUpdate() {
        runBlocking {
            DummyData.dailySaleSummary.addSaleData(DummyData.order1)
            DummyData.dailySaleSummary.addSaleData(DummyData.order2)
            saleSummaryDao.update(DummyData.dailySaleSummary)
            val result = getValue(saleSummaryDao.getDailySaleSummary(Calendar.getInstance()))

            Assert.assertThat(result, Matchers.equalTo(DummyData.dailySaleSummary))
        }
    }
}