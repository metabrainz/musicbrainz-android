/*
 * Copyright (C) 2011 Jamie McDonald
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

package org.musicbrainz.mobile.util;

import org.musicbrainz.mobile.R;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.FilterQueryProvider;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;

/**
 * This is a helper class to provide a Cursor to the suggestions database.
 * The content provider isn't public, so this might break in future.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class SuggestionHelper {

	private final Activity activity;
	private static final String COLUMN = "display1";
	private static final String URI = "content://" + SuggestionProvider.AUTHORITY + "/suggestions";
    private static final String[] FROM = new String[] {COLUMN};
    private static final int[] TO = new int[] {R.id.dropdown_item};

    public SuggestionHelper(Activity activity) {
        this.activity = activity;
    }
    
    public SimpleCursorAdapter getAdapter() {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(activity, R.layout.dropdown_item, null, FROM, TO);
        adapter.setCursorToStringConverter(new SuggestionCursorToString());
        adapter.setFilterQueryProvider(new SuggestionFilterQuery());
        return adapter;
    }
    
    public ArrayAdapter<String> getEmptyAdapter() {
    	return new ArrayAdapter<String>(activity, R.layout.dropdown_item, new String[]{});
    }
    
    private class SuggestionCursorToString implements CursorToStringConverter {

		@Override
		public CharSequence convertToString(Cursor cursor) {
			int columnIndex = cursor.getColumnIndexOrThrow(COLUMN);
            return cursor.getString(columnIndex);
		}
    }
    
    private class SuggestionFilterQuery implements FilterQueryProvider {

		@Override
		public Cursor runQuery(CharSequence constraint) {
			return getMatchingEntries((constraint != null ? constraint.toString() : null));
		}
    }
    
    private Cursor getMatchingEntries(String constraint) {

        if (constraint == null) {
            return null;
        }
        
        String where = COLUMN + " LIKE ?";
    	String[] args = {constraint.trim() + "%"};

        Cursor cursor = activity.managedQuery(Uri.parse(URI), null, where, args, COLUMN + " ASC");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    
}
