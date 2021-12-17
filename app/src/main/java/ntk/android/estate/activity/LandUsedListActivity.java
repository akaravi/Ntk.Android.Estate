package ntk.android.estate.activity;

import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.base.SearchTypeModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.services.estate.EstatePropertyTypeLanduseService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstateLandUsedAdapter;

public class LandUsedListActivity extends BaseFilterModelListActivity<EstatePropertyTypeLanduseModel> {
    @Override
    protected void onCreated() {
        super.onCreated();
        ((TextView) findViewById(R.id.lblTitle)).setText("دسته بندی املاک");
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new EstateLandUsedAdapter(models);
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<EstatePropertyTypeLanduseModel>>> getService() {
        return new EstatePropertyTypeLanduseService(this)::getAll;
    }

    @Override
    public List<SearchTypeModel> getSortList() {
        return null;
    }

    @Override
    public RecyclerView.LayoutManager getRvLayoutManager() {
        return new GridLayoutManager(this,3);
    }
}
