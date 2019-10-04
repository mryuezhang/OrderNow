package com.yue.ordernow.models

import android.os.Parcel
import android.os.Parcelable

data class Order(val item: MenuItem, val note: String) : Parcelable {

    //val createTime = Calendar.getInstance().time.toString()

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(MenuItem::class.java.classLoader) as MenuItem,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flag: Int) {
        parcel.writeParcelable(item, flag)
        parcel.writeString(note)
    }

    override fun describeContents(): Int = 0

    override fun toString(): String {
        return "Order(item=$item, note=${note})"
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}