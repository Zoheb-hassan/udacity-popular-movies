package movies.nano.udacity.com.udacitypopularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.util.Arrays;
import java.util.List;

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


    public MainAcivityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.movies_grid);

        if(savedInstanceState == null) {

            makeAWish(true);


        }else {

            mResponse = savedInstanceState.getParcelable(parcelableMovieResponse);
            movieData = mResponse.getMovieData();


            movieList =  Arrays.asList(movieData);
            movieListAdapter = new MovieListAdapter(getActivity(), movieList);
            gridView.setAdapter(movieListAdapter);

        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("key_position", position);

                movieData = mResponse.getMovieData();
                intent.putExtra(parcelableMovieData,movieData[position]);

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

                movieList =  Arrays.asList(movieData);
                movieListAdapter = new MovieListAdapter(getActivity(), movieList);
                gridView.setAdapter(movieListAdapter);

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

