package movies.nano.udacity.com.udacitypopularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zoheb Syed on 21-04-2016.
 */
public class MovieTrailer implements Parcelable {

    @SerializedName("id")
    private String trailerID;
    @SerializedName("size")
    private int videoRes;
    @SerializedName("name")
    private String trailerName;
    @SerializedName("key")
    private String trailerKey;
    @SerializedName("site")
    private String videoSite;
    @SerializedName("type")
    private String videoType;

    public String getTrailerID() {
        return trailerID;
    }

    public void setTrailerID(String trailerID) {
        this.trailerID = trailerID;
    }

    public int getVideoRes() {
        return videoRes;
    }

    public void setVideoRes(int videoRes) {
        this.videoRes = videoRes;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getVideoSite() {
        return videoSite;
    }

    public void setVideoSite(String videoSite) {
        this.videoSite = videoSite;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trailerID);
        dest.writeInt(this.videoRes);
        dest.writeString(this.trailerName);
        dest.writeString(this.trailerKey);
        dest.writeString(this.videoSite);
        dest.writeString(this.videoType);
    }

    public MovieTrailer() {
    }

    protected MovieTrailer(Parcel in) {
        this.trailerID = in.readString();
        this.videoRes = in.readInt();
        this.trailerName = in.readString();
        this.trailerKey = in.readString();
        this.videoSite = in.readString();
        this.videoType = in.readString();
    }

    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel source) {
            return new MovieTrailer(source);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };
}
