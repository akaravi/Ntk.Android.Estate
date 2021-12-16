package ntk.android.estate.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeUsageModel;
import ntk.android.base.entitymodel.news.NewsContentModel;
import ntk.android.base.services.estate.EstatePropertyTypeLanduseService;
import ntk.android.base.services.estate.EstatePropertyTypeUsageService;
import ntk.android.base.services.news.NewsContentService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.Main3EstateLandUseAdapter;
import ntk.android.estate.adapter.Main3NewsAdapter;

public class MainActivity3 extends BaseMainActivity {
    RecyclerView Slider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Slider = findViewById(R.id.rcNews);
        HandelSlider();
        getEstateProperty();
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
