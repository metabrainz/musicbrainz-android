package org.metabrainz.mobile.presentation.features.suggestion;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.FilterQueryProvider;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.loader.content.CursorLoader;

import org.metabrainz.mobile.R;

/**
 * This is a helper class to provide a Cursor to the suggestions database.
 */
public class SuggestionHelper {

    private static final String COLUMN = "display1";
    private static final String URI = "content://" + SuggestionProvider.AUTHORITY + "/suggestions";
    private static final String[] FROM = new String[]{COLUMN};
    private static final int[] TO = new int[]{R.id.dropdown_item};

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

    public Cursor getMatchingEntries(String constraint) {
        if (constraint == null) {
            return null;
        }

        String where = COLUMN + " LIKE ?";
        String[] args = {constraint.trim() + "%"};

        CursorLoader cursorLoader = new CursorLoader(context, Uri.parse(URI), null, where, args, COLUMN + " ASC");
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    private static class SuggestionCursorToString implements SimpleCursorAdapter.CursorToStringConverter {

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

}
