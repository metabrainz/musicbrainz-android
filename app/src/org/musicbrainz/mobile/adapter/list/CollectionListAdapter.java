package org.musicbrainz.mobile.adapter.list;

import java.util.List;

import org.musicbrainz.android.api.data.UserCollectionInfo;
import org.musicbrainz.mobile.R;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CollectionListAdapter extends ArrayAdapter<UserCollectionInfo> {

    private Activity context;
    private List<UserCollectionInfo> userCollections;
    
    public CollectionListAdapter(Activity context, List<UserCollectionInfo> userCollections) {
        super(context, R.layout.list_collection, userCollections);
        this.context = context;
        this.userCollections = userCollections;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View collectionView = convertView;
        CollectionInfoHolder holder = null;
        
        if (collectionView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            collectionView = inflater.inflate(R.layout.list_collection, parent, false);
            holder = new CollectionInfoHolder(collectionView);
            collectionView.setTag(holder);
        } else {
            holder = (CollectionInfoHolder) collectionView.getTag();
        }
        
        UserCollectionInfo collection = userCollections.get(position);
        holder.getTitle().setText(collection.getName());
        holder.getCount().setText(getReleasesDisplayText(collection));
        return collectionView;
    }

    private String getReleasesDisplayText(UserCollectionInfo collection) {
        Resources res = context.getResources();
        return res.getQuantityString(R.plurals.release_plurals, collection.getCount(), collection.getCount());
    }
    
    private class CollectionInfoHolder {
        View base;
        TextView title = null;
        TextView count = null;
        
        CollectionInfoHolder(View base) {
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
