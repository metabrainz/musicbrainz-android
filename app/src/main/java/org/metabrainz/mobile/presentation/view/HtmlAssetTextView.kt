package org.metabrainz.mobile.presentation.view

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import org.metabrainz.mobile.util.Utils

class HtmlAssetTextView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    fun setAsset(asset: String?) {
        text = HtmlCompat.fromHtml(Utils.stringFromAsset(context, asset), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}