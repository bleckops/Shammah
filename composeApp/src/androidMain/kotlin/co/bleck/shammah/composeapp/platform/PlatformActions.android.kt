package co.bleck.shammah.composeapp.platform

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import org.koin.core.module.Module
import org.koin.dsl.module

actual class PlatformActions {
    private val context get() = AndroidContextHolder.context

    actual fun openUrl(url: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    actual fun openMaps(latitude: Double, longitude: Double, query: String) {
        val encodedQuery = Uri.encode(query)
        val uri = Uri.parse("geo:$latitude,$longitude?q=$encodedQuery")
        context.startActivity(Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    actual fun shareIcsCalendar(filename: String, icsContent: String) {
        val file = File(context.cacheDir, filename)
        file.writeText(icsContent)
        val uri = FileProvider.getUriForFile(context, "co.bleck.shammah.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/calendar"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Exportar calendario con...").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    actual fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    actual fun appVersionName(): String = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0"
    } catch (e: Exception) {
        "1.0"
    }
}

actual fun platformModule(): Module = module {
    single { PlatformActions() }
}
