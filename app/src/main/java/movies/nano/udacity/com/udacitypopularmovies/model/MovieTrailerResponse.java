package movies.nano.udacity.com.udacitypopularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zoheb Syed on 21-04-2016.
 */
public class MovieTrailerResponse implements Parcelable {

    @SerializedName("id")
    private int movieID;
    @SerializedName("results")
    private MovieTrailer[] trailerList;

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public MovieTrailer[] getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(MovieTrailer[] trailerList) {
        this.trailerList = trailerList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.movieID);
        dest.writeTypedArray(this.trailerList, flags);
    }

    public MovieTrailerResponse() {
    }

    protected MovieTrailerResponse(Parcel in) {
        this.movieID = in.readInt();
        this.trailerList = in.createTypedArray(MovieTrailer.CREATOR);
    }

    public static final Parcelable.Creator<MovieTrailerResponse> CREATOR = new Parcelable.Creator<MovieTrailerResponse>() {
        @Override
        public MovieTrailerResponse createFromParcel(Parcel source) {
            return new MovieTrailerResponse(source);
        }

        @Override
        public MovieTrailerResponse[] newArray(int size) {
            return new MovieTrailerResponse[size];
        }
    };
}
