package com.yue.ordernow

import org.junit.Assert
import org.junit.Test

class SaleSummaryTest {

    @Test
    fun testAddSaleData() {
        var expectedOrderIds = listOf(DummyData.order1.id)
        var expectedSaleData = mapOf(
            DummyData.orderItem1.item.name to DummyData.orderItem1.quantity,
            DummyData.orderItem2.item.name to DummyData.orderItem2.quantity
        )
        DummyData.dailySaleSummary.addSaleData(DummyData.order1)
        Assert.assertEquals(expectedSaleData, DummyData.dailySaleSummary.saleData)

        DummyData.dailySaleSummary.addSaleData(DummyData.order1)
        Assert.assertEquals(expectedSaleData, DummyData.dailySaleSummary.saleData)

        expectedOrderIds = listOf(DummyData.order1.id, DummyData.order2.id)
        expectedSaleData = mapOf(
            DummyData.orderItem1.item.name to DummyData.orderItem1.quantity,
            DummyData.orderItem2.item.name to DummyData.orderItem2.quantity + DummyData.orderItem3.quantity,
            DummyData.orderItem4.item.name to DummyData.orderItem4.quantity
        )
        DummyData.dailySaleSummary.addSaleData(DummyData.order2)
        Assert.assertEquals(expectedSaleData, DummyData.dailySaleSummary.saleData)
    }

    @Test
    fun testRemoveSaleData() {
        val expectedOrderIds = listOf(DummyData.order1.id)
        val expectedSaleData = mapOf(
            DummyData.orderItem1.item.name to DummyData.orderItem1.quantity,
            DummyData.orderItem2.item.name to DummyData.orderItem2.quantity
        )
        DummyData.dailySaleSummary.addSaleData(DummyData.order1)
        DummyData.dailySaleSummary.addSaleData(DummyData.order2)
        DummyData.dailySaleSummary.removeSaleData(DummyData.order2)
        Assert.assertEquals(expectedSaleData, DummyData.dailySaleSummary.saleData)

        DummyData.dailySaleSummary.removeSaleData(DummyData.order2)
        Assert.assertEquals(expectedSaleData, DummyData.dailySaleSummary.saleData)
    }
}
