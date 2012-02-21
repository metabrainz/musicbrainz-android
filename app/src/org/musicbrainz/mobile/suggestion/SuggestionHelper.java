/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.suggestion;

import org.musicbrainz.mobile.R;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.widget.ArrayAdapter;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;

/**
 * This is a helper class to provide a Cursor to the suggestions database. The
 * content provider is not public, so this *could* stop working in future.
 */
public class SuggestionHelper {

    private static final String COLUMN = "display1";
    private static final String URI = "content://" + SuggestionProvider.AUTHORITY + "/suggestions";
    private static final String[] FROM = new String[] { COLUMN };
    private static final int[] TO = new int[] { R.id.dropdown_item };
    
    private final Context context;

    public SuggestionHelper(Context context) {
        this.context = context;
    }

    public SimpleCursorAdapter getAdapter() {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, R.layout.dropdown_item, null, FROM, TO);
        adapter.setCursorToStringConverter(new SuggestionCursorToString());
        adapter.setFilterQueryProvider(new SuggestionFilterQuery());
        return adapter;
    }

    public ArrayAdapter<String> getEmptyAdapter() {
        return new ArrayAdapter<String>(context, R.layout.dropdown_item, new String[] {});
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
        String[] args = { constraint.trim() + "%" };

        CursorLoader cursorLoader = new CursorLoader(context, Uri.parse(URI), null, where, args, COLUMN + " ASC");
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

}
