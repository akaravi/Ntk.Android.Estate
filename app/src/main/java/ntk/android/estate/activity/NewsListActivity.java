package ntk.android.estate.activity;

import android.app.Dialog;
import android.content.Intent;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelWithCategoryActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.news.NewsCategoryModel;
import ntk.android.base.entitymodel.news.NewsContentModel;
import ntk.android.base.services.news.NewsCategoryService;
import ntk.android.base.services.news.NewsContentService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.NewsAdapter;
import ntk.android.estate.adapter.NewsCategoryAdapter;

public class NewsListActivity extends BaseFilterModelWithCategoryActivity<NewsContentModel, NewsCategoryModel> {
    @Override
    protected void onCreated() {
        super.onCreated();
        ((TextView) findViewById(R.id.lblTitle)).setText(R.string.per_news);
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<NewsContentModel>>> getService() {
        return new NewsContentService(this)::getAll;
    }


    @Override
    public RecyclerView.Adapter createAdapter() {
        return new NewsAdapter(this, models);
    }

    @Override
    public void ClickSearch() {
        startActivity(new Intent(this, NewsSearchActivity.class));
    }

    @Override
    protected void onCategoryResponse(ErrorException<NewsCategoryModel> response, Dialog dialog) {
        RecyclerView rc = dialog.findViewById(R.id.rc);
        rc.setLayoutManager(new LinearLayoutManager(NewsListActivity.this, LinearLayoutManager.VERTICAL, false));
        rc.setAdapter(new NewsCategoryAdapter(response.ListItems));
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<NewsCategoryModel>>> getCatService() {
        return new NewsCategoryService(this)::getAll;
    }
}
