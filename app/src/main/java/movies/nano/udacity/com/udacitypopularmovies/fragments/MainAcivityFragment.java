package movies.nano.udacity.com.udacitypopularmovies.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import movies.nano.udacity.com.udacitypopularmovies.DetailActivity;
import movies.nano.udacity.com.udacitypopularmovies.R;
import movies.nano.udacity.com.udacitypopularmovies.adapter.MovieListAdapter;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieData;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieRequestResponse;
import movies.nano.udacity.com.udacitypopularmovies.utility.GsonRequest;
import movies.nano.udacity.com.udacitypopularmovies.utility.MyVolley;
import movies.nano.udacity.com.udacitypopularmovies.utility.RequestConstants;

/**
 * Created by Zoheb Syed on 23-12-2015.
 */

public class MainAcivityFragment extends Fragment implements RequestConstants {

    private boolean isDescending;
    MyVolley volley;
    List<MovieData> movieList;


    List<MovieData> movieDataList;
    MainAcivityFragment mainActFrag;

    GridView gridView;
    MovieListAdapter movieListAdapter;
    ProgressDialog progressDialog;
    MovieData[] movieData;
    String parcelableMovieResponse = "nano.movie.parcelableData";
    String parcelableMovieData = "nano.movie.parcelableMovieData";
    MovieRequestResponse mResponse;

    MovieData movieDetails;

    List<MovieData> favoriteList;

    static String TAG = "MainAcivityFragment";


    public MainAcivityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        movieList = new ArrayList<MovieData>();
        movieListAdapter = new MovieListAdapter(getActivity(), movieList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(movieListAdapter);

        if(savedInstanceState == null) {

            makeAWish(true);


        }else {

            mResponse = savedInstanceState.getParcelable(parcelableMovieResponse);
            movieData = mResponse.getMovieData();

            movieList.clear();
            movieList.addAll(Arrays.asList(movieData));
            movieListAdapter.notifyDataSetChanged();

        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("key_position", position);

                movieData = mResponse.getMovieData();

                movieDetails = movieData[position];

                intent.putExtra(parcelableMovieData, movieData[position]);

                startActivity(intent);
            }
        });
        return rootView;
    }

    private void makeAWish(boolean popOrVote) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Movies....");
        progressDialog.show();
        volley = MyVolley.getInstance(getActivity());

        GsonRequest<MovieRequestResponse> movieListRequest = new GsonRequest(Request.Method.GET, requestURL(popOrVote), MovieRequestResponse.class, createMyReqSuccessListener(), createMyReqErrorListener());
        volley.addToRequestQueue(movieListRequest);

    }


    private Response.Listener <MovieRequestResponse>  createMyReqSuccessListener(){

        return new Response.Listener<MovieRequestResponse>() {
            @Override
            public void onResponse(MovieRequestResponse response) {

                mResponse = response;
                movieData = mResponse.getMovieData();

                movieList.clear();
                movieList.addAll(Arrays.asList(movieData));
                movieListAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        };
    }


    private Response.ErrorListener createMyReqErrorListener(){

        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        };
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(parcelableMovieResponse, mResponse);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.movie_sort, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_sort_popularity) {
            makeAWish(true);
            return true;
        }
        if (id == R.id.action_sort_rating) {
            //setSortCriteria(SortCriteria.RATING);
            makeAWish(false);
            return true;
        }

        if(id == R.id.action_favorites){
            //Get the favorites list
            favoriteList = new ArrayList<>();
            //Todo fetch the favorite list from shared prefs and populate in the adapter
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String storedList = sharedPreferences.getString(DetailActivity.FAVORITE_LIST, null);

            Type typeToken = new TypeToken<List<MovieData>>(){}.getType();
            favoriteList = gson.fromJson(storedList, typeToken);

            movieList.clear();
            movieList.addAll(favoriteList);
            movieListAdapter.notifyDataSetChanged();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String requestURL(boolean isDescending) {

        Uri uriBuilder = null;

        if(isDescending)
            uriBuilder = Uri.parse(apiBasePath).buildUpon().appendQueryParameter(sortBy,popDesc).appendQueryParameter(apiKey,key).build();
        else
            uriBuilder = Uri.parse(apiBasePath).buildUpon().appendQueryParameter(sortBy,voteDesc).appendQueryParameter(apiKey,key).build();

        return uriBuilder.toString();

    }


}

