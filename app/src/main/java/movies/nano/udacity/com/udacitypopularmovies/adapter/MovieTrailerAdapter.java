package movies.nano.udacity.com.udacitypopularmovies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import movies.nano.udacity.com.udacitypopularmovies.R;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieTrailer;
import movies.nano.udacity.com.udacitypopularmovies.utility.RequestConstants;

/**
 * Created by Zoheb Syed on 26-04-2016.
 */
public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.TrailerViewHolder> implements RequestConstants{

    Context mContext;
    List<MovieTrailer> trailersList;

    public MovieTrailerAdapter(Context context, List<MovieTrailer> trailersList){

        mContext = context;
        this.trailersList = trailersList;

    }


    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.movie_item, parent, false);

        TrailerViewHolder trailerViewHolder = new TrailerViewHolder(inflatedView);

        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

        MovieTrailer movieTrailer = trailersList.get(position);
        String trailerThumb = posterUrl(movieTrailer.getTrailerKey());

        ImageView posterView = holder.trailerPosterImage;
        Picasso.with(mContext).load(trailerThumb).placeholder(R.drawable.error_placeholder).into(posterView);
        posterView.setAdjustViewBounds(true);
        
    }



    @Override
    public int getItemCount() {
        return trailersList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected ImageView trailerPosterImage;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerPosterImage = (ImageView)itemView.findViewById(R.id.poster_image);
            trailerPosterImage.setPadding(8,8,8,8);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();
            MovieTrailer  movieTrailer = trailersList.get(position);
            watchTrailer(movieTrailer.getTrailerKey());

        }
    }

    public String posterUrl(String key) {

        Uri uriBuilder = Uri.parse(youtubeThumbPath).buildUpon().appendPath(key).appendPath(defaultThumb).build();
        String posterUrl = uriBuilder.toString();

        return posterUrl;
    }

    public void watchTrailer(String keyID){

        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + keyID));
            mContext.startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youTubeVideoPath+keyID));
            mContext.startActivity(intent);
        }


    }
}
