package ntk.android.estate.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ntk.android.base.activity.BaseActivity;
import ntk.android.estate.R;

public class SearchEstateActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate_search);
        findViewById(R.id.imgToolbarBack).setOnClickListener(view -> finish());
        ((TextView) findViewById(R.id.txtToolbarTitle)).setText("جست و جو");

    }
}
