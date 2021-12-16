package ntk.android.estate.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.GenericErrors;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.dtomodel.theme.DrawerChildThemeDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.entitymodel.news.NewsContentModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.services.estate.EstatePropertyTypeLanduseService;
import ntk.android.base.services.news.NewsContentService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.Main3EstateLandUseAdapter;
import ntk.android.estate.adapter.Main3EstatePropertyAdapter;
import ntk.android.estate.adapter.Main3NewsAdapter;
import ntk.android.estate.adapter.MainEstatePropertyAdapter;
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
        DrawerAdapter adapter = new DrawerAdapter(this, menus, findViewById(R.id.floaingDrawer));
        drawerRecycler.setAdapter(adapter);
        drawerRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        drawerRecycler.setHasFixedSize(true);
        //get news
        HandelSlider();
        //get landUsed property
        getEstateProperty();
        //get row data
        getData(row1, findViewById(R.id.row1));
        getData(row2, findViewById(R.id.row2));
        getData(row3, findViewById(R.id.row3));
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


    private void getEstateProperty() {
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
