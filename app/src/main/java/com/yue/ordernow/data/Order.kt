package com.yue.ordernow.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yue.ordernow.adapters.OrderAdapter
import com.yue.ordernow.fragments.RestaurantMenuFragment
import com.yue.ordernow.utilities.currencyFormat
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "orders")
data class Order(
    @ColumnInfo(name = "order-items")
    var orderItems: ArrayList<OrderItem>,

    @ColumnInfo(name = "subtotal")
    var subtotalAmount: Float,

    @ColumnInfo(name = "total-quantity")
    var totalQuantity: Int,

    @ColumnInfo(name = "order-number")
    var orderNumber: Int,

    @ColumnInfo(name = "is-takeout")
    var isTakeout: Boolean,

    @ColumnInfo(name = "is-paid")
    var isPaid: Boolean,

    @ColumnInfo(name = "tax-rate")
    var taxRate: Float,

    @ColumnInfo(name = "time-created")
    var timeCreated: Calendar = Calendar.getInstance(),

    @ColumnInfo(name = "orderer")
    var orderer: String?,

    @ColumnInfo(name = "is-valid")
    var isValid: Boolean
) : Parcelable, OrderAdapter.ListItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    fun getFormattedCreatedTimeWithDayOfWeek(): String =
        SimpleDateFormat(
            "EEEE, MMMM d, yyyy 'at' HH:mm",
            Locale.getDefault()
        ).format(timeCreated.time)

    fun getCreatedDate(): String =
        SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(timeCreated.time)

    fun getFormattedSubtotal(): String =
        currencyFormat(subtotalAmount)

    fun getFormattedTax(): String =
        currencyFormat(subtotalAmount * taxRate)

    fun getFormattedTotalAmount(): String =
        currencyFormat(getTotalAmount())

    private fun getTotalAmount(): Float =
        (subtotalAmount * (1 + taxRate))

    companion object {
        var lastOrderCreatedTime: Calendar? = null
        var orderCount = 0

        private fun timeStamp(): Calendar {
            val now = Calendar.getInstance()
            if (now.get(Calendar.DAY_OF_YEAR) ==
                lastOrderCreatedTime?.get(Calendar.DAY_OF_YEAR)
            ) {
                // if this and previous order are made within the same day
                orderCount++
            } else {
                // if this order and previous order are not made within the same day, or there is no previous order
                orderCount = 0
                orderCount++
            }
            lastOrderCreatedTime = now
            return now
        }

        fun newInstance(
            orderItems: ArrayList<OrderItem>,
            subtotal: Float,
            totalQuantity: Int,
            isTakeout: Boolean,
            isPaid: Boolean
        ): Order {
            val timeStamp = timeStamp()
            return Order(
                orderItems,
                subtotal,
                totalQuantity,
                orderCount,
                isTakeout,
                isPaid,
                RestaurantMenuFragment.taxRate,
                timeStamp,
                null,
                true
            )
        }

        fun newInstance(
            orderItems: ArrayList<OrderItem>,
            subtotal: Float,
            totalQuantity: Int,
            isTakeout: Boolean,
            isPaid: Boolean,
            orderer: String
        ): Order {
            val timeStamp = timeStamp()
            return Order(
                orderItems,
                subtotal,
                totalQuantity,
                orderCount,
                isTakeout,
                isPaid,
                RestaurantMenuFragment.taxRate,
                timeStamp,
                orderer,
                true
            )
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<Order> {
            override fun createFromParcel(parcel: Parcel): Order {
                return Order(parcel)
            }

            override fun newArray(size: Int): Array<Order?> {
                return arrayOfNulls(size)
            }
        }
    }

    /*
     * Parcelable methods
     */
    @Suppress("UNCHECKED_CAST")
    constructor(parcel: Parcel) : this(
        parcel.readArrayList(OrderItem::class.java.classLoader) as ArrayList<OrderItem>,
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readFloat(),
        Calendar.getInstance().apply { timeInMillis = parcel.readLong() },
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
        id = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(orderItems)
        parcel.writeFloat(subtotalAmount)
        parcel.writeInt(totalQuantity)
        parcel.writeInt(orderNumber)
        parcel.writeByte(if (isTakeout) 1 else 0)
        parcel.writeByte(if (isPaid) 1 else 0)
        parcel.writeFloat(taxRate)
        parcel.writeLong(timeCreated.timeInMillis)
        parcel.writeString(orderer)
        parcel.writeLong(id)
        parcel.writeByte(if (isValid) 1 else 0)
    }

    override fun describeContents(): Int = 0
}