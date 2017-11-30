package com.azha.android.imdb.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.azha.android.imdb.Activity.DetailActivity;
import com.azha.android.imdb.R;
import com.azha.android.imdb.Data.Base_Film;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListUpcomingAdapter extends
        RecyclerView.Adapter<ListUpcomingAdapter.MyViewHolder> {
    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;
    private List<Base_Film> list_item ;
    public static Context mcontext;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;
    private String title, poster_path, original_language, original_title, backdrop_path, overview, release_date;
    private int vote_count, id;
    private Boolean video, adult;
    private Double vote_average, popularity;

    String baseUrlPicture = "http://image.tmdb.org/t/p/w185";
    public ListUpcomingAdapter(List<Base_Film> list, Context context) {
        list_item = list;
        mcontext = context;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public ListUpcomingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_view, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);


        return myViewHolder;
    }


    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position ) {
        final Base_Film baseFilm = list_item.get(position);


        viewHolder.film_title.setText(baseFilm.getTitle());
        viewHolder.film_tanggal.setText(baseFilm.getRelease_date());
        viewHolder.film_overview.setText(baseFilm.getOverview() );
        viewHolder.film_rating.setText(""+baseFilm.getVote_average() );

        if (!TextUtils.isEmpty(baseFilm.getPoster_path())) {
            Picasso.with(mcontext).load(baseUrlPicture + baseFilm.getPoster_path())
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(viewHolder.film_poster);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, DetailActivity.class);
                intent.putExtra("title", baseFilm.getTitle());
                intent.putExtra("tanggal", baseFilm.getRelease_date());
                intent.putExtra("overview", baseFilm.getOverview());
                intent.putExtra("picture", baseFilm.getPoster_path());
                intent.putExtra("rating", String.valueOf(baseFilm.getVote_average()));
                mcontext.startActivity(intent);
            }
        });



        //   viewHolder.film_poster.setImageResource(baseFilm.getPoster_path());
/*
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, MainActivity.class);
                intent.putExtra("title", baseFilm.getTitle());
                intent.putExtra("poster_path", baseFilm.getPoster_path());
                intent.putExtra("original_language", baseFilm.getOriginal_language());
                intent.putExtra("original_title", baseFilm.getOriginal_title());
                intent.putExtra("backdrop_path", baseFilm.getBackdrop_path());
                intent.putExtra("overview", baseFilm.getOverview());
                intent.putExtra("release_date", baseFilm.getRelease_date());
                intent.putExtra("vote_count", baseFilm.getVote_count());
                intent.putExtra("id", baseFilm.getId());
                intent.putExtra("video", baseFilm.getVideo());
                intent.putExtra("adult", baseFilm.getAdult());
                intent.putExtra("vote_average", baseFilm.getVote_average());
                intent.putExtra("popularity", baseFilm.getPopularity());
                mcontext.startActivity(intent);
            }
        });*/

    }

    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView film_title, film_tanggal, film_overview, film_rating;
        public ImageView film_poster;


        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            film_title = (TextView) itemLayoutView.findViewById(R.id.judul);
            film_tanggal = (TextView) itemLayoutView.findViewById(R.id.tanggal);
            film_overview = (TextView) itemLayoutView.findViewById(R.id.overview);
            film_rating = (TextView) itemLayoutView.findViewById(R.id.rating);
            film_poster = (ImageView) itemLayoutView.findViewById(R.id.gambar);
            //    film_poster = (ImageView) itemLayoutView.findViewById(R.id.gambar);

//            itemLayoutView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(mcontext, NewsGeneralDetailActivity.class);
//                    intent.putExtra("title", news.getTitle());
//                    intent.putExtra("description", news.getDescription());
//                    intent.putExtra("picture", news.getPicture());
//                    intent.putExtra("created_date", news.getCreated_date());
//                    mcontext.startActivity(intent);
//                }
//            });

        }
    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return list_item.size();
    }

     /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(Base_Film r) {
        list_item.add(r);
        notifyItemInserted(list_item.size() - 1);
    }

    public void addAll(List<Base_Film> moveResults) {
        for (Base_Film result : moveResults) {
            add(result);
        }
    }

    public void remove(Base_Film r) {
        int position = list_item.indexOf(r);
        if (position > -1) {
            list_item.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Base_Film(title, poster_path, original_language, original_title, backdrop_path, overview, release_date,vote_count, id,video, adult,vote_average, popularity));
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = list_item.size() - 1;
        Base_Film result = getItem(position);

        if (result != null) {
            list_item.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Base_Film getItem(int position) {
        return list_item.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(list_item.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

}