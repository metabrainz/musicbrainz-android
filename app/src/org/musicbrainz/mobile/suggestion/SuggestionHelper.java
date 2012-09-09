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
 * This is a helper class to provide a Cursor to the suggestions database.
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
