package org.metabrainz.mobile.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.WebLink;

import java.util.List;

public class WeblinkAdapter extends ArrayAdapter<WebLink> {

    private Context context;
    private List<WebLink> links;

    public WeblinkAdapter(Context context, List<WebLink> links) {
        super(context, R.layout.list_link, links);
        this.context = context;
        this.links = links;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View link = convertView;
        LinkHolder holder;

        if (link == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            link = inflater.inflate(R.layout.list_link, parent, false);
            holder = new LinkHolder(link);
            link.setTag(holder);
        } else {
            holder = (LinkHolder) link.getTag();
        }

        WebLink linkData = links.get(position);
        holder.getLinkTitle().setText(linkData.getPrettyType());
        holder.getLink().setText(linkData.getPrettyUrl());
        holder.getIcon().setImageResource(findIconForLink(linkData));
        return link;
    }

    private int findIconForLink(WebLink linkData) {
        String type = linkData.getType();
        String url = linkData.getUrl();

        if (type.equalsIgnoreCase("youtube")) {
            return R.drawable.link_youtube;
        } else if (type.equalsIgnoreCase("official homepage")) {
            return R.drawable.link_home;
        } else if (type.equalsIgnoreCase("imdb")) {
            return R.drawable.link_film;
        } else if (type.equalsIgnoreCase("fanpage")) {
            return R.drawable.link_community;
        } else if (type.equalsIgnoreCase("online community")) {
            return R.drawable.link_community;
        } else if (type.equalsIgnoreCase("wikipedia")) {
            return R.drawable.link_info;
        } else if (type.equalsIgnoreCase("lyrics")) {
            return R.drawable.link_lyrics;
        } else if (type.equalsIgnoreCase("download for free")) {
            return R.drawable.link_download;
        } else if (type.equalsIgnoreCase("soundcloud")) {
            return R.drawable.link_soundcloud;
        } else if (type.startsWith("streaming")) {
            return R.drawable.link_streaming;
        } else if (type.startsWith("purchase")) {
            return R.drawable.link_buy;
        } else if (url.contains("twitter")) {
            return R.drawable.link_twitter;
        } else if (url.contains("facebook")) {
            return R.drawable.link_facebook;
        } else if (type.contains("discog")) {
            return R.drawable.link_discog;
        } else if (url.contains("vimeo")) {
            return R.drawable.link_vimeo;
        }

        return R.drawable.link_generic;
    }

    private class LinkHolder {
        View base;
        ImageView icon = null;
        TextView linkTitle = null;
        TextView link = null;

        LinkHolder(View base) {
            this.base = base;
        }

        ImageView getIcon() {
            if (icon == null) {
                icon = (ImageView) base.findViewById(R.id.link_icon);
            }
            return icon;
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
