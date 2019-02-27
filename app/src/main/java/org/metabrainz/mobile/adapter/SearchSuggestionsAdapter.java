package org.metabrainz.mobile.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import org.metabrainz.mobile.R;


public class SearchSuggestionsAdapter extends CursorAdapter {

    private LayoutInflater inflater;

    public SearchSuggestionsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.item_suggestions, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = view.findViewById(R.id.suggestion);
        String[] suggestion = cursor.getColumnNames();
        int su = cursor.getCount();
        textView.setText(suggestion[0]);
    }
}
