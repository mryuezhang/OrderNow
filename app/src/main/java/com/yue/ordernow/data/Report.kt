package com.yue.ordernow.data

import android.os.Parcel
import android.os.Parcelable
import com.yue.ordernow.utilities.currencyFormat

data class Report(val type: Type, var quantity: Int, var amount: Float) : Parcelable {

    var orders = ArrayList<Order>()

    @Suppress("UNCHECKED_CAST")
    constructor(parcel: Parcel) : this(
        Type.fromByte(parcel.readByte()),
        parcel.readInt(),
        parcel.readFloat()
    ) {
        orders = parcel.readArrayList(Order::class.java.classLoader) as ArrayList<Order>
    }

    fun getFormattedAmount(): String = currencyFormat(amount)

    enum class Type(val value: Byte) {
        TODAY(0), THIS_WEEK(1), THIS_MONTH(2);

        companion object {
            fun fromByte(value: Byte) = values().first { it.value == value }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(type.value)
        parcel.writeInt(quantity)
        parcel.writeFloat(amount)
        parcel.writeList(orders)
    }

    override fun describeContents(): Int {
        return 0
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