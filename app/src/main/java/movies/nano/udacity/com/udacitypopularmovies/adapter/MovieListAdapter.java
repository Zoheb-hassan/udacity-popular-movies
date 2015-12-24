package movies.nano.udacity.com.udacitypopularmovies.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import movies.nano.udacity.com.udacitypopularmovies.R;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieData;
import movies.nano.udacity.com.udacitypopularmovies.utility.RequestConstants;

/**
 * Created by Zoheb Syed on 23-12-2015.
 */
public class MovieListAdapter extends BaseAdapter implements RequestConstants {

    List<MovieData> movieList;
    Context mcontext;
    ImageView gridImage;

    public MovieListAdapter(Context context, List<MovieData> list){

        movieList = list;
        mcontext = context;

    }


    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return movieList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(convertView == null)
            convertView = LayoutInflater.from(mcontext).inflate(
                    R.layout.movie_item, parent, false);

        gridImage = (ImageView) convertView.findViewById(R.id.poster_image);

        MovieData movieData = movieList.get(position);
        String posterUrl = posterUrl(movieData.getPosterPath());

        Picasso.with(mcontext).load(posterUrl).into(gridImage);
        gridImage.setAdjustViewBounds(true);

        return convertView;
    }

    public String posterUrl(String posterPath) {

        Uri uriBuilder = Uri.parse(posterBasePath).buildUpon().appendPath(imageResolution).build();
        //.appendPath(posterPath)
        String posterUrl = uriBuilder.toString();

        return posterUrl+posterPath;
    }


}

