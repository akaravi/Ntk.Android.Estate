package ntk.android.estate.activity;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstatePropertyAdapter;

public class FavoriteListActivity extends BaseFilterModelListActivity<EstatePropertyModel> {
    @Override
    protected void onCreated() {
        super.onCreated();
        ((TextView) findViewById(R.id.lblTitle)).setText(R.string.per_my_favorite);
        findViewById(ntk.android.base.R.id.imgSearch).setVisibility(View.GONE);
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new EstatePropertyAdapter(this, models);
    }
    @Override
    public Function<FilterModel, Observable<ErrorException<EstatePropertyModel>>> getService() {
        return new EstatePropertyService(this)::getFavoriteList;
    }
}
