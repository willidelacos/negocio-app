package com.tunegocio.negocio.utils

import android.util.Log

object Logger {
    private const val TAG = "NegocioLog"

    fun d(message: String) = Log.d(TAG, message)
    fun e(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }
}