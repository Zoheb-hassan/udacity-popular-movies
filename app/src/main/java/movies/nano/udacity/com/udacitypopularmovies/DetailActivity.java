package movies.nano.udacity.com.udacitypopularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import movies.nano.udacity.com.udacitypopularmovies.fragments.MovieDetailsFragment;
import movies.nano.udacity.com.udacitypopularmovies.fragments.NavigationBackListener;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieData;

/**
 * Created by Zoheb Syed on 23-12-2015.
 */
public class DetailActivity extends AppCompatActivity implements NavigationBackListener{

    //Todo Refactor for the two pane layout, Seperation into activity fragment
    MovieData movieDetails;
    String parcelableMovieData = "nano.movie.parcelableMovieData";
    int positionSelected;
    String POSITION_SELECTED = "position_selected";
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        frameLayout = (FrameLayout)findViewById(R.id.detail_activity_framelayout);

        if (savedInstanceState == null) {

            //Bundle we receive from the activity
            Bundle extras = getIntent().getExtras();
            movieDetails = extras.getParcelable(parcelableMovieData);
            positionSelected = getIntent().getIntExtra("key_position", 0);

        } else {

            //Handling orientation change via the parcel
            movieDetails = savedInstanceState.getParcelable(parcelableMovieData);
            positionSelected = savedInstanceState.getInt("key_position", 0);

        }

        //Inititate Fragment via the details obtained
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.getInstance(movieDetails,true);
        fragmentTransaction.add(R.id.detail_activity_framelayout, movieDetailsFragment);
        fragmentTransaction.commit();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(parcelableMovieData, movieDetails);
        outState.putInt("key_position", positionSelected);
    }

    //Listener's back button
    @Override
    public void backPressed() {

        onBackPressed();
    }

}
