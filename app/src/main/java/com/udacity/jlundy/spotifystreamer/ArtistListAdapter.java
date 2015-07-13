package com.udacity.jlundy.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jlundy on 7/6/15.
 */
public class ArtistListAdapter extends ArrayAdapter<MyArtist> {

    private final String LOG_TAG = ArtistListAdapter.class.getSimpleName();

    private final String ICON_DEFAULT_URL = "https://rogueamoeba.com/support/knowledgebase/images/spotify128.png";

    private final Context context;
    private ArrayList<MyArtist> artists;

    public ArtistListAdapter(Context context, ArrayList<MyArtist> artists) {
        super(context, R.layout.list_item_artist);
        this.context = context;
        this.artists = artists;
    }


    private static class ViewHolder {
        TextView name;
        ImageView icon;
    }

    public void addAll(ArrayList<MyArtist> artistList) {
        this.artists = artistList;
    }

    @Override
    public int getCount() {
        return artists.size();
    }


    @Override
    public MyArtist getItem(int position) { return artists.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    public View getView(int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater  = LayoutInflater.from(context);
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_artist, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.artist_name);
            holder.icon = (ImageView) convertView.findViewById(R.id.artist_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyArtist artist = artists.get(position);

        holder.name.setText(artist.getArtistName());
        Picasso.with(context).load(artist.getImageUrl()).resize(180, 180)
                .centerInside().into(holder.icon);

        return convertView;
    }
}
