package ntk.android.estate.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.dtomodel.theme.DrawerChildThemeDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.enums.EnumSortType;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.entitymodel.news.NewsContentModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.services.estate.EstatePropertyTypeLanduseService;
import ntk.android.base.services.news.NewsContentService;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.adapter.Main3EstateLandUseAdapter;
import ntk.android.estate.adapter.Main3EstatePropertyAdapter;
import ntk.android.estate.adapter.Main3NewsAdapter;
import ntk.android.estate.adapter.drawer.Drawer3Adapter;
import ntk.android.estate.adapter.drawer.DrawerAdapter;

public class MainActivity3 extends BaseMainActivity {
    RecyclerView Slider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Slider = findViewById(R.id.rcNews);
        //show drawer
        List<DrawerChildThemeDtoModel> menus = DrawerAdapter.createDrawerItems();
        RecyclerView drawerRecycler = findViewById(R.id.RecyclerDrawer);
        Drawer3Adapter adapter = new Drawer3Adapter(this, menus, findViewById(R.id.floaingDrawer));
        drawerRecycler.setAdapter(adapter);
        drawerRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        init();
        //get news
        HandelSlider();
        //get landUsed property
        getLandUsedProperty();
        //get row data
        // karavi
// Descending = 0,
        // Ascending = 1,
        // Random = 2,
        // View = 3
      

        // karavi
        getData(row1, findViewById(R.id.row1));
        getData(row2, findViewById(R.id.row2));
        getData(row3, findViewById(R.id.row3));
    }

    private void init() {
        TextView seeMore = (TextView) findViewById(R.id.seeMore);
        //see landUse list on new activity on click
        seeMore.setOnClickListener(view -> startActivity(new Intent(MainActivity3.this, LandUsedListActivity.class)));
        //click on humberger
        findViewById(R.id.img_menu).setOnClickListener(v -> ((FlowingDrawer) findViewById(R.id.floaingDrawer)).openMenu(true));
        findViewById(R.id.shareQrCode).setOnClickListener(v -> onInviteMethod());

        TextView rowTitle1 = findViewById(R.id.row1).findViewById(R.id.title);
        TextView rowSeeMore1 = findViewById(R.id.row1).findViewById(R.id.seeMore);
        TextView rowTitle2 = findViewById(R.id.row2).findViewById(R.id.title);
        TextView rowSeeMore2 = findViewById(R.id.row2).findViewById(R.id.seeMore);
        TextView rowTitle3 = findViewById(R.id.row3).findViewById(R.id.title);
        TextView rowSeeMore3 = findViewById(R.id.row3).findViewById(R.id.seeMore);
        //special list
        String title1 = "جدیدترین ";
        String title2 = "پیشنهادی ";
        String title3 = "اجاره روزانه ";
        rowTitle1.setText(title1);
        rowTitle2.setText(title2);
        rowTitle3.setText(title3);
        rowSeeMore1.setOnClickListener(view -> EstateListWithFilterActivity.START_NEW(MainActivity3.this, row1, title1));
        rowSeeMore2.setOnClickListener(view -> EstateListWithFilterActivity.START_NEW(MainActivity3.this, row2, title2));
        rowSeeMore3.setOnClickListener(view -> EstateListWithFilterActivity.START_NEW(MainActivity3.this, row3, title3));

        //set font
        Typeface t1 = FontManager.T1_Typeface(this);
        ((EditText) findViewById(R.id.searchEt)).setTypeface(t1);
        ((TextView) findViewById(R.id.title1)).setTypeface(t1);

        seeMore.setTypeface(t1);
        rowTitle1.setTypeface(t1);
        rowSeeMore1.setTypeface(t1);
        rowTitle2.setTypeface(t1);
        rowSeeMore2.setTypeface(t1);
        rowTitle3.setTypeface(t1);
        rowSeeMore3.setTypeface(t1);


    }

    private void getData(FilterModel filter, View view) {
        ServiceExecute.execute(new EstatePropertyService(this).getAll(filter))
                .subscribe(new ErrorExceptionObserver<EstatePropertyModel>(switcher::showErrorView) {

                    @Override
                    protected void SuccessResponse(ErrorException<EstatePropertyModel> response) {
                        RecyclerView rc = view.findViewById(R.id.rc);
                        rc.setAdapter(new Main3EstatePropertyAdapter(response.ListItems));
                        rc.setLayoutManager(new LinearLayoutManager(MainActivity3.this, RecyclerView.HORIZONTAL, false));
//                        SnapHelper snapHelper = new PagerSnapHelper();
//                        snapHelper.attachToRecyclerView(rc);
                        ViewCompat.setNestedScrollingEnabled(rc, false);
                    }

                    @Override
                    protected Runnable tryAgainMethod() {
                        return () -> getData(filter, view);
                    }
                });


    }


    private void getLandUsedProperty() {
        ServiceExecute.execute(new EstatePropertyTypeLanduseService(this).getAll(new FilterModel().setRowPerPage(100)))
                .subscribe(new NtkObserver<>() {
                    @Override
                    public void onNext(@NonNull ErrorException<EstatePropertyTypeLanduseModel> response) {
                        Main3EstateLandUseAdapter adapter = new Main3EstateLandUseAdapter(response.ListItems);
                        RecyclerView rc = findViewById(R.id
                                .landUseAdapter);
                        rc.setAdapter(adapter);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                });
    }

    private void HandelSlider() {

        FilterModel request = new FilterModel();
        request.RowPerPage = 5;
        request.SortColumn = "Id";
        request.CurrentPageNumber = 1;
        new NewsContentService(this).getAll(request).
                observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new NtkObserver<ErrorException<NewsContentModel>>() {

                    @Override
                    public void onNext(ErrorException<NewsContentModel> newsContentResponse) {
                        if (newsContentResponse.IsSuccess) {
                            if (newsContentResponse.ListItems.size() > 0) {
                                SnapHelper snapHelper = new PagerSnapHelper();
                                Main3NewsAdapter adapter = new Main3NewsAdapter(MainActivity3.this, newsContentResponse.ListItems);
                                Slider.setHasFixedSize(true);
                                LinearLayoutManager manager = new LinearLayoutManager(MainActivity3.this, LinearLayoutManager.HORIZONTAL, false);
                                Slider.setLayoutManager(manager);
                                Slider.setAdapter(adapter);
                                snapHelper.attachToRecyclerView(Slider);
                                adapter.notifyDataSetChanged();
                            } else
                                findViewById(R.id.linear).setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }
}
