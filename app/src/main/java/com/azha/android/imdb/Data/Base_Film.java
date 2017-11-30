package com.azha.android.imdb.Data;

/**
 * Created by way on 9/17/2017.
 */

public class Base_Film {

    private String title, poster_path, original_language, original_title, backdrop_path, overview, release_date;
    private int vote_count, id;
    private Boolean video, adult;
    private Double vote_average, popularity;


    public Base_Film(String title, String poster_path, String original_language, String original_title, String backdrop_path, String overview, String release_date,
                     int vote_count, int id,
                     Boolean video, Boolean adult,
                     Double vote_average, Double popularity){
        this.title = title;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.release_date = release_date;

        this.vote_count = vote_count;
        this.id = id;

        this.video = video;
        this.adult = adult;

        this.vote_average = vote_average;
        this.popularity = popularity;
    }

    public String getTitle(){
        return title;
    }

    public String getPoster_path(){
        return poster_path;
    }

    public String getOriginal_language(){
        return original_language;
    }

    public String getOriginal_title(){
        return original_title;
    }

    public String getBackdrop_path(){
        return backdrop_path;
    }

    public String getOverview () { return overview; }

    public String getRelease_date () { return release_date; }


    public int getVote_count(){
        return vote_count;
    }

    public int getId () { return id; }


    public Boolean getVideo(){
        return video;
    }

    public Boolean getAdult () { return adult; }


    public Double getVote_average(){
        return vote_average;
    }

    public Double getPopularity () { return popularity; }

    public genre_id getGenreId (){
        return getGenreId();
    }


    @Override
    public String toString()
    {
        return String.format(
                "[news: title=%1$s, description=%2$s, picture=%3$s, created_date=%4$s, modified_date=%5$s]",
                title, poster_path, original_language, original_title, backdrop_path, overview, vote_count, id, video, adult, vote_average, popularity);

    }

    public class genre_id {
        public int getGenreId(){

            return getGenreId();
        }
    }
}
