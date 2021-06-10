package org.metabrainz.mobile.util

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.WorkerThread
import org.metabrainz.mobile.data.sources.api.LookupService
import org.metabrainz.mobile.data.sources.api.entities.CoverArt
import org.metabrainz.mobile.util.Log.e
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

/**
 * A set of fairly general Android utility methods.
 */
object Utils {

    fun shareIntent(text: String?): Intent {
        val intent = Intent(Intent.ACTION_SEND).setType("text/plain")
        return intent.putExtra(Intent.EXTRA_TEXT, text)
    }

    fun emailIntent(recipient: String, subject: String?): Intent {
        val uri = Uri.parse("mailto:$recipient")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        return intent
    }

    fun urlIntent(url: String?): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }

    fun stringFromAsset(context: Context, asset: String?): String {
        return try {
            val input = context.resources.assets.open(asset!!)
            val buffer = ByteArray(input.available())
            input.read(buffer)
            val output = ByteArrayOutputStream()
            output.write(buffer)
            output.close()
            input.close()
            output.toString()
        } catch (e: IOException) {
            e("Error reading text file from assets folder.")
            ""
        }
    }

    fun changeLanguage(context: Context, lang_code: String): ContextWrapper {
        var context = context
        val sysLocale: Locale
        val rs = context.resources
        val config = rs.configuration
        sysLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales[0]
        } else {
            config.locale
        }
        if (lang_code != "" && sysLocale.language != lang_code) {
            val locale = Locale(lang_code)
            Locale.setDefault(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale)
            } else {
                config.locale = locale
            }
            context = context.createConfigurationContext(config)
        }
        return ContextWrapper(context)
    }
}