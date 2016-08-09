package movies.nano.udacity.com.udacitypopularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import movies.nano.udacity.com.udacitypopularmovies.fragments.MainAcivityFragment;
import movies.nano.udacity.com.udacitypopularmovies.fragments.NavigationBackListener;
import movies.nano.udacity.com.udacitypopularmovies.fragments.PositionChangeListener;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieData;

/**
 * Created by Zoheb Syed on 23-12-2015.
 */
public class MainActivity extends AppCompatActivity implements PositionChangeListener, NavigationBackListener{

    Toolbar toolbar;
    String parcelableMovieData = "nano.movie.parcelableMovieData";
    FrameLayout movie_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        movie_grid = (FrameLayout) findViewById(R.id.movie_grid);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MainAcivityFragment mainAcivityFragment = new MainAcivityFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(savedInstanceState == null && movie_grid == null){

            fragmentTransaction.add(R.id.mainContainer, mainAcivityFragment);
            fragmentTransaction.commit();


        }else {

            fragmentTransaction.add(R.id.movie_grid, mainAcivityFragment);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    //Swtchng the Detaiil View on Clicks
    @Override
    public void onPositionChangeListener(MovieData movieData,int positionSelected) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("key_position", positionSelected);
        intent.putExtra(parcelableMovieData, movieData);

        startActivity(intent);
    }

    //Back Navgaton dsabled from the Detail View
    @Override
    public void backPressed() {

    }
}
