package movies.nano.udacity.com.udacitypopularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import movies.nano.udacity.com.udacitypopularmovies.model.MovieData;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieRequestResponse;
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


        posterPath = movieDetails.getPosterPath();
        isAdult = movieDetails.isAdult();
        overview = movieDetails.getOverview();
        releaseDate = movieDetails.getReleaseDate();
        genreIds = movieDetails.getGenreIds();
        movieID = movieDetails.getMovieID();
        original_title = movieDetails.getOriginal_title();
        original_language = movieDetails.getOriginal_language();
        title = movieDetails.getTitle();
        backdrop_path = movieDetails.getBackdrop_path();
        popularity = movieDetails.getPopularity();
        vote_count = movieDetails.getVote_count();
        isVideo = movieDetails.isVideo();
        vote_average = movieDetails.getVote_average();


        movieTitle = (TextView)findViewById(R.id.detail_activity_movie_title);
        movieTitle.setText(title);
        Log.e("DetailActivity Title", "" + movieDetails.getTitle());

        movieRating = (TextView) findViewById(R.id.rating);
        movieRating.setText(String.valueOf(vote_average));

        Log.e("Detail Vote Average", String.valueOf(movieDetails.getVote_average()));

        plotSynopsis = (TextView) findViewById(R.id.detail_activity_plot_synopsis);
        plotSynopsis.setText(overview);

        collapsingToolBar = (CollapsingToolbarLayout)findViewById(R.id.detail_activity_collapsing_toolbar);

        posterImage = (ImageView)findViewById(R.id.detail_activity_poster_image);
        String posterUrl = posterUrl(posterPath);

        Log.e("Detail posterUrl", posterUrl);

        Picasso.with(this).load(posterUrl).into(posterImage);
        posterImage.setAdjustViewBounds(true);

        backDropImage = (ImageView) findViewById(R.id.detail_activity_backdrop_image);
        String backdropUrl = posterUrl(backdrop_path);

        Log.e("Detail posterUrl", backdropUrl);

        Picasso.with(this).load(backdropUrl).into(posterImage);
        backDropImage.setAdjustViewBounds(true);


        appBarLayout = (AppBarLayout)findViewById(R.id.detail_activity_appbarlayout);

        mMaxScrollSize = appBarLayout.getTotalScrollRange();

        appBarLayout.addOnOffsetChangedListener(this);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(parcelableMovieData, movieDetails);
        outState.putInt("key_position",positionSelected);


    }

    public String posterUrl(String posterPath) {

        Uri uriBuilder = Uri.parse(posterBasePath).buildUpon().appendPath(imageResolution).build();
        //.appendPath(posterPath)
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
}
