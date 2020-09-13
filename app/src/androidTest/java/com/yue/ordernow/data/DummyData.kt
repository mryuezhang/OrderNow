package com.yue.ordernow.data

object DummyData {

    val burger = MenuItem("burger", 15.00F, "main")
    val pizza = MenuItem("pizza", 12.00F, "main")
    val wings = MenuItem("wings", 8.00F, "appetizer")
    val orderItem1 = OrderItem(burger, 1, "no tomato")
    val orderItem2 = OrderItem(pizza, 1, "")
    val orderItem3 = OrderItem(pizza, 1, "extra cheese")
    val orderItem4 = OrderItem(wings, 3, "")
    var order1: Order
    var order2: Order

    init {
        order1 =
            Order.newInstance(arrayListOf(orderItem1, orderItem2), 0f, 0, false, false, "Table 2")
        Thread.sleep(1000)
        order2 = Order.newInstance(arrayListOf(orderItem3, orderItem4), 0f, 0, true, true)
    }
}