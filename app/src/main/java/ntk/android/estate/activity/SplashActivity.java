package ntk.android.estate.activity;

import android.content.Intent;

import ntk.android.base.activity.common.BaseSplashActivity;

public class SplashActivity extends BaseSplashActivity {
    @Override
    protected void onCreated() {
        finish();
        startActivity(new Intent(this, SearchEstateActivity.class));
    }
}
