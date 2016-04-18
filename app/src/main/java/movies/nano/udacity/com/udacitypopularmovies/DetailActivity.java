package movies.nano.udacity.com.udacitypopularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import movies.nano.udacity.com.udacitypopularmovies.adapter.MovieListAdapter;
import movies.nano.udacity.com.udacitypopularmovies.adapter.MoviePagerAdapter;
import movies.nano.udacity.com.udacitypopularmovies.fragments.MovieReviewFragment;
import movies.nano.udacity.com.udacitypopularmovies.fragments.MovieSynopsisFragment;
import movies.nano.udacity.com.udacitypopularmovies.fragments.MovieTrailerFragment;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieData;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        MovieSynopsisFragment movieSynopsisFragment = new MovieSynopsisFragment();
        MovieReviewFragment movieReviewFragment = new MovieReviewFragment();
        MovieTrailerFragment movieTrailerFragment = new MovieTrailerFragment();

        fragmentList.add(movieSynopsisFragment);
        fragmentList.add(movieReviewFragment);
        fragmentList.add(movieTrailerFragment);

        movieDetailPager = (ViewPager)findViewById(R.id.movie_detail_pager);
        tabs = (TabLayout)findViewById(R.id.movie_detail_tab);

        MoviePagerAdapter moviePagerAdapter = new MoviePagerAdapter(getSupportFragmentManager(), fragmentList);
        movieDetailPager.setAdapter(moviePagerAdapter);
        tabs.setupWithViewPager(movieDetailPager);

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
        movieID = movieDetails.getMovieID();
        original_title = movieDetails.getOriginal_title();
        original_language = movieDetails.getOriginal_language();
        */


        movieTitle = (TextView)findViewById(R.id.detail_activity_movie_title);
        movieTitle.setText(title);

        movieRating = (TextView) findViewById(R.id.rating);

        String rating = String.format("%.1f", vote_average);
        rating = rating.concat("/10");
        movieRating.setText(rating);


//        plotSynopsis = (TextView) findViewById(R.id.detail_activity_plot_synopsis);
//        plotSynopsis.setText(overview);

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
