package com.yue.ordernow.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface OrderDao {

    /**
     * Get all orders from the database
     *
     * @return A LiveData object holding a list of all orders
     */
    @Query("SELECT * FROM `orders` ORDER BY `time-created`")
    fun getAllOrders(): LiveData<List<Order>>

    /**
     * Get the last one order that was placed
     *
     * @return A LiveData object holding the last order
     */
    @Query("SELECT * FROM `orders` ORDER BY `time-created` DESC LIMIT 1")
    fun getLastOrder(): LiveData<Order>

    /**
     * Get orders between a time period
     *
     * @param start The start of the time period
     * @param end The end of the time period
     * @return A ListData object holding a list of orders
     */
    @Query("SELECT * FROM `orders` WHERE `time-created` BETWEEN :start AND :end ORDER BY `time-created`")
    fun getOrdersBetween(start: Long, end: Long): LiveData<List<Order>>

    /**
     * Get only unpaid orders between a time period
     *
     * @param start The start of the time period
     * @param end The end of the time period
     * @return A ListData object holding a list of orders
     */
    @Query("SELECT * FROM `orders` WHERE `time-created` BETWEEN :start AND :end AND `is-paid` == 0 ORDER BY `time-created`")
    fun getUnPaidOrdersBetween(start: Long, end: Long): LiveData<List<Order>>

    /**
     * Get orders between a time period
     *
     * @param start The start of the time period
     * @param end The end of the time period
     * @return A ListData object holding a list of orders
     */
    @Query(
        "SELECT * FROM " +
                "(SELECT * FROM `orders` WHERE `time-created` BETWEEN :start AND :end ORDER BY `time-created`) " +
                "WHERE `orderer` LIKE :searchText OR `order-number` LIKE :searchText"
    )
    fun getOrdersBetweenBySearchText(
        searchText: String,
        start: Long,
        end: Long
    ): LiveData<List<Order>>

    /**
     * Get only unpaid orders between a time period
     *
     * @param start The start of the time period
     * @param end The end of the time period
     * @return A ListData object holding a list of orders
     */
    @Query(
        "SELECT * FROM " +
                "(SELECT * FROM `orders` WHERE `time-created` BETWEEN :start AND :end AND `is-paid` == 0 ORDER BY `time-created`) " +
                "WHERE `orderer` LIKE :searchText OR `order-number` LIKE :searchText"
    )
    fun getUnPaidOrdersBetweenBySearchText(
        searchText: String,
        start: Long,
        end: Long
    ): LiveData<List<Order>>

    /**
     * Get the latest orders of the given amount
     *
     * @param num The amount of wanted orders
     * @return A ListData object holding a list of orders
     */
    @Query("SELECT * FROM `orders` ORDER BY `time-created` DESC LIMIT :num")
    fun getOrders(num: Int): LiveData<List<Order>>

    /**
     * Get the latest unpaid orders of the given amount
     * So let's say you passed 100 here, this function will NOT return 100 unpaid orders, it will
     * return the unpaid orders within this 100 orders
     *
     * @param num The amount of wanted orders
     * @return A ListData object holding a list of orders
     */
    @Query("SELECT * FROM (SELECT * FROM `orders` ORDER BY `time-created` DESC LIMIT :num) WHERE `is-paid` == 0")
    fun getUnpaidOrders(num: Int): LiveData<List<Order>>

    /**
     * Get the orders, which contain the given search text, from the latest orders of the given amount
     *
     * @param searchText The text should be contained in the wanted orders
     * @param num The amount of wanted orders
     * @return A ListData object holding a list of orders
     */
    @Query(
        "SELECT * FROM " +
                "(SELECT * FROM `orders` ORDER BY `time-created` DESC LIMIT :num) " +
                "WHERE `orderer` LIKE :searchText OR `order-number` LIKE :searchText"
    )
    fun getOrdersBySearchText(searchText: String, num: Int): LiveData<List<Order>>

    /**
     * GGet the orders, which contain the given search text, from the latest orders of the given amount
     * So let's say you passed 100 here, this function will NOT return 100 unpaid orders, it will
     * return the unpaid orders that also contains the given search text within this 100 orders
     *
     * @param num The amount of wanted orders
     * @return A ListData object holding a list of orders
     */
    @Query(
        "SELECT * FROM " +
                "(SELECT * FROM " +
                "(SELECT * FROM `orders` ORDER BY `time-created` DESC LIMIT :num) " +
                "WHERE `orderer` LIKE :searchText OR `order-number` LIKE :searchText) " +
                "WHERE `is-paid` == 0"
    )
    fun getUnpaidOrdersBySearchText(searchText: String, num: Int): LiveData<List<Order>>

    /**
     * Inert an order to the database
     *
     * @param order The order that will be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    /**
     * Update the order within the database, which has the same ID with the provided one
     * Note: this should be used for testing purpose only
     *
     * @param order The order that will be updated
     */
    @Update
    suspend fun updateOrder(order: Order)

    /**
     * Delete all orders from the database
     * Note: this should be used for testing purpose only
     *
     */
    @Query("DELETE FROM `orders`")
    suspend fun deleteAllOrders()
}