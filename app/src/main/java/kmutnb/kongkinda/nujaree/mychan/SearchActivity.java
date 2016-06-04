package kmutnb.kongkinda.nujaree.mychan;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchActivity extends AppCompatActivity {

    private ListView mListView;
    private CustomAdapter mAdapter;
    Typeface FontDTAC;
    MaterialDialog dialog;
    Bundle extras;
    GlobalVariables gvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_search);

        gvar = ((GlobalVariables) getApplicationContext());

        FontDTAC = Typeface.createFromAsset(getAssets(), "DTAC2013_Rg.ttf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);

        mListView = (ListView) findViewById(R.id.listView);

        extras = getIntent().getExtras();
        if (extras != null) {
            if (!extras.getString("txtKeytype").equals("")) {
                syncData(extras.getString("txtKeytype"), "");
                if (extras.getString("txtKeytype").equals("1")) {
                    mTitle.setText("ร้านอาหาร");
                } else if (extras.getString("txtKeytype").equals("2")) {
                    mTitle.setText("ร้านกาแฟ");
                } else if (extras.getString("txtKeytype").equals("3")) {
                    mTitle.setText("สถานที่ท่องเที่ยว");
                } else if (extras.getString("txtKeytype").equals("4")) {
                    mTitle.setText("ที่พัก");
                }
            }
        }

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

    public void syncData(String txtType, String txtInput) {
        showIndeterminateProgressDialog(false);
        Ion.with(this)
                .load(gvar.getWS_URL_GET())
                .setMultipartParameter("txtKeytype", txtType)
                .setMultipartParameter("txtKeyword", txtInput)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        dialog.dismiss();
                        if (e == null) {
                            showData(result.getResult());
                        } else {
                            DialogOKError("แจ้งเตือน", "ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้", "ยกเลิก");
                        }
                    }
                });
    }

    private void showData(String jsonString) {
        Gson gson = new Gson();
        Blog blog = gson.fromJson(jsonString, Blog.class);
        final List<Post> posts = blog.getPosts();

        mAdapter = new CustomAdapter(this, posts);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {

                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra("get_category", posts.get(arg2).get_category());
                i.putExtra("get_detail", posts.get(arg2).get_detail());
                i.putExtra("get_head", posts.get(arg2).get_head());
                i.putExtra("get_lat", posts.get(arg2).get_lat());
                i.putExtra("get_lon", posts.get(arg2).get_lon());
                i.putExtra("get_review", posts.get(arg2).get_review());
                i.putExtra("get_title", posts.get(arg2).get_title());
                i.putExtra("get_source", posts.get(arg2).get_source());
                i.putExtra("get_thumbnail", posts.get(arg2).get_thumbnail());
                startActivity(i);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (extras != null) {
            if (extras.getString("txtKeytype").equals("")) {

                getMenuInflater().inflate(R.menu.search, menu);
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView)
                        MenuItemCompat.getActionView(menu.findItem(R.id.search));

                if (null != searchView) {
                    searchView.setSearchableInfo(searchManager
                            .getSearchableInfo(getComponentName()));
                    searchView.setIconifiedByDefault(false);
                }

                SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                    public boolean onQueryTextChange(String newText) {
                        // this is your adapter that will be filtered
                        return true;
                    }

                    public boolean onQueryTextSubmit(String query) {
                        //Here u can get the value "query" which is entered in the search box.
                        if (extras != null) {
                            syncData(extras.getString("txtKeytype"), query);
                        }
                        return true;
                    }
                };
                searchView.setOnQueryTextListener(queryTextListener);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void showIndeterminateProgressDialog(boolean horizontal) {

        dialog = new MaterialDialog.Builder(this)
                .typeface(FontDTAC, FontDTAC)
                .content("โปรดรอ...")
                .progress(true, 0)
                .cancelable(false)
                .widgetColorRes(R.color.colorPrimary)
                .progressIndeterminateStyle(horizontal)
                .show();

    }

    public void DialogOKError(final String txtTitle, final String txtContent, final String txtPositive) {
        new MaterialDialog.Builder(this)
                .iconRes(R.mipmap.ic_launcher)
                .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                .typeface(FontDTAC, FontDTAC)
                .title(txtTitle)
                .content(txtContent)
                .cancelable(false)
                .positiveText(txtPositive)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                    }
                }).show();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
