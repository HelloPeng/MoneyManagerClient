package com.pansoft.lvzp.moneymanagerclient.base;

import android.app.Application;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.BallPulseView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.pansoft.lvzp.moneymanagerclient.R;

/**
 * Created by lv_zhp on 2018/4/1.
 */

public class Apl extends Application {

    private static Apl sInstance;
    private String mBaseUrl;
    private String mUserOid;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        TwinklingRefreshLayout.setDefaultHeader(ProgressLayout.class.getName());
        TwinklingRefreshLayout.setDefaultFooter(BallPulseView.class.getName());
    }

    public static Apl getInstance() {
        return sInstance;
    }

    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return getString(R.string.http_format, mBaseUrl);
    }

    public void setUserOid(String userOid) {
        mUserOid = userOid;
    }

    public String getUserOid() {
        return mUserOid;
    }
}
