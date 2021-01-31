package com.esoxjem.movieguide.favorites;

import com.esoxjem.movieguide.Movie;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author NUTS-COON
 */

public class MovieRealmObject extends RealmObject {

    @PrimaryKey
    private String anId;
    private String anOverview;
    private String releaseDate;
    private String posterPath;
    private String backdropPath;
    private String aTitle;
    private double voteAverage;

    public static final String ID = "id";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "releaseDate";
    public static final String POSTER_PATH = "posterPath";
    public static final String BACKDROP_PATH = "backdropPath";
    public static final String TITLE = "title";
    public static final String VOTE_AVERAGE = "voteAverage";


    public String getId() {
        return anId;
    }

    public void setId(String anId) {
        this.anId = anId;
    }

    public String getOverview() {
        return anOverview;
    }

    public void setOverview(String anOverview) {
        this.anOverview = anOverview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getTitle() {
        return aTitle;
    }

    public void setTitle(String aTitle) {
        this.aTitle = aTitle;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public MovieRealmObject(Movie movie) {
        anId = movie.getId();
        anOverview = movie.getOverview();
        releaseDate = movie.getReleaseDate();
        posterPath = movie.getPosterPath();
        backdropPath = movie.getBackdropPath();
        aTitle = movie.getTitle();
        voteAverage = movie.getVoteAverage();
    }

    public MovieRealmObject() {

    }
}
