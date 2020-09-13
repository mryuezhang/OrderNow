package com.yue.ordernow.data

import android.os.Parcel
import android.os.Parcelable
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.yue.ordernow.utilities.currencyFormat
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class Report(val type: Type, var quantity: Int, var amount: Float) : Parcelable {
    var timeStamp: Long = 0
    var takeOutCount = 0
    var diningInCount = 0
    val barEntries = ArrayList<BarEntry>()
    var saleData: java.util.HashMap<String, Int> = HashMap()

    fun getFormattedAmount(): String = currencyFormat(amount)

    fun associate(order: Order) {
        if (barEntries.isEmpty()) {
            when (type) {
                Type.TODAY -> {
                    for (i in 0..23) {
                        barEntries.add(BarEntry(i.toFloat(), 0f))
                    }
                }
                Type.THIS_WEEK -> {
                    for (i in 0..6) {
                        barEntries.add(BarEntry(i.toFloat(), 0f))
                    }
                }
                Type.THIS_MONTH -> {
                    when (order.timeCreated.get(Calendar.MONTH)) {
                        Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> {
                            for (i in 0..30) {
                                barEntries.add(BarEntry(i.toFloat(), 0f))
                            }
                        }
                        Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> {
                            for (i in 0..29) {
                                barEntries.add(BarEntry(i.toFloat(), 0f))
                            }
                        }
                        else -> {
                            if (order.timeCreated.getActualMaximum(Calendar.DAY_OF_YEAR) > 365) {
                                for (i in 0..28) {
                                    barEntries.add(BarEntry(i.toFloat(), 0f))
                                }
                            } else {
                                for (i in 0..27) {
                                    barEntries.add(BarEntry(i.toFloat(), 0f))
                                }
                            }
                        }
                    }
                }
            }
        }

        when (type) {
            Type.TODAY -> {
                barEntries[order.timeCreated.get(Calendar.HOUR_OF_DAY)].y++
            }
            Type.THIS_WEEK -> {
                // Java Calendar DAY_OF_MONTH starts at 1, which is Sunday
                barEntries[order.timeCreated.get(Calendar.DAY_OF_WEEK) - 1].y++
            }
            Type.THIS_MONTH -> {
                // Java Calendar DAY_OF_MONTH starts at 1
                barEntries[order.timeCreated.get(Calendar.DAY_OF_MONTH) - 1].y++
            }
        }

        if (order.isTakeout) {
            takeOutCount++
        } else {
            diningInCount++
        }

        if (timeStamp == 0L) {
            timeStamp = order.timeCreated.timeInMillis
        }

        quantity++
        amount += order.subtotalAmount
        
        order.orderItems.forEach { 
            if (saleData.containsKey(it.item.name)) {
                saleData[it.item.name] = saleData.getValue(it.item.name) + it.quantity
            } else {
                saleData[it.item.name] = it.quantity
            }
        }
    }

    enum class Type(val value: Int) {
        TODAY(0), THIS_WEEK(1), THIS_MONTH(2);

        companion object {
            fun fromInt(value: Int) = values().first { it.value == value }
        }
    }

    constructor(parcel: Parcel) : this(
        Type.fromInt(parcel.readInt()),
        parcel.readInt(),
        parcel.readFloat()
    ) {
        timeStamp = parcel.readLong()
        takeOutCount = parcel.readInt()
        diningInCount = parcel.readInt()
        parcel.readTypedList(barEntries as List<Entry>, BarEntry.CREATOR)
        saleData = parcel.readSerializable() as HashMap<String, Int>
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(type.value)
        dest.writeInt(quantity)
        dest.writeFloat(amount)
        dest.writeLong(timeStamp)
        dest.writeInt(takeOutCount)
        dest.writeInt(diningInCount)
        dest.writeTypedList(barEntries)
        dest.writeSerializable(saleData as Serializable)
    }

    companion object CREATOR : Parcelable.Creator<Report> {
        override fun createFromParcel(parcel: Parcel): Report {
            return Report(parcel)
        }

        override fun newArray(size: Int): Array<Report?> {
            return arrayOfNulls(size)
        }
    }
}