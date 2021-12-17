package ntk.android.estate.activity;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.Extras;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstatePropertyAdapter;

public class EstateListActivity extends BaseFilterModelListActivity<EstatePropertyModel> {


    public static void START_NEW(Context context, EstatePropertyTypeLanduseModel item) {
        Intent i=new Intent(context,EstateListActivity.class);
        FilterModel filterModel=new FilterModel();
        i.putExtra(Extras.EXTRA_FIRST_ARG, new Gson().toJson(filterModel));
        context.startActivity(i);
    }


    @Override
    protected void onCreated() {
            LblTitle.setText(R.string.per_estate);
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<EstatePropertyModel>>> getService() {
        return (new EstatePropertyService(this))::getAll;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new EstatePropertyAdapter(models);
    }

    @Override
    public void ClickSearch() {
        startActivity(new Intent(this, SearchEstateActivity.class));
    }
}
