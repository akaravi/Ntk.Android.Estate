package ntk.android.estate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import ntk.android.estate.R;
import ntk.android.estate.utill.FontManager;

public class ActSplash extends AppCompatActivity {

    @BindViews({R.id.lblAppNameActSplash,
            R.id.lblVersionActSplash})
    List<TextView> Lbls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Lbls.get(0).setTypeface(FontManager.GetTypeface(this, FontManager.IranSansBold));
        Lbls.get(1).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

        new Handler().postDelayed(() -> startActivity(new Intent(ActSplash.this , ActMain.class)), 3500);
    }
}
