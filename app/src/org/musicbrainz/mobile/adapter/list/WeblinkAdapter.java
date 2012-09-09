package org.musicbrainz.mobile.adapter.list;

import java.util.List;

import org.musicbrainz.android.api.data.WebLink;
import org.musicbrainz.mobile.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WeblinkAdapter extends ArrayAdapter<WebLink> {

    private Activity context;
    private List<WebLink> links;

    public WeblinkAdapter(Activity context, List<WebLink> links) {
        super(context, R.layout.list_link, links);
        this.context = context;
        this.links = links;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View link = convertView;
        LinkHolder holder = null;

        if (link == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            link = inflater.inflate(R.layout.list_link, parent, false);
            holder = new LinkHolder(link);
            link.setTag(holder);
        } else {
            holder = (LinkHolder) link.getTag();
        }

        WebLink linkData = links.get(position);
        holder.getLinkTitle().setText(linkData.getPrettyType());
        holder.getLink().setText(linkData.getPrettyUrl());
        return link;
    }

    private class LinkHolder {
        View base;
        TextView linkTitle = null;
        TextView link = null;

        LinkHolder(View base) {
            this.base = base;
        }

        TextView getLinkTitle() {
            if (linkTitle == null) {
                linkTitle = (TextView) base.findViewById(R.id.list_link_title);
            }
            return linkTitle;
        }

        TextView getLink() {
            if (link == null) {
                link = (TextView) base.findViewById(R.id.list_link);
            }
            return link;
        }
    }

}
