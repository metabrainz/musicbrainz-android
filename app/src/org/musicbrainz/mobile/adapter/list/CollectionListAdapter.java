/*
 * Copyright (C) 2012 Jamie McDonald
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
