package com.yue.ordernow.models

import android.icu.util.Calendar

data class Order(val item: MenuItem) {
    val createTime = Calendar.getInstance().time.toString()
    var note: String = ""
        private set

    constructor(item: MenuItem, note: String) : this(item) {
        this.note = note
    }
}