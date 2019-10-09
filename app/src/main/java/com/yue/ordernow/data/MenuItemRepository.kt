package com.yue.ordernow.data

class MenuItemRepository private constructor(private val menuItemDao: MenuItemDao) {


    fun getItems() = menuItemDao.getItems()

    fun getMains() = menuItemDao.getMains()

    fun getAppetizers() = menuItemDao.getAppetizers()

    fun getDrinks() = menuItemDao.getDrinks()

    fun getBreakfasts() = menuItemDao.getBreakfasts()

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: MenuItemRepository? = null

        fun getInstance(menuItemDao: MenuItemDao) =
            instance ?: synchronized(this) {
                instance ?: MenuItemRepository(menuItemDao).also { instance = it }
            }
    }
}