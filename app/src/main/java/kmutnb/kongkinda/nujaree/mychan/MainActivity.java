package kmutnb.kongkinda.nujaree.mychan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
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

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ListView mListView;
    private CustomAdapter mAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView mTitle;
    Toolbar toolbar;
    Typeface FontDTAC;
    private View negativeAction;
    GlobalVariables gvar;
    FloatingActionButton flaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_main);

        gvar = ((GlobalVariables) getApplicationContext());

        FontDTAC = Typeface.createFromAsset(getAssets(), "DTAC2013_Rg.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText("จันทบุรี");

        mListView = (ListView) findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        flaBtn = (FloatingActionButton) findViewById(R.id.fabBtn);
        flaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MagazineActivity.class);
                startActivity(i);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);

        syncData("");

    }

    public void onRefresh() {
        syncData("");
    }

    public void syncData(String txtInput) {
        Ion.with(this)
                .load(gvar.getWS_URL_GET())
                .setMultipartParameter("txtKeytype", "")
                .setMultipartParameter("txtKeyword", txtInput)
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

        mAdapter = new CustomAdapter(this, posts);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {

                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra("get_id", posts.get(arg2).getId());
                i.putExtra("get_category", posts.get(arg2).get_category());
                i.putExtra("get_detail", posts.get(arg2).get_detail());
                i.putExtra("get_head", posts.get(arg2).get_head());
                i.putExtra("get_lat", posts.get(arg2).get_lat());
                i.putExtra("get_lon", posts.get(arg2).get_lon());
                i.putExtra("get_review", posts.get(arg2).get_review());
                i.putExtra("get_title", posts.get(arg2).get_title());
                i.putExtra("get_source", posts.get(arg2).get_source());
                i.putExtra("get_thumbnail", posts.get(arg2).get_thumbnail());
                i.putExtra("get_phone", posts.get(arg2).get_phone());
                startActivity(i);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_magnify) {
            showCustomViewWork();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showCustomViewWork() {

        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("ค้นหา")
                .typeface(FontDTAC, FontDTAC)
                .iconRes(R.mipmap.ic_launcher)
                .cancelable(false)
                .customView(R.layout.dialog_customview_type, true)
                .negativeText("ยกเลิก")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                    }
                }).show();

        negativeAction = dialog.getActionButton(DialogAction.NEGATIVE);
        //noinspection ConstantConditions

        CheckBox chkAll = (CheckBox) dialog.getCustomView().findViewById(R.id.chkAll);
        CheckBox chkfood = (CheckBox) dialog.getCustomView().findViewById(R.id.chkfood);
        CheckBox chkcoffe = (CheckBox) dialog.getCustomView().findViewById(R.id.chkcoffe);
        CheckBox chktav = (CheckBox) dialog.getCustomView().findViewById(R.id.chktav);
        CheckBox chkhotail = (CheckBox) dialog.getCustomView().findViewById(R.id.chkhotail);

        chkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                i.putExtra("txtKeytype", "");
                startActivity(i);
                dialog.dismiss();
            }
        });

        chkfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                i.putExtra("txtKeytype", "1");
                startActivity(i);
                dialog.dismiss();
            }
        });

        chkcoffe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                i.putExtra("txtKeytype", "2");
                startActivity(i);
                dialog.dismiss();
            }
        });

        chktav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                i.putExtra("txtKeytype", "3");
                startActivity(i);
                dialog.dismiss();
            }
        });

        chkhotail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                i.putExtra("txtKeytype", "4");
                startActivity(i);
                dialog.dismiss();
            }
        });

        dialog.show();
        negativeAction.setEnabled(true); // disabled by default
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
