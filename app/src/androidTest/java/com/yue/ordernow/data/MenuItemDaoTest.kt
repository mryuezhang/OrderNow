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

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        menuItemDao = database.menuItemDao()

        // Insert plants in non-alphabetical order to test that results are sorted by name
        menuItemDao.insertAll(listOf(DummyData.burger, DummyData.pizza, DummyData.wings))
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
        Assert.assertThat(menuItemList[0], Matchers.equalTo(DummyData.burger))
        Assert.assertThat(menuItemList[1], Matchers.equalTo(DummyData.pizza))
        Assert.assertThat(menuItemList[2], Matchers.equalTo(DummyData.wings))

        // Get only mains
        val mainList = getValue(menuItemDao.getMains())
        Assert.assertThat(mainList.size, Matchers.equalTo(2))
        Assert.assertThat(mainList[0], Matchers.equalTo(DummyData.burger))
        Assert.assertThat(mainList[1], Matchers.equalTo(DummyData.pizza))

        // Get only appetizers
        val appetizerList = getValue(menuItemDao.getAppetizers())
        Assert.assertThat(appetizerList.size, Matchers.equalTo(1))
        Assert.assertThat(appetizerList[0], Matchers.equalTo(DummyData.wings))
    }
}