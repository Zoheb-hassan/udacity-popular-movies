package movies.nano.udacity.com.udacitypopularmovies;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
public class DetailActivity extends AppCompatActivity implements RequestConstants, AppBarLayout.OnOffsetChangedListener{

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
    private int [] genreIds;
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
    List<Fragment> fragmentList =  new ArrayList<Fragment>();

    private static int REQUEST_TRAILERS = 0;
    private static int REQUEST_REVIEWS = 1;
    ProgressDialog progressDialog;
    MyVolley volley;
    MovieTrailerResponse movieTrailerResponse;
    MovieTrailer []movieTrailerArray;
    List<MovieTrailer> movieTrailerList;

    MovieReviewResponse movieReviewResponse;
    MovieReview[] movieReviewArray;
    List<MovieReview> movieReviewList;
    private boolean updateUIFlag = false;

    RecyclerView trailerView;
    MovieTrailerAdapter movieTrailerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);


        if(savedInstanceState == null){

            Bundle extras = getIntent().getExtras();

            movieDetails = extras.getParcelable(parcelableMovieData);
            positionSelected = getIntent().getIntExtra("key_position", 0);

        }else{

            movieDetails = savedInstanceState.getParcelable(parcelableMovieData);
            positionSelected = savedInstanceState.getInt("key_position", 0);

        }

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

        title = title+" - ("+String.valueOf(releaseYear)+")";

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

        movieTitle = (TextView)findViewById(R.id.detail_activity_movie_title);
        movieTitle.setText(title);

        movieRating = (TextView) findViewById(R.id.rating);

        String rating = String.format("%.1f", vote_average);
        rating = rating.concat("/10");
        movieRating.setText(rating);


        plotSynopsis = (TextView) findViewById(R.id.detail_activity_plot_synopsis);
        plotSynopsis.setText(overview);

        collapsingToolBar = (CollapsingToolbarLayout)findViewById(R.id.detail_activity_collapsing_toolbar);

        posterImage = (ImageView)findViewById(R.id.detail_activity_poster_image);
        String posterUrl = posterUrl(posterPath);


        Picasso.with(this).load(posterUrl).into(posterImage);
        posterImage.setAdjustViewBounds(true);

        backDropImage = (ImageView) findViewById(R.id.detail_activity_backdrop_image);
        String backdropUrl = posterUrl(backdrop_path);


        Picasso.with(this).load(backdropUrl).into(backDropImage);
        backDropImage.setAdjustViewBounds(true);


        appBarLayout = (AppBarLayout)findViewById(R.id.detail_activity_appbarlayout);

        mMaxScrollSize = appBarLayout.getTotalScrollRange();

        appBarLayout.addOnOffsetChangedListener(this);

        trailerView = (RecyclerView)findViewById(R.id.detail_activity_trailer_view);
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        trailerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        trailerView.setLayoutManager(trailerLayoutManager);

        movieTrailerList = new ArrayList<MovieTrailer>();
        movieTrailerList.clear();
        movieTrailerAdapter = new MovieTrailerAdapter(DetailActivity.this, movieTrailerList);
        trailerView.setAdapter(movieTrailerAdapter);

        requestTrailers();
        requestReviews();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(parcelableMovieData, movieDetails);
        outState.putInt("key_position",positionSelected);


    }

    public String posterUrl(String posterPath) {

        Uri uriBuilder = Uri.parse(posterBasePath).buildUpon().appendPath(imageResolution).build();
        String posterUrl = uriBuilder.toString();

        return posterUrl+posterPath;
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


    private Response.Listener <MovieTrailerResponse>  createTrailerReqSuccessListener(){

        return new Response.Listener<MovieTrailerResponse>() {
            @Override
            public void onResponse(MovieTrailerResponse response) {

                movieTrailerResponse = response;
                movieTrailerArray = movieTrailerResponse.getTrailerList();
                movieTrailerList.clear();
                movieTrailerList.addAll(Arrays.asList(movieTrailerArray));
                movieTrailerAdapter.notifyDataSetChanged();

                if(updateUIFlag){
                    progressDialog.dismiss();
                    //Todo Some Method to update the UI
                }else{
                    updateUIFlag  = true;
                }


            }
        };
    }

    public void requestReviews(){


        GsonRequest<MovieReviewResponse> movieReviewRequest = new GsonRequest<MovieReviewResponse>(Request.Method.GET, requestTrailerReviews(REQUEST_REVIEWS, movieID), MovieReviewResponse.class, createReviewSuccessListener(), createMyReqErrorListener());
        volley.addToRequestQueue(movieReviewRequest);

    }

    private Response.Listener<MovieReviewResponse> createReviewSuccessListener(){
        return new Response.Listener<MovieReviewResponse>() {
            @Override
            public void onResponse(MovieReviewResponse response) {

                movieReviewResponse = response;
                movieReviewArray = movieReviewResponse.getMovieReviews();
                movieReviewList = Arrays.asList(movieReviewArray);

                if(updateUIFlag){
                    progressDialog.dismiss();
                    //Todo Some Method to update the UI
                }else{

                    updateUIFlag = true;

                }



            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener(){

        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(DetailActivity.class.getSimpleName(), error.toString());


            }
        };
    }

    public String requestTrailerReviews(int requestCode, int movieID) {

        Uri uriBuilder = null;

        if(requestCode == 0 )
            uriBuilder = Uri.parse(movieDetailsPath).buildUpon().appendPath(String.valueOf(movieID)).appendPath(trailers_path).appendQueryParameter(apiKey, key).build();

        else if(requestCode == 1)
            uriBuilder = Uri.parse(movieDetailsPath).buildUpon().appendPath(String.valueOf(movieID)).appendPath(reviews_path).appendQueryParameter(apiKey,key).build();


        return uriBuilder.toString();

    }
}
