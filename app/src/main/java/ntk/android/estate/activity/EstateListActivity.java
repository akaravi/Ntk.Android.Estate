package ntk.android.estate.activity;

import android.content.Intent;
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

public class EstateListActivity extends BaseFilterModelListActivity<EstatePropertyModel> {


    @Override
    protected void onCreated() {
        super.onCreated();
        ((TextView) findViewById(R.id.lblTitle)).setText(R.string.per_estate);
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<EstatePropertyModel>>> getService() {
        return (new EstatePropertyService(this))::getAll;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new EstatePropertyAdapter( models);
    }

    @Override
    public void ClickSearch() {
        startActivity(new Intent(this, SearchEstateActivity.class));
    }
}
