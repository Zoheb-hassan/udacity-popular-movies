package movies.nano.udacity.com.udacitypopularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zoheb Syed on 21-04-2016.
 */
public class MovieReviewResponse implements Parcelable {

    @SerializedName("id")
    private int movieId;

    @SerializedName("page")
    private int pageNumber;

    @SerializedName("results")
    private MovieReview[] movieReviews;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public MovieReview[] getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(MovieReview[] movieReviews) {
        this.movieReviews = movieReviews;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.movieId);
        dest.writeInt(this.pageNumber);
        dest.writeTypedArray(this.movieReviews, flags);
        dest.writeInt(this.totalPages);
        dest.writeInt(this.totalResults);
    }

    public MovieReviewResponse() {
    }

    protected MovieReviewResponse(Parcel in) {
        this.movieId = in.readInt();
        this.pageNumber = in.readInt();
        this.movieReviews = in.createTypedArray(MovieReview.CREATOR);
        this.totalPages = in.readInt();
        this.totalResults = in.readInt();
    }

    public static final Parcelable.Creator<MovieReviewResponse> CREATOR = new Parcelable.Creator<MovieReviewResponse>() {
        @Override
        public MovieReviewResponse createFromParcel(Parcel source) {
            return new MovieReviewResponse(source);
        }

        @Override
        public MovieReviewResponse[] newArray(int size) {
            return new MovieReviewResponse[size];
        }
    };
}
