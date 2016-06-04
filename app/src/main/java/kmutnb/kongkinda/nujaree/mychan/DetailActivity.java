package kmutnb.kongkinda.nujaree.mychan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vutchai14 on 7/1/2559.
 */
public class DetailActivity extends AppCompatActivity {

    TextView mTitle, mDetail, mSource, mType, mIconType, mPhone;
    Toolbar toolbar;
    Bundle extras;
    ImageView img;
    GlobalVariables gvar;
    Typeface FontDTAC;
    MaterialDialog dialog, dialogSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_detail);

        gvar = ((GlobalVariables) getApplicationContext());

        FontDTAC = Typeface.createFromAsset(getAssets(), "DTAC2013_Rg.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mDetail = (TextView) findViewById(R.id.mDetail);
        mSource = (TextView) findViewById(R.id.mSource);
        mType = (TextView) findViewById(R.id.mType);
        mIconType = (TextView) findViewById(R.id.mIconType);
        mPhone = (TextView) findViewById(R.id.mPhone);
        img = (ImageView) findViewById(R.id.img);
        LinearLayout btnPhone = (LinearLayout) findViewById(R.id.btnPhone);
        LinearLayout btnupdate = (LinearLayout) findViewById(R.id.btnupdate);

//        RatingBar Rating = (RatingBar) findViewById(R.id.ColratingBar);
//        Drawable drawable = Rating.getProgressDrawable();
//        drawable.setColorFilter(Color.parseColor("#EF6C00"), PorterDuff.Mode.SRC_ATOP);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        extras = getIntent().getExtras();
        if (extras != null) {
            mTitle.setText(extras.getString("get_title"));
        } else {
            mTitle.setText("รายละเอียด");
        }

        Glide.with(this)
                .load(extras.getString("get_thumbnail"))
                        //  .placeholder(R.mipmap.ic_wrench_grey600_36dp)
                        //  .error(R.mipmap.ic_wrench_grey600_36dp)
                        //.transform(new CircleTransform(mActivity))
                .into(img);

        mDetail.setText(extras.getString("get_detail"));
        mSource.setText(extras.getString("get_source"));

        if (!extras.getString("get_phone").equals("")) {
            btnPhone.setVisibility(View.VISIBLE);
            mPhone.setText(extras.getString("get_phone"));
        }

        if (extras.getString("get_category").equals("1")) {

            mType.setText("ประเภท : ร้านอาหาร");
            mIconType.setText(R.string.icon_cutlery);

        } else if (extras.getString("get_category").equals("2")) {

            mType.setText("ประเภท : ร้านกาแฟ");
            mIconType.setText(R.string.icon_coffee);

        } else if (extras.getString("get_category").equals("3")) {

            mType.setText("ประเภท : สถานที่ท่องเที่ยว");
            mIconType.setText(R.string.icon_sun);

        } else {

            mType.setText("ประเภท : ที่พัก");
            mIconType.setText(R.string.icon_building);

        }

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomViewSource();
            }
        });

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + extras.getString("get_phone")));
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_map) {
            Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri
                    .parse("geo:0,0?q="
                            + extras.getString("get_lat") + "," + extras.getString("get_lon")
                            + "(" + extras.getString("get_title") + ")"));
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void syncData(String _id) {
        showIndeterminateProgressDialog(false);
        Ion.with(this)
                .load(gvar.getWS_URL_SET())
                .setMultipartParameter("_id", _id)
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
        mSource.setText(blog.getSource());
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

    public void showCustomViewSource() {

        dialogSource = new MaterialDialog.Builder(this)
                .typeface(FontDTAC, FontDTAC)
                .iconRes(R.mipmap.ic_launcher)
                .customView(R.layout.dialog_source, true)
                .show();

        TextView txtPlus = (TextView) dialogSource.getCustomView().findViewById(R.id.txtPlus);
        txtPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncData(extras.getString("get_id"));
                dialogSource.dismiss();
            }
        });

        dialogSource.show();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
