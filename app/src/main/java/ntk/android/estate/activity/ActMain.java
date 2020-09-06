package ntk.android.estate.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.gson.Gson;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ntk.android.estate.EState;
import ntk.android.estate.R;
import ntk.android.estate.adapter.AdHouse;
import ntk.android.estate.adapter.drawer.AdDrawer;
import ntk.android.estate.model.House;
import ntk.android.estate.model.theme.Drawer;
import ntk.android.estate.utill.AppUtill;
import ntk.android.estate.utill.EasyPreference;
import ntk.android.estate.utill.FontManager;

public class ActMain extends AppCompatActivity {

    @BindViews({R.id.lblTitleActMain, R.id.lblNearMeTag, R.id.lblLowPriceTag, R.id.lblComfortTag, R.id.lblSpaciousTag})
    List<TextView> Lbls;

    @BindView(R.id.TxtSearchActMain)
    EditText TxtSearch;

    @BindView(R.id.drawerlayout)
    FlowingDrawer drawer;

    @BindView(R.id.HeaderImageActMain)
    KenBurnsView Header;

    @BindView(R.id.RecyclerDrawer)
    RecyclerView RvDrawer;

    @BindView(R.id.recyclerHouseActMain)
    RecyclerView RvHouse;

    @BindView(R.id.FabAddActMain)
    FloatingActionButton Fab;

    private long lastPressedTime;
    private static final int PERIOD = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtill.setStatusBarGradiant(this);
        setContentView(R.layout.act_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Lbls.get(0).setTypeface(FontManager.GetTypeface(this, FontManager.IranSansBold));
        Lbls.get(1).setTypeface(FontManager.GetTypeface(this, FontManager.IranSansBold));
        Lbls.get(2).setTypeface(FontManager.GetTypeface(this, FontManager.IranSansBold));
        Lbls.get(3).setTypeface(FontManager.GetTypeface(this, FontManager.IranSansBold));
        Lbls.get(4).setTypeface(FontManager.GetTypeface(this, FontManager.IranSansBold));
        TxtSearch.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

        drawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
            }
        });

        HandelToolbarDrawer();

        RvHouse.setHasFixedSize(true);
        RvHouse.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<House> houses = new ArrayList<>();
        houses.add(new House(1, "منزل مسکونی", "http://uupload.ir/files/tfbk_photo_recycler.png", "150000000", "تهران شریعتی", 1));
        houses.add(new House(2, "منزل مسکونی", "http://uupload.ir/files/tfbk_photo_recycler.png", "150000", "تهران شریعتی", 2));
        houses.add(new House(3, "منزل مسکونی", "http://uupload.ir/files/tfbk_photo_recycler.png", "1500000000000", "تهران شریعتی", 3));
        houses.add(new House(4, "منزل مسکونی", "http://uupload.ir/files/tfbk_photo_recycler.png", "1500000000", "تهران شریعتی", 4));
        houses.add(new House(5, "منزل مسکونی", "http://uupload.ir/files/tfbk_photo_recycler.png", "150000000", "تهران شریعتی", 1));
        houses.add(new House(6, "منزل مسکونی", "http://uupload.ir/files/tfbk_photo_recycler.png", "1500000000", "تهران شریعتی", 2));
        houses.add(new House(7, "منزل مسکونی", "http://uupload.ir/files/tfbk_photo_recycler.png", "150000000", "تهران شریعتی", 3));
        houses.add(new House(8, "منزل مسکونی", "http://uupload.ir/files/tfbk_photo_recycler.png", "150000000", "تهران شریعتی", 4));
        houses.add(new House(9, "منزل مسکونی", "http://uupload.ir/files/tfbk_photo_recycler.png", "15000000", "تهران شریعتی", 5));

        AdHouse adapter = new AdHouse(this, houses);
        RvHouse.setItemViewCacheSize(houses.size());
        RvHouse.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        RvHouse.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    Fab.hide();
                } else {
                    Fab.show();
                }
            }
        });

    }

    @OnClick(R.id.RippleHamberRecyclerToolbar)
    public void ClickMenu() {
        drawer.openMenu(false);
    }

    private void HandelToolbarDrawer() {
        Drawer theme = new Gson().fromJson(EState.JsonThemeExmaple, Drawer.class);

        RvDrawer.setHasFixedSize(true);
        RvDrawer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        AdDrawer AdDrawer = new AdDrawer(this, theme.Child, drawer);
        RvDrawer.setAdapter(AdDrawer);
        AdDrawer.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "برای خروج مجددا کلید بازگشت را فشار دهید",
                                Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }


    @OnClick(R.id.FabAddActMain)
    public void ClickFab() {
        if (EasyPreference.with(this).getString("register", "0").equals("1")) {
            startActivity(new Intent(this , ActAddState.class));
        } else {
            startActivity(new Intent(this, ActRegister.class));
        }
    }
}
