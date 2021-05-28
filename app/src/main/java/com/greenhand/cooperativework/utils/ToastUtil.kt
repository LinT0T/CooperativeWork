package com.greenhand.cooperativework.utils

import android.widget.Toast
import com.greenhand.cooperativework.base.BaseApplication

fun String.toast() {
    Toast.makeText(BaseApplication.appContext,this, Toast.LENGTH_SHORT).show()
}

fun String.toastLong() {
    Toast.makeText(BaseApplication.appContext, this, Toast.LENGTH_LONG).show()
}