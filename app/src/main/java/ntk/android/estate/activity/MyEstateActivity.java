package ntk.android.estate.activity;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.MyEstatePropertyAdapter;

public class MyEstateActivity extends BaseFilterModelListActivity<EstatePropertyModel> {
    @Override
    public int getResourceLayout() {
        return R.layout.activity_my_estate;
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        ((TextView) findViewById(R.id.lblTitle)).setText(R.string.per_my_estate);
        findViewById(ntk.android.base.R.id.imgSearch).setVisibility(View.GONE);
        MaterialButton newBtn = findViewById(R.id.addNew);
        newBtn.setText("ثبت ملک جدید");
        newBtn.setOnClickListener(view -> {
            NewEstateActivity.START_ACTIVITY(view.getContext());
        });
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new MyEstatePropertyAdapter(models);
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<EstatePropertyModel>>> getService() {
        return (new EstatePropertyService(this))::getAllEditor;
    }

    @Override
    protected void onShowNewItem() {
        findViewById(R.id.addNew).setVisibility(View.VISIBLE);
    }
}
