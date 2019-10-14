package com.yue.ordernow.data

class OrderRepository private constructor(private val orderDao: OrderDao) {

    fun getAllOrders() = orderDao.getAllOrders()

    suspend fun deleteAllOrders() = orderDao.deleteAllOrders()

    suspend fun insertOrder(order: Order) = orderDao.insertOrder(order)

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: OrderRepository? = null

        fun getInstance(gardenPlantingDao: OrderDao) =
            instance ?: synchronized(this) {
                instance ?: OrderRepository(gardenPlantingDao).also { instance = it }
            }
    }
}