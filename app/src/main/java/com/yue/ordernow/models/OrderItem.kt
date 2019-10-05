package com.yue.ordernow.models

import android.os.Parcel
import android.os.Parcelable

data class OrderItem(val item: MenuItem, var quantity: Int, val note: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(MenuItem::class.java.classLoader) as MenuItem,
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flag: Int) {
        parcel.writeParcelable(item, flag)
        parcel.writeInt(quantity)
        parcel.writeString(note)
    }

    override fun describeContents(): Int = 0

    override fun toString(): String {
        return "OrderItem(item=$item, quantity=${quantity}, note=${note})"
    }

    companion object CREATOR : Parcelable.Creator<OrderItem> {
        override fun createFromParcel(parcel: Parcel): OrderItem {
            return OrderItem(parcel)
        }

        override fun newArray(size: Int): Array<OrderItem?> {
            return arrayOfNulls(size)
        }
    }
}