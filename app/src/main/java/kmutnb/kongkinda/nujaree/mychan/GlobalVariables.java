package kmutnb.kongkinda.nujaree.mychan;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author Jenny Tong (mimming)
 * @since 12/5/14
 * <p/>
 * Initialize Firebase with the application context. This must happen before the client is used.
 */
public class GlobalVariables extends Application {

    public String WS_URL_GET = "http://192.168.1.35/json/getjson.php";
    public String WS_URL_SET = "http://192.168.1.35/json/updatesource.php";
    public String WS_URL_GET_MAG = "http://192.168.1.35/json/getmagazine.php";

    public String getWS_URL_GET() {
        return WS_URL_GET;
    }

    public String getWS_URL_SET() {
        return WS_URL_SET;
    }

    public String getWS_URL_GET_MAG() {
        return WS_URL_GET_MAG;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("DTAC2013_Rg.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

    }

}
