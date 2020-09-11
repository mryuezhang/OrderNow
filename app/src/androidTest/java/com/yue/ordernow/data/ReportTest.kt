package com.yue.ordernow.data

import android.os.Parcel
import org.junit.Assert
import org.junit.Test

class ReportTest {

    @Test
    fun testParcelable() {
        val report = Report(Report.Type.THIS_MONTH, 2, 63f).apply {
            associate(DummyData.order1)
            associate(DummyData.order2)
        }

        val parcel = Parcel.obtain()
        report.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val actual = Report.createFromParcel(parcel)
        Assert.assertEquals(report, actual)
    }
}