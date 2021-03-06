package com.example.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Артем on 21.08.2015.
 * Class that stores information about movie and supporting class for using Bundle
 */
class MovieInfo implements Parcelable {

    private final String LOG_TAG = "PopularMovies";

    public String id;
    public String posterPath;
    public String bigPosterPath;
    public String originalTitle;
    public String overview;
    public String releaseDate;
    public double voteAverage;
    public boolean isFavorite;

    MovieInfo() {
    }

    MovieInfo(String id, String posterPath, String bigPosterPath, String originalTitle, String overview, String releaseDate, double voteAverage) {
        this.id = id;
        this.posterPath = posterPath;
        this.bigPosterPath = bigPosterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    public boolean isFavorite(Context c) {
        MovieDBOpenHelper movieDBOpenHelper = new MovieDBOpenHelper(c);

        SQLiteDatabase movieDB = movieDBOpenHelper.getReadableDatabase();
        Cursor cur;

        try {
            cur = movieDB.rawQuery("select 1 from movies where mv_id = " + id, null);
            isFavorite = (cur.getCount() > 0);
            cur.close();
        } catch (SQLException e) {
            Log.v(LOG_TAG, e.getMessage());
        } finally {
            movieDB.close();
            movieDBOpenHelper.close();
        }
        return isFavorite;
    }

    public void changeFavorite(SQLiteDatabase movieDB) {


        try {
            movieDB.beginTransaction();
            if (!isFavorite) {
                movieDB.execSQL("insert into movies values (" + id + ",'"
                        + originalTitle + "','"
                        + releaseDate + "','"
                        + overview.replace("'", "") + "',"
                        + voteAverage + ",'"
                        + posterPath + "')");
                isFavorite = true;
            } else {
                movieDB.execSQL("delete from movies where mv_id = " + id);
                isFavorite = false;
            }

            movieDB.setTransactionSuccessful();
            movieDB.endTransaction();
        } catch (SQLException e) {
            Log.v(LOG_TAG, e.getMessage());
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(posterPath);
        out.writeString(bigPosterPath);
        out.writeString(originalTitle);
        out.writeString(overview);
        out.writeString(releaseDate);
        out.writeDouble(voteAverage);
    }

    private MovieInfo(Parcel in) {
        id = in.readString();
        posterPath = in.readString();
        bigPosterPath = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR
            = new Parcelable.Creator<MovieInfo>() {
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    public MovieInfo getMovie() {
        return new MovieInfo(this.id, this.posterPath, this.bigPosterPath, this.originalTitle, this.overview, this.releaseDate, this.voteAverage);
    }

}
