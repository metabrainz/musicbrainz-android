package org.musicbrainz.mobile.adapter.list;

import java.util.List;

import org.musicbrainz.android.api.data.EditorCollectionStub;
import org.musicbrainz.mobile.R;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CollectionListAdapter extends ArrayAdapter<EditorCollectionStub> {

    private Activity context;
    private List<EditorCollectionStub> collectionStubs;
    
    public CollectionListAdapter(Activity context, List<EditorCollectionStub> collectionStubs) {
        super(context, R.layout.list_collection, collectionStubs);
        this.context = context;
        this.collectionStubs = collectionStubs;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View collection = convertView;
        CollectionStubHolder holder = null;
        
        if (collection == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            collection = inflater.inflate(R.layout.list_collection, parent, false);
            holder = new CollectionStubHolder(collection);
            collection.setTag(holder);
        } else {
            holder = (CollectionStubHolder) collection.getTag();
        }
        
        EditorCollectionStub stub = collectionStubs.get(position);
        holder.getTitle().setText(stub.getName());
        holder.getCount().setText(getReleasesDisplayText(stub));
        return collection;
    }

    private String getReleasesDisplayText(EditorCollectionStub stub) {
        Resources res = context.getResources();
        return res.getQuantityString(R.plurals.release_plurals, stub.getCount(), stub.getCount());
    }
    
    private class CollectionStubHolder {
        View base;
        TextView title = null;
        TextView count = null;
        
        CollectionStubHolder(View base) {
            this.base = base;
        }
        
        TextView getTitle() {
            if (title == null) {
                title = (TextView) base.findViewById(R.id.list_collection_title);
            }
            return title;
        }
        
        TextView getCount() {
            if (count == null) {
                count = (TextView) base.findViewById(R.id.list_collection_count);
            }
            return count;
        }
    }
    
}
