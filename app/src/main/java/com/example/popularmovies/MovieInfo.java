package com.example.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Артем on 21.08.2015.
 * Class that stores information about movie and supporting class for using Bundle
 */
class MovieInfo  {

    public String id;
    public String posterPath;
    public String bigPosterPath;
    public String originalTitle;
    public String overview;
    public String releaseDate;
    public String voteAverage;

    MovieInfo(String id, String posterPath, String bigPosterPath, String originalTitle, String overview, String releaseDate, String voteAverage) {
        this.id = id;
        this.posterPath = posterPath;
        this.bigPosterPath = bigPosterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }
}
class ParcelMovie implements Parcelable {

    public String id;
    public String posterPath;
    public String bigPosterPath;
    public String originalTitle;
    public String overview;
    public String releaseDate;
    public String voteAverage;

    public ParcelMovie (MovieInfo movieInfo) {
        this.id = movieInfo.id;
        this.posterPath = movieInfo.posterPath;
        this.bigPosterPath = movieInfo.bigPosterPath;
        this.originalTitle = movieInfo.originalTitle;
        this.overview = movieInfo.overview;
        this.releaseDate = movieInfo.releaseDate;
        this.voteAverage = movieInfo.voteAverage;
    }

    private ParcelMovie(Parcel in) {
        id = in.readString();
        posterPath = in.readString();
        bigPosterPath = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(posterPath);
        out.writeString(bigPosterPath);
        out.writeString(originalTitle);
        out.writeString(overview);
        out.writeString(releaseDate);
        out.writeString(voteAverage);
    }

    public static final Parcelable.Creator<ParcelMovie> CREATOR
            = new Parcelable.Creator<ParcelMovie>() {
        public ParcelMovie createFromParcel(Parcel in) {
            return new ParcelMovie(in);
        }

        public ParcelMovie[] newArray(int size) {
            return new ParcelMovie[size];
        }
    };

    public MovieInfo getMovie(){
        return new MovieInfo(this.id,this.posterPath,this.bigPosterPath,this.originalTitle,this.overview,this.releaseDate,this.voteAverage);
    }


}