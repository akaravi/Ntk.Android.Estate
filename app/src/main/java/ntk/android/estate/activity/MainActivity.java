package ntk.android.estate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.List;

import ntk.android.base.activity.BaseActivity;
import ntk.android.base.appclass.AboutUsClass;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.GenericErrors;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.dtomodel.theme.DrawerChildThemeDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.utill.AppUtill;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstatePropertyAdapter;
import ntk.android.estate.adapter.drawer.DrawerAdapter;

public class MainActivity extends BaseActivity {
    FilterModel row1;
    FilterModel row2;
    FilterModel row3;
    FilterModel row4;
    FilterModel row5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<DrawerChildThemeDtoModel> menus = DrawerAdapter.createDrawerItems();

        startActivity(new Intent(this,NewEstateActivity.class));
        RecyclerView drawerRecycler = findViewById(R.id.RecyclerDrawer);
        DrawerAdapter adapter = new DrawerAdapter(this, menus, findViewById(R.id.floaingDrawer));
        drawerRecycler.setAdapter(adapter);
        drawerRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        drawerRecycler.setHasFixedSize(true);
        //set title of app
        AboutUsClass aboutUsClass = Preferences.with(this).appVariableInfo().aboutUs();
        ((TextView) findViewById(R.id.txtToolbarTitle)).setText(aboutUsClass.AboutUsTitle);
        //click on humberger
        ImageView menu = findViewById(R.id.img_drawable_back);
        menu.setImageResource(R.drawable.hamburger);
        menu.setOnClickListener(v -> ((FlowingDrawer) findViewById(R.id.floaingDrawer)).openMenu(false));

        row1=new FilterModel();
        row2=new FilterModel();
        row3=new FilterModel();
        row4=new FilterModel();
        row5=new FilterModel();
        getdata(row1,findViewById(R.id.rc1));
        getdata(row2,findViewById(R.id.rc2));
        getdata(row3,findViewById(R.id.rc3));
        getdata(row4,findViewById(R.id.rc4));

    }

    private void getdata(FilterModel req, RecyclerView rc) {
        if (AppUtill.isNetworkAvailable(this)) {
            switcher.showProgressView();
            ServiceExecute.execute(new EstatePropertyService(this).getAll(req))
                    .subscribe(new ErrorExceptionObserver<EstatePropertyModel>(switcher::showErrorView) {

                        @Override
                        protected void SuccessResponse(ErrorException<EstatePropertyModel> response) {
                            switcher.showContentView();
                            EstatePropertyAdapter adapter = new EstatePropertyAdapter(MainActivity.this, response.ListItems);
                            rc.setAdapter(adapter);
                            rc.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                            SnapHelper snapHelper = new PagerSnapHelper();
                            snapHelper.attachToRecyclerView(rc);
                            ViewCompat.setNestedScrollingEnabled(rc, false);
                        }

                        @Override
                        protected Runnable tryAgainMethod() {
                            return () -> getdata(req, rc);
                        }
                    });


        } else {
            new GenericErrors().netError(switcher::showErrorView, () -> getdata(req,rc));

        }
    }
}