package com.udacity.jlundy.spotifystreamer;

import android.app.Activity;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by jlundy on 7/6/15.
 */
public class ArtistSearchFragment extends ListFragment {
    private final String LOG_TAG = ArtistSearchFragment.class.getSimpleName();
    static public final String ARTIST_ID = "ARTIST_ID";
    static public final String FRAGMENT_TAG = "ARTIST_SEARCH_FRAGMENT";
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;
    ArrayList<MyArtist> myArtists;

    ArtistListAdapter mArtistAdapter;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    public ArtistSearchFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null) {
            //setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
            myArtists = savedInstanceState.getParcelableArrayList("ARTIST_LIST");
            mArtistAdapter.addAll(myArtists);
            mArtistAdapter.notifyDataSetChanged();
        } else {
            myArtists = new ArrayList<>();
            mArtistAdapter = new ArtistListAdapter(getActivity(), myArtists);
            if (getArguments() != null) {
                String query = getArguments().getString("query_string");
                GetArtistsTask getArtistsTask = new GetArtistsTask();
                getArtistsTask.execute(query);
            }
        }
        setListAdapter(mArtistAdapter);
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }
        mActivatedPosition = position;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        MyArtist artist = mArtistAdapter.getItem(position);
        mCallbacks.onItemSelected(artist.getArtistId());
    }

    public void updateArtist(Bundle bundle) {
        String query = bundle.getString(ArtistSearchActivity.QUERY_STRING);
        GetArtistsTask getArtistsTask = new GetArtistsTask();
        getArtistsTask.execute(query);
    }

    public class GetArtistsTask extends AsyncTask<String, Void, ArrayList<MyArtist>> {

        @Override
        protected ArrayList<MyArtist> doInBackground(String... params) {
            String query = params[0];
            SpotifyApi api = new SpotifyApi();
            ArrayList<MyArtist> artistList = new ArrayList<>();
            try {
                SpotifyService spotify = api.getService();
                ArtistsPager results = spotify.searchArtists(query);
                List<Artist> artists = results.artists.items;
                if (!artists.isEmpty()) {
                    for (Artist artist : artists) {
                        String newArtistName = artist.name;
                        String newArtistId = artist.id;
                        String newImageUrl;
                        if (!artist.images.isEmpty()) {
                            newImageUrl = artist.images.get(artist.images.size()-1).url;
                        } else {
                            newImageUrl = "https://rogueamoeba.com/support/knowledgebase/images/spotify128.png";
                        }
                        MyArtist newArtist = new MyArtist(newArtistId, newArtistName, newImageUrl);
                        artistList.add(newArtist);
                    }
                }
            } catch (Exception e) {
                artistList = null;
            }
            return artistList;
        }

        @Override
        protected void onPostExecute(ArrayList<MyArtist> artistList) {
            super.onPostExecute(artistList);

            if (artistList != null) {
                if (artistList.isEmpty()) {
                    Toast.makeText(getActivity(), "No artists found!", Toast.LENGTH_SHORT).show();
                } else {
                    myArtists = artistList;
                    mArtistAdapter.addAll(artistList);
                    mArtistAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getActivity(), "No connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
        outState.putParcelableArrayList("ARTIST_LIST", myArtists);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }
}
