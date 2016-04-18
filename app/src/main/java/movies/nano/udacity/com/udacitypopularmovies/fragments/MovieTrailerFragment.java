package movies.nano.udacity.com.udacitypopularmovies.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import movies.nano.udacity.com.udacitypopularmovies.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieTrailerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieTrailerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieTrailerFragment extends Fragment {


    public MovieTrailerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_trailer, container, false);
    }



}
