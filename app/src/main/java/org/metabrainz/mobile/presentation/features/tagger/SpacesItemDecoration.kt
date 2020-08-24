package org.metabrainz.mobile.presentation.features.tagger

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(space: Int, val skipFirst: Boolean = false) : RecyclerView.ItemDecoration() {

    private val space: Int = space.dp

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val orientation = (parent.layoutManager as? LinearLayoutManager)?.orientation
                ?: LinearLayoutManager.VERTICAL

        if (orientation == LinearLayoutManager.HORIZONTAL) {
            if (skipFirst && parent.getChildLayoutPosition(view) == 0) {
                outRect.left = 0
                outRect.right = space
            } else {
                outRect.left = space
                outRect.right = space
            }
        } else {
            if (skipFirst && parent.getChildLayoutPosition(view) == 0) {
                outRect.top = 0
                outRect.bottom = space
            } else {
                outRect.top = space
                outRect.bottom = space
            }
        }
    }
}

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()