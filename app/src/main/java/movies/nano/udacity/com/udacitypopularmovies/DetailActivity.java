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
    TextView releaseDate;

    CollapsingToolbarLayout collapsingToolBar;

    ActionBar actionBar;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private int mMaxScrollSize;

    List<MovieData> movieList;
    MovieData[] movieData;
    MovieData movieDetails;

    String parcelableMovieResponse = "nano.movie.parcelableData";
    MovieRequestResponse mResponse;
    int positionSelected;
    String POSITION_SELECTED = "position_selected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);


        if(savedInstanceState == null){

            Bundle extras = getIntent().getExtras();

            mResponse = extras.getParcelable(parcelableMovieResponse);
            positionSelected = getIntent().getIntExtra("key_position", 0);

        }else{

            mResponse = savedInstanceState.getParcelable(parcelableMovieResponse);
            positionSelected = savedInstanceState.getInt("key_position", 0);

        }



        movieData = mResponse.getMovieData();


        movieList =  Arrays.asList(movieData);

        movieDetails = movieList.get(positionSelected);

        navigationToolbar = (Toolbar) findViewById(R.id.detail_activity_toolbar);
        navigationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Log.e("DetailActivity  Length", "" + movieData.length);


        movieTitle = (TextView)findViewById(R.id.detail_activity_movie_title);
        movieTitle.setText(movieDetails.getTitle());
        Log.e("DetailActivity Title", "" + movieDetails.getTitle());

        movieRating = (TextView) findViewById(R.id.rating);
        movieRating.setText(String.valueOf(movieDetails.getVote_average()));

        Log.e("Detail Vote Average", String.valueOf(movieDetails.getVote_average()));

        plotSynopsis = (TextView) findViewById(R.id.detail_activity_plot_synopsis);
        plotSynopsis.setText(movieDetails.getOverview());

        collapsingToolBar = (CollapsingToolbarLayout)findViewById(R.id.detail_activity_collapsing_toolbar);

        posterImage = (ImageView)findViewById(R.id.detail_activity_poster_image);
        String posterUrl = posterUrl(movieDetails.getPosterPath());

        Log.e("Detail posterUrl", posterUrl);

        Picasso.with(this).load(posterUrl).into(posterImage);
        posterImage.setAdjustViewBounds(true);

        backDropImage = (ImageView) findViewById(R.id.detail_activity_backdrop_image);
        String backdropUrl = posterUrl(movieDetails.getPosterPath());

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
        outState.putParcelable(parcelableMovieResponse, mResponse);
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
