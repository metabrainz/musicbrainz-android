package org.metabrainz.mobile.presentation.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import org.metabrainz.mobile.App

class RobotoLightTextView : AppCompatTextView {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle)

    override fun setTypeface(tf: Typeface?) {
        if (isInEditMode) {
            super.setTypeface(tf)
        } else {
            super.setTypeface(App.getRobotoLight())
        }
    }
}