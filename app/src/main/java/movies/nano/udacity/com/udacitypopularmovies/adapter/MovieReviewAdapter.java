package movies.nano.udacity.com.udacitypopularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import movies.nano.udacity.com.udacitypopularmovies.R;
import movies.nano.udacity.com.udacitypopularmovies.model.MovieReview;


/**
 * Created by Zoheb Syed on 26-04-2016.
 */
public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewViewHolder> {

    Context mContext;
    List<MovieReview> reviewList;

    public MovieReviewAdapter(Context context, List<MovieReview> reviewList){

        mContext = context;
        this.reviewList = reviewList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.review_item, parent, false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(inflatedView);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

        MovieReview movieReview = reviewList.get(position);
        holder.authorName.setText(movieReview.getAuthor());
        holder.reviewContent.setText(movieReview.getContent());

    }

    @Override
    public int getItemCount() {

        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView authorName;
        TextView reviewContent;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            authorName = (TextView)itemView.findViewById(R.id.detail_activity_author_name);
            reviewContent = (TextView)itemView.findViewById(R.id.detail_activity_reviews_header);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
