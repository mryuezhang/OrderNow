package com.yue.ordernow.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.yue.ordernow.utilities.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.*

class MenuItemDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var menuItemDao: MenuItemDao
    private val burger = MenuItem("burger", 15.00F, "main")
    private val pizza = MenuItem("pizza", 12.00F, "main")
    private val wings = MenuItem("wings", 8.00F, "appetizer")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        menuItemDao = database.menuItemDao()

        // Insert plants in non-alphabetical order to test that results are sorted by name
        menuItemDao.insertAll(listOf(burger, pizza, wings))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetMenuItems() {
        val menuItemList = getValue(menuItemDao.getItems())
        Assert.assertThat(menuItemList.size, Matchers.equalTo(3))

        // Ensure plant list is sorted by name
        Assert.assertThat(menuItemList[0], Matchers.equalTo(burger))
        Assert.assertThat(menuItemList[1], Matchers.equalTo(pizza))
        Assert.assertThat(menuItemList[2], Matchers.equalTo(wings))

        // Get only mains
        val mainList = getValue(menuItemDao.getMains())
        Assert.assertThat(mainList.size, Matchers.equalTo(2))
        Assert.assertThat(mainList[0], Matchers.equalTo(burger))
        Assert.assertThat(mainList[1], Matchers.equalTo(pizza))

        // Get only appetizers
        val appetizerList = getValue(menuItemDao.getAppetizers())
        Assert.assertThat(appetizerList.size, Matchers.equalTo(1))
        Assert.assertThat(appetizerList[0], Matchers.equalTo(wings))
    }
}