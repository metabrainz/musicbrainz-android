package org.metabrainz.android.adapter.list;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.metabrainz.android.R;
import org.metabrainz.android.api.data.UserSearchResult;

import java.util.List;

import android.content.Context;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CollectionListAdapter extends ArrayAdapter<UserSearchResult> {

    private Context context;
    private List<UserSearchResult> userCollections;
    
    public CollectionListAdapter(Context context, List<UserSearchResult> userCollections) {
        super(context, R.layout.list_collection, userCollections);
        this.context = context;
        this.userCollections = userCollections;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View collectionView = convertView;
        CollectionInfoHolder holder = null;
        
        if (collectionView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            collectionView = inflater.inflate(R.layout.list_collection, parent, false);
            holder = new CollectionInfoHolder(collectionView);
            collectionView.setTag(holder);
        } else {
            holder = (CollectionInfoHolder) collectionView.getTag();
        }
        
        UserSearchResult collection = userCollections.get(position);
        holder.getTitle().setText(collection.getName());
        holder.getCount().setText(getReleasesDisplayText(collection));
        return collectionView;
    }

    private String getReleasesDisplayText(UserSearchResult collection) {
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
