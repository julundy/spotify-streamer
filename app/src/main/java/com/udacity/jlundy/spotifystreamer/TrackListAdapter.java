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
 * Created by jlundy on 7/11/15.
 */
public class TrackListAdapter extends ArrayAdapter<MyTrack> {

    private final String ICON_DEFAULT_URL = "https://rogueamoeba.com/support/knowledgebase/images/spotify128.png";
    private final String LOG_TAG = TrackListAdapter.class.getSimpleName();

    private Context context;
    public ArrayList<MyTrack> tracks;

    public TrackListAdapter(Context context, ArrayList<MyTrack> tracks) {
        super(context, R.layout.list_item_track);
        this.context = context;
        this.tracks = tracks;
    }

    private static class ViewHolder {
        TextView trackView;
        TextView albumView;
        ImageView iconView;
    }

    /*
    Adds all tracks in ArrayList form.
     */
    public void addAll(ArrayList<MyTrack> tracks) { this.tracks = tracks; }

    @Override
    public int getCount() { return tracks.size(); }

    @Override
    public MyTrack getItem(int position) { return tracks.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    public View getView(int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_track, parent, false);
            holder.trackView = (TextView) convertView.findViewById(R.id.track_name);
            holder.albumView = (TextView) convertView.findViewById(R.id.album_name);
            holder.iconView = (ImageView) convertView.findViewById(R.id.track_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyTrack track = tracks.get(position);

        String albumName = track.getAlbumName();
        String trackName = track.getTrackName();
        holder.albumView.setText(albumName);
        holder.trackView.setText(trackName);
        Picasso.with(context).load(track.getImageUrl()).resize(180, 180)
                .centerInside().into(holder.iconView);

        return convertView;
    }
}
