package com.yue.ordernow.utilities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

interface ActivityArgs {

    fun intent(context: AppCompatActivity): Intent

    fun launchForResult(context: AppCompatActivity, requestCode: Int) =
        context.startActivityForResult(intent(context), requestCode)
}