package kmutnb.kongkinda.nujaree.mychan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MagazineActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private GridView mGridView;
    private CustomAdapterMagazine mAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView mTitle;
    Toolbar toolbar;
    Typeface FontDTAC;
    GlobalVariables gvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_magazine);

        gvar = ((GlobalVariables) getApplicationContext());

        FontDTAC = Typeface.createFromAsset(getAssets(), "DTAC2013_Rg.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText("นิตยสาร");

        mGridView = (GridView) findViewById(R.id.grid);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);

        syncData();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void onRefresh() {
        syncData();
    }

    public void syncData() {
        Ion.with(this)
                .load(gvar.getWS_URL_GET_MAG())
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (e == null) {
                            showData(result.getResult());
                        } else {

                        }
                    }
                });
    }

    private void showData(String jsonString) {
        Gson gson = new Gson();
        Blog blog = gson.fromJson(jsonString, Blog.class);
        final List<Post> posts = blog.getPosts();

        mAdapter = new CustomAdapterMagazine(this, posts);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(posts.get(arg2).get_url()));
                startActivity(Intent.createChooser(intent, "อ่านนิตยสาร"));
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
