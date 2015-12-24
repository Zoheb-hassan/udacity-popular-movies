package movies.nano.udacity.com.udacitypopularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zoheb Syed on 23-12-2015.
 */
public class MovieRequestResponse implements Parcelable {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private MovieData[] movieData;

    @SerializedName("total_results")
    private int total_results;

    @SerializedName("total_pages")
    private int total_pages;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeTypedArray(movieData, flags);
        dest.writeInt(total_results);
        dest.writeInt(total_pages);
    }
    protected MovieRequestResponse(Parcel in) {
        page = in.readInt();
        movieData = in.createTypedArray(MovieData.CREATOR);
        total_results = in.readInt();
        total_pages = in.readInt();
    }

    public static final Creator<MovieRequestResponse> CREATOR = new Creator<MovieRequestResponse>() {
        @Override
        public MovieRequestResponse createFromParcel(Parcel in) {
            return new MovieRequestResponse(in);
        }

        @Override
        public MovieRequestResponse[] newArray(int size) {
            return new MovieRequestResponse[size];
        }
    };

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public MovieData[] getMovieData() {
        return movieData;
    }

    public void setMovieData(MovieData[] movieData) {
        this.movieData = movieData;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }


}

