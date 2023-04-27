package org.metabrainz.android.application

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import org.metabrainz.android.util.Configuration

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        loadCustomTypefaces()
    }

    private fun loadCustomTypefaces() {
        robotoLight = Typeface.createFromAsset(context!!.assets, "Roboto-Light.ttf")
    }

    val isOnline: Boolean
        get() {
            val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw      = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    //for other device how are able to connect with Ethernet
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    //for check internet over Bluetooth
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else -> false
                }
            } else {
                return connectivityManager.activeNetworkInfo?.isConnected ?: false
            }
        }

    companion object {
        const val WEBSITE_BASE_URL = "https://musicbrainz.org/"
        const val PICARD_OPENALBUM_URL = "http://%s:%s/openalbum?id=%s"
        var context: App? = null
        var robotoLight: Typeface? = null
            private set
        val userAgent: String
            get() = Configuration.USER_AGENT + "/" + version
        val clientId: String
            get() = Configuration.CLIENT_NAME + "-" + version
        val version: String
            get() = try {
                context!!.packageManager.getPackageInfo(context!!.packageName, 0).versionName
            }
            catch (e: PackageManager.NameNotFoundException) {
                "unknown"
            }
    }
}