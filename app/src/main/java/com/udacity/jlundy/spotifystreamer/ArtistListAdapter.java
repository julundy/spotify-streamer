package com.udacity.jlundy.spotifystreamer;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jlundy on 7/6/15.
 */
public class ArtistListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final TypedArray nameId;
    private final TypedArray imageId;

    public ArtistListAdapter(Context context, TypedArray nameId, TypedArray imageId) {
        super(context, R.layout.list_item_artist);
        this.context = context;
        this.nameId = nameId;
        this.imageId = imageId;
    }

    private static class ViewHolder {
        TextView name;
        ImageView thumb;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater  = LayoutInflater.from(context);
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_artist, null);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.artist_name);
            holder.thumb = (ImageView) view.findViewById(R.id.thumb_small);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(nameId.getIndex(position));
        holder.thumb.setImageResource(imageId.getIndex(position));
        return view;
    }
}
