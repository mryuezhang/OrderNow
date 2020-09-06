package com.yue.ordernow.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.yue.ordernow.utilities.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.*

class OrderDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var orderDao: OrderDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        orderDao = database.orderDao()

        // Insert these orders in a different order than the order of when they're being created
        orderDao.insert(DummyData.order2)
        Thread.sleep(1000)
        orderDao.insert(DummyData.order1)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetOrders() {
        val orders = getValue(orderDao.getAllOrders())
        Assert.assertThat(orders.size, Matchers.equalTo(2))

        // Ensure plant list is sorted by created time
        Assert.assertThat(orders[0], Matchers.equalTo(DummyData.order1))
        Assert.assertThat(orders[1], Matchers.equalTo(DummyData.order2))
    }
}