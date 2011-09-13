/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Simple subclass of TextView to maintain focus. This allows the text to
 * marquee continuously so that a large String of tags can be displayed in a
 * vertically small View.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class FocusTextView extends TextView {

	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onFocusChanged(boolean focused, int direction, Rect prev) {
		if (focused)
			super.onFocusChanged(focused, direction, prev);
	}

	public void onWindowFocusChanged(boolean focused) {
		if (focused)
			super.onWindowFocusChanged(focused);
	}

	public boolean isFocused() {
		return true;
	}

}