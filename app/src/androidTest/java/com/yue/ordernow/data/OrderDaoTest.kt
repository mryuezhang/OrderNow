package com.yue.ordernow.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.yue.ordernow.utils.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.*

class OrderDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var orderDao: OrderDao
    private val burger = MenuItem("burger", 15.00F, "main")
    private val pizza = MenuItem("pizza", 12.00F, "main")
    private val wings = MenuItem("wings", 8.00F, "appetizer")
    private val orderItem1 = OrderItem(burger, 1, "no tomato")
    private val orderItem2 = OrderItem(pizza, 1, "")
    private val orderItem3 = OrderItem(pizza, 1, "extra cheese")
    private val orderItem4 = OrderItem(wings, 3, "")
    private lateinit var order1: Order
    private lateinit var order2: Order

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        orderDao = database.orderDao()
        order1 = Order(arrayListOf(orderItem1, orderItem2))
        Thread.sleep(1000)
        order2 = Order(arrayListOf(orderItem1, orderItem2))
        orderDao.insertOrder(order2)
        orderDao.insertOrder(order1)
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
        Assert.assertThat(orders[0], Matchers.equalTo(order1))
        Assert.assertThat(orders[1], Matchers.equalTo(order2))
    }
}