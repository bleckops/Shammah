package co.bleck.shammah

import android.app.Application
import co.bleck.shammah.composeapp.platform.AndroidContextHolder
import co.bleck.shammah.data.firebase.FirebaseBootstrap

class ShammahApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidContextHolder.init(applicationContext)
        FirebaseBootstrap.initialize()
    }
}
