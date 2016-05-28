package movies.nano.udacity.com.udacitypopularmovies;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import movies.nano.udacity.com.udacitypopularmovies.adapter.ChildLayoutMeasureManager;
import movies.nano.udacity.com.udacitypopularmovies.adapter.MovieReviewAdapter;
import movies.nano.udacity.com.udacitypopularmovies.adapter.MovieTrailerAdapter;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieData;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieReview;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieReviewResponse;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieTrailer;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieTrailerResponse;
import movies.nano.udacity.com.udacitypopularmovies.utility.GsonRequest;
import movies.nano.udacity.com.udacitypopularmovies.utility.MyVolley;
import movies.nano.udacity.com.udacitypopularmovies.utility.RequestConstants;

/**
 * Created by Zoheb Syed on 23-12-2015.
 */
public class DetailActivity extends AppCompatActivity implements RequestConstants, AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    Toolbar navigationToolbar;
    AppBarLayout appBarLayout;
    ImageView posterImage;
    ImageView backDropImage;

    TextView movieTitle;
    TextView movieRating;
    TextView plotSynopsis;
    TextView releaseDateTv;

    CollapsingToolbarLayout collapsingToolBar;

    ActionBar actionBar;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private int mMaxScrollSize;

    MovieData movieDetails;

    String parcelableMovieData = "nano.movie.parcelableMovieData";

    int positionSelected;
    String POSITION_SELECTED = "position_selected";


    private String posterPath;
    private boolean isAdult;
    private String overview;
    private String releaseDate;
    private int[] genreIds;
    private int movieID;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private double popularity;
    private int vote_count;
    private boolean isVideo;
    private double vote_average;

    static String TAG = "DetailAcivity";
    ViewPager movieDetailPager;
    TabLayout tabs;
    List<Fragment> fragmentList = new ArrayList<Fragment>();

    private static int REQUEST_TRAILERS = 0;
    private static int REQUEST_REVIEWS = 1;
    ProgressDialog progressDialog;
    MyVolley volley;
    MovieTrailerResponse movieTrailerResponse;
    MovieTrailer[] movieTrailerArray;
    List<MovieTrailer> movieTrailerList;

    MovieReviewResponse movieReviewResponse;
    MovieReview[] movieReviewArray;
    List<MovieReview> movieReviewList;
    private boolean updateUIFlag = false;

    RecyclerView trailerView;
    MovieTrailerAdapter movieTrailerAdapter;

    RecyclerView reviewView;
    MovieReviewAdapter movieReviewAdapter;

    List<MovieData> favoriteList;
    FloatingActionButton fabButton;
    boolean isFavorited;
    SharedPreferences sharedPreferences;

    public static final String FAVORITE_LIST = "favorites";

    //Todo Store values in Shared Preferences and delete or update them based on the fab button select/unselect
    //Todo add them to the list of favorited movies

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);


        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            movieDetails = extras.getParcelable(parcelableMovieData);
            positionSelected = getIntent().getIntExtra("key_position", 0);

        } else {

            movieDetails = savedInstanceState.getParcelable(parcelableMovieData);
            positionSelected = savedInstanceState.getInt("key_position", 0);

        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        fabButton = (FloatingActionButton)findViewById(R.id.favorite);
        fabButton.setOnClickListener(this);

        navigationToolbar = (Toolbar) findViewById(R.id.detail_activity_toolbar);
        navigationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = movieDetails.getTitle();
        vote_average = movieDetails.getVote_average();
        overview = movieDetails.getOverview();
        posterPath = movieDetails.getPosterPath();
        backdrop_path = movieDetails.getBackdrop_path();
        releaseDate = movieDetails.getReleaseDate();


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(releaseDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int releaseYear = calendar.get(Calendar.YEAR);

        title = title + " - (" + String.valueOf(releaseYear) + ")";

        /*
        popularity = movieDetails.getPopularity();
        vote_count = movieDetails.getVote_count();
        isVideo = movieDetails.isVideo();


        isAdult = movieDetails.isAdult();


        genreIds = movieDetails.getGenreIds();
        original_title = movieDetails.getOriginal_title();
        original_language = movieDetails.getOriginal_language();
        */

        movieID = movieDetails.getMovieID();

        movieTitle = (TextView) findViewById(R.id.detail_activity_movie_title);
        movieTitle.setText(title);

        movieRating = (TextView) findViewById(R.id.rating);

        String rating = String.format("%.1f", vote_average);
        rating = rating.concat("/10");
        movieRating.setText(rating);


        plotSynopsis = (TextView) findViewById(R.id.detail_activity_plot_synopsis);
        plotSynopsis.setText(overview);

        collapsingToolBar = (CollapsingToolbarLayout) findViewById(R.id.detail_activity_collapsing_toolbar);

        posterImage = (ImageView) findViewById(R.id.detail_activity_poster_image);
        String posterUrl = posterUrl(posterPath);


        Picasso.with(this).load(posterUrl).into(posterImage);
        posterImage.setAdjustViewBounds(true);

        backDropImage = (ImageView) findViewById(R.id.detail_activity_backdrop_image);
        String backdropUrl = posterUrl(backdrop_path);


        Picasso.with(this).load(backdropUrl).into(backDropImage);
        backDropImage.setAdjustViewBounds(true);


        appBarLayout = (AppBarLayout) findViewById(R.id.detail_activity_appbarlayout);

        mMaxScrollSize = appBarLayout.getTotalScrollRange();

        appBarLayout.addOnOffsetChangedListener(this);

        trailerView = (RecyclerView) findViewById(R.id.detail_activity_trailer_view);
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        trailerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        trailerView.setLayoutManager(trailerLayoutManager);

        movieTrailerList = new ArrayList<MovieTrailer>();
        movieTrailerList.clear();
        movieTrailerAdapter = new MovieTrailerAdapter(DetailActivity.this, movieTrailerList);
        trailerView.setAdapter(movieTrailerAdapter);

        reviewView = (RecyclerView) findViewById(R.id.detail_activity_reviews_view);
//        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        reviewsLayoutManager.setAutoMeasureEnabled(true);

        ChildLayoutMeasureManager reviewsLayoutManager = new ChildLayoutMeasureManager(this, LinearLayoutManager.VERTICAL, true);
        reviewsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reviewView.setLayoutManager(reviewsLayoutManager);

        movieReviewList = new ArrayList<MovieReview>();
        movieReviewList.clear();
        movieReviewAdapter = new MovieReviewAdapter(DetailActivity.this, movieReviewList);
        reviewView.setAdapter(movieReviewAdapter);

        checkIfFavorited();

        requestTrailers();
        requestReviews();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(parcelableMovieData, movieDetails);
        outState.putInt("key_position", positionSelected);


    }

    public String posterUrl(String posterPath) {

        Uri uriBuilder = Uri.parse(posterBasePath).buildUpon().appendPath(imageResolution).build();
        String posterUrl = uriBuilder.toString();

        return posterUrl + posterPath;
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            posterImage.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            posterImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }

    }

    private void requestTrailers() {

        progressDialog = new ProgressDialog(DetailActivity.this);
        progressDialog.setMessage("Fetching Movies....");
        progressDialog.show();
        volley = MyVolley.getInstance(DetailActivity.this);

        GsonRequest<MovieTrailerResponse> movieTrailerRequest = new GsonRequest(Request.Method.GET, requestTrailerReviews(REQUEST_TRAILERS, movieID), MovieTrailerResponse.class, createTrailerReqSuccessListener(), createMyReqErrorListener());
        volley.addToRequestQueue(movieTrailerRequest);

    }


    private Response.Listener<MovieTrailerResponse> createTrailerReqSuccessListener() {

        return new Response.Listener<MovieTrailerResponse>() {
            @Override
            public void onResponse(MovieTrailerResponse response) {

                movieTrailerResponse = response;
                movieTrailerArray = movieTrailerResponse.getTrailerList();
                movieTrailerList.clear();
                movieTrailerList.addAll(Arrays.asList(movieTrailerArray));


                if (updateUIFlag) {
                    progressDialog.dismiss();
                    notifyAdapter();
                } else {
                    updateUIFlag = true;
                }


            }
        };
    }

    public void requestReviews() {


        GsonRequest<MovieReviewResponse> movieReviewRequest = new GsonRequest<MovieReviewResponse>(Request.Method.GET, requestTrailerReviews(REQUEST_REVIEWS, movieID), MovieReviewResponse.class, createReviewSuccessListener(), createMyReqErrorListener());
        volley.addToRequestQueue(movieReviewRequest);

    }

    private Response.Listener<MovieReviewResponse> createReviewSuccessListener() {
        return new Response.Listener<MovieReviewResponse>() {
            @Override
            public void onResponse(MovieReviewResponse response) {

                movieReviewResponse = response;
                movieReviewArray = movieReviewResponse.getMovieReviews();

                movieReviewList.clear();
                movieReviewList.addAll(Arrays.asList(movieReviewArray));


                if (updateUIFlag) {
                    progressDialog.dismiss();
                    notifyAdapter();
                } else {

                    updateUIFlag = true;

                }


            }
        };
    }

    private void notifyAdapter() {

        movieReviewAdapter.notifyDataSetChanged();
        movieTrailerAdapter.notifyDataSetChanged();

    }

    private Response.ErrorListener createMyReqErrorListener() {

        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(DetailActivity.class.getSimpleName(), error.toString());


            }
        };
    }

    public String requestTrailerReviews(int requestCode, int movieID) {

        Uri uriBuilder = null;

        if (requestCode == 0)
            uriBuilder = Uri.parse(movieDetailsPath).buildUpon().appendPath(String.valueOf(movieID)).appendPath(trailers_path).appendQueryParameter(apiKey, key).build();

        else if (requestCode == 1)
            uriBuilder = Uri.parse(movieDetailsPath).buildUpon().appendPath(String.valueOf(movieID)).appendPath(reviews_path).appendQueryParameter(apiKey, key).build();


        return uriBuilder.toString();

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.favorite:
                isFavorited = !isFavorited;
                flipFabResource(isFavorited);
                addRemoveToFavList();

        }

    }

    private void addRemoveToFavList() {

        SharedPreferences.Editor edit = sharedPreferences.edit();
        String storedList = sharedPreferences.getString(FAVORITE_LIST,null);
        Gson gson = new Gson();

        if(storedList == null){

            favoriteList = new ArrayList<>();

        }else{

            Type typeToken = new TypeToken<List<MovieData>>(){}.getType();
            favoriteList = gson.fromJson(storedList, typeToken);
        }


        if(isFavorited){

            favoriteList.add(movieDetails);
            String convertedList = gson.toJson(favoriteList);
            edit.putString(FAVORITE_LIST, convertedList);
            edit.putString(String.valueOf(movieID),String.valueOf(movieID));

        }else{

            for(int i=0; i<favoriteList.size();i++){

                MovieData data = favoriteList.get(i);
                if(movieID == data.getMovieID()){
                    favoriteList.remove(i);
                    edit.remove(String.valueOf(data.getMovieID()));
                }
            }

            String mutatedList = gson.toJson(favoriteList);
            edit.putString(FAVORITE_LIST, mutatedList);
        }

        edit.commit();

    }

    private void checkIfFavorited() {

        String ifExists = sharedPreferences.getString(String.valueOf(movieID), null);
        if(ifExists==null)
            isFavorited = false;
        else
            isFavorited = true;

        flipFabResource(isFavorited);

    }

    private void flipFabResource(boolean isFavorited) {

        if(isFavorited)
            fabButton.setImageResource(R.drawable.star);
        else
            fabButton.setImageResource(R.drawable.star_outline);
    }

}
