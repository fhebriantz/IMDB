package com.azha.android.imdb.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.azha.android.imdb.Adapter.ListUpcomingAdapter;
import com.azha.android.imdb.Data.Base_Film;
import com.azha.android.imdb.Config.Config;
import com.azha.android.imdb.Utils.PaginationAdapterCallback;
import com.azha.android.imdb.Utils.PaginationScrollListener;
import com.azha.android.imdb.helper.DividerItemDecoration;
import com.azha.android.imdb.R;
import com.azha.android.imdb.helper.EndlessRecyclerViewScrollListener;
import com.azha.android.imdb.helper.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends BaseActivity  implements PaginationAdapterCallback{

    private String url;
    private RecyclerView mRecyclerView;
    //vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    LinearLayoutManager linearLayoutManager;
    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    @Override
    public void retryPageLoad() {

    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> BARU<<<
    private PaginationScrollListener scrollListener;
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> BARU<<<

    // ArrayList for Listview
    private List<Base_Film> upcominglist = new ArrayList<Base_Film>();

    public ListUpcomingAdapter mAdapter;

    ProgressBar pb;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.id_Upcoming){

            ButterKnife.bind(this);
            url = Config.KEY_URL_UPCOMING;
            String title = "Upcoming";
            title_bar(title);
            init_view();
            upcominglist.clear();
            loadUpcoming(1);
            //scroll_pagination();
            return true;
        }
        if (id==R.id.id_TopRated){

            ButterKnife.bind(this);
            url = Config.KEY_URL_TOP_RATED;
            String title = "Top Rated";
            title_bar(title);
            init_view();
            upcominglist.clear();
            loadUpcoming(1);
           // scroll_pagination();
            return true;
        }

        if (id==R.id.id_Popular){

            ButterKnife.bind(this);
            url = Config.KEY_URL_POPULAR;
            String title = "Popular";
            title_bar(title);
            init_view();
            upcominglist.clear();
            loadUpcoming(1);
          //  scroll_pagination();
            return true;
        }

        if (id==R.id.id_NowPlaying){

            ButterKnife.bind(this);
            url = Config.KEY_URL_NOW_PLAYING;
            String title = "Now Playing";
            title_bar(title);
            init_view();
            upcominglist.clear();
            loadUpcoming(1);
          //  scroll_pagination();
            return true;
        }
        // call the adapter with argument list of items and context.

        return true;
    }



    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        url = Config.KEY_URL_UPCOMING;
        ActionBar ab = getSupportActionBar();
        if (ab !=null) {
            getSupportActionBar().setTitle("Upcoming");
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_event);
        mRecyclerView.setHasFixedSize(true);
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        loadFirstPage();

    }


    private void loadFirstPage() {
        Log.d("MainActivity", "loadFirstPage: "+currentPage);

               loadUpcoming(currentPage);
               //if (currentPage <= TOTAL_PAGES) mAdapter.addLoadingFooter();
               //else isLastPage = true;


    }


    private void loadNextPage() {
        Log.d("MainActivity", "loadNextPage: " + currentPage);

        loadUpcoming(currentPage);
        if (currentPage != TOTAL_PAGES) mAdapter.addLoadingFooter();
        else isLastPage = true;
    }

/*
    //vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callTopRatedMoviesApi().enqueue(new Callback<TopRatedMovies>() {
            @Override
            public void onResponse(Call<TopRatedMovies> call, retrofit2.Response<TopRatedMovies> response) {
                mAdapter.removeLoadingFooter();
                isLoading = false;

                List<Base_Film> results = fetchResults(response);
                mAdapter.addAll(results);

                if (currentPage != TOTAL_PAGES) mAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<TopRatedMovies> call, Throwable t) {
                t.printStackTrace();
                mAdapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }
    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
*/
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> title_bar
   private void title_bar(String title){
       ActionBar ab = getSupportActionBar();
       if (ab !=null) {
           getSupportActionBar().setTitle(title);
       }
   }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> init_view
   private void init_view(){
       pb = (ProgressBar) findViewById(R.id.progress_bar);
       mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));

   }


   /* // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> scroll_pagination
    private void scroll_pagination(){
        RecyclerView rvItems = (RecyclerView) findViewById(R.id.recyclerview_event);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(linearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int pages , int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pages=page + pages++;
                loadUpcoming(pages);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvItems.addOnScrollListener(scrollListener);
    }*/

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> loadUpcoming
    private void loadUpcoming(int currentPage) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+currentPage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);
                        String message = null;
                        JSONObject dataObj = null;

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {

                                dataObj = jsonObject;

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                                try {
                                    if (dataObj != null) {
                                        JSONArray newsArray = dataObj.getJSONArray("results");

                                        for (int i = 0; i < newsArray.length(); i++) {
                                            JSONObject upcObj = newsArray.getJSONObject(i);

//                                            Log.d("title "+i+":", newsObj.getString("title"));

                                            Base_Film filmupc = new Base_Film(
                                                    upcObj.getString("title"),
                                                    upcObj.getString("poster_path"),
                                                    upcObj.getString("original_language"),
                                                    upcObj.getString("original_title"),
                                                    upcObj.getString("backdrop_path"),
                                                    upcObj.getString("overview"),
                                                    upcObj.getString("release_date"),
                                                    upcObj.getInt("vote_count"),
                                                    upcObj.getInt("id"),
                                                    upcObj.getBoolean("video"),
                                                    upcObj.getBoolean("adult"),
                                                    upcObj.getDouble("vote_average"),
                                                    upcObj.getDouble("popularity")
                                            );
                                            upcominglist.add(filmupc);
                                        }

                                        mAdapter = new ListUpcomingAdapter(upcominglist, MainActivity.this);
                                        mRecyclerView.setAdapter(mAdapter);
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        pb.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, MainActivity.this);
                Toast.makeText(getApplicationContext(), "Tidak ada data Film", Toast.LENGTH_LONG).show();
                pb.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //Adding parameters to request

                //returning parameter
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
