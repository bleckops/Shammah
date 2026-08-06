package co.bleck.shammah.composeapp.platform

import android.content.Context

object AndroidContextHolder {
    lateinit var context: Context
        private set

    fun init(appContext: Context) {
        context = appContext
    }
}
