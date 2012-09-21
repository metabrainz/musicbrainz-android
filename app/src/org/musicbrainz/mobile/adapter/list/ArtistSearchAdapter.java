package org.musicbrainz.mobile.adapter.list;

import java.util.List;

import org.musicbrainz.android.api.data.ArtistSearchResult;
import org.musicbrainz.mobile.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArtistSearchAdapter extends ArrayAdapter<ArtistSearchResult> {

    private Activity context;
    private List<ArtistSearchResult> resultData;

    public ArtistSearchAdapter(Activity context, List<ArtistSearchResult> resultData) {
        super(context, R.layout.list_search_artist, resultData);
        this.context = context;
        this.resultData = resultData;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View artist = convertView;
        ArtistSearchHolder holder = null;

        if (artist == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            artist = inflater.inflate(R.layout.list_search_artist, parent, false);
            holder = new ArtistSearchHolder(artist);
            artist.setTag(holder);
        } else {
            holder = (ArtistSearchHolder) artist.getTag();
        }

        ArtistSearchResult artistData = resultData.get(position);
        holder.getArtistName().setText(artistData.getName());

        TextView option = holder.getDisambiguation();
        String disambiguation = artistData.getDisambiguation();
        option.setText(disambiguation);

        if (disambiguation != null) {
            option.setVisibility(View.VISIBLE);
        } else {
            option.setVisibility(View.GONE);
        }
        return artist;
    }

    private class ArtistSearchHolder {
        View base;
        TextView artistName = null;
        TextView disambiguation = null;

        ArtistSearchHolder(View base) {
            this.base = base;
        }

        TextView getArtistName() {
            if (artistName == null) {
                artistName = (TextView) base.findViewById(R.id.search_artist_name);
            }
            return artistName;
        }

        TextView getDisambiguation() {
            if (disambiguation == null) {
                disambiguation = (TextView) base.findViewById(R.id.search_artist_disambig);
            }
            return disambiguation;
        }
    }

}
