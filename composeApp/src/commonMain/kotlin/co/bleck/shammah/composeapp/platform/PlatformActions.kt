package co.bleck.shammah.composeapp.platform

expect class PlatformActions {
    fun openUrl(url: String)
    fun openMaps(latitude: Double, longitude: Double, query: String)
    fun shareIcsCalendar(filename: String, icsContent: String)
    fun showMessage(message: String)
    fun appVersionName(): String
}
