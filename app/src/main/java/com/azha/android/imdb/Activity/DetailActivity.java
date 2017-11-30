package com.azha.android.imdb.Activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.azha.android.imdb.R;
import com.squareup.picasso.Picasso;


public class DetailActivity extends BaseActivity {

    Context mcontext;
    String baseUrlPicture = "http://image.tmdb.org/t/p/w185";

    @BindView(R.id.juduldetail)
    TextView tvTitle;
    @BindView(R.id.tanggaldetail)
    TextView tvTanggal;
    @BindView(R.id.overviewdetail)
    TextView tvOverview;
    @BindView(R.id.gambardetail)
    ImageView tvPicture;
    @BindView(R.id.ratingdetail)
    TextView tvRating;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_upcoming_detail;
    }

//    public static String [] menuList={"Absen","Rundown","Materi","Location"};
//    public static int [] iconId={R.drawable.icon_absen,R.drawable.icon_rundown,R.drawable.icon_materi,R.drawable.icon_location};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        }

        tvTitle.setText(getIntent().getStringExtra("title"));
        tvOverview.setText(getIntent().getStringExtra("overview"));
        tvRating.setText(getIntent().getStringExtra("rating"));
        String dateTime[]=getIntent().getStringExtra("tanggal").split(" ");


        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy");

        String date = null;
        try {
            Date d = inputFormat.parse(dateTime[0]);
            date = outputFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvTanggal.setText(date);

        if (!TextUtils.isEmpty(getIntent().getStringExtra("picture"))){
            Picasso.with(mcontext).load(baseUrlPicture + getIntent().getStringExtra("picture"))
                    .error(R.drawable.common_google_signin_btn_icon_dark)
                    .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                    .into(tvPicture);
        }

        /*
        img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (mcontext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.v(TAG, "Permission is granted");

                        if (isDownloadManagerAvailable(mcontext)) {
                            Toast.makeText(getApplicationContext(), "Downloading material event", Toast.LENGTH_SHORT).show();

                            String url = getIntent().getStringExtra("url_download");
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setTitle("Download File");
                            // in order for this if to run, you must use the android 3.2 to compile your app
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            }
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url);

                            // get download service and enqueue file
                            DownloadManager manager = (DownloadManager) mcontext.getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);
                        } else {
                            Toast.makeText(mcontext, "Download Manager tidak tersedia!", Toast.LENGTH_LONG).show();
                        }

                    } else {

                        Log.v(TAG, "Permission is revoked");
                        Toast.makeText(mcontext, "Error, penyimpanan file tidak diperbolehkan", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });*/
//        gv=(GridView) findViewById(R.id.gridview_parallax_header);
//        gv.setAdapter(new EventDetailAdapter(this, menuList, iconId));
//        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(EventDetailActivity.this, "You Clicked at " +menuList[+ i], Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }
}
