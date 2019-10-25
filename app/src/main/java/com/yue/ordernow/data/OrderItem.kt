package com.yue.ordernow.data

import android.os.Parcel
import android.os.Parcelable
import com.yue.ordernow.utilities.currencyFormat

data class OrderItem(val item: MenuItem, var quantity: Int, var note: String) : Parcelable {
    var extraCost: Float = 0f

    constructor(item: MenuItem, quantity: Int, note: String, extraCharge: Float) : this(
        item,
        quantity,
        note
    ) {
        this.extraCost = extraCharge
    }

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(MenuItem::class.java.classLoader) as MenuItem,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flag: Int) {
        parcel.writeParcelable(item, flag)
        parcel.writeInt(quantity)
        parcel.writeString(note)
        parcel.writeFloat(extraCost)
    }

    override fun describeContents(): Int = 0

    fun getAmount(): Float = item.price * quantity + extraCost

    fun getFormattedExtraCost(): String = currencyFormat(extraCost)

    fun getFormattedAmount(): String = currencyFormat(item.price * quantity + extraCost)


    companion object CREATOR : Parcelable.Creator<OrderItem> {
        override fun createFromParcel(parcel: Parcel): OrderItem {
            return OrderItem(parcel)
        }

        override fun newArray(size: Int): Array<OrderItem?> {
            return arrayOfNulls(size)
        }
    }
}