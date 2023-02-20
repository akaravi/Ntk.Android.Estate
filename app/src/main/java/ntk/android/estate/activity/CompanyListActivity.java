package ntk.android.estate.activity;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstateCompanyModel;
import ntk.android.base.services.estate.EstatePropertyCompanyService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.CompanyAdapter;

public class CompanyListActivity extends BaseFilterModelListActivity<EstateCompanyModel> {
    @Override
    public void afterInit() {
        super.afterInit();
        LblTitle.setText("انبوه سازان");
        findViewById(R.id.imgSearch).setVisibility(View.GONE);
        findViewById(R.id.imgSort).setVisibility(View.GONE);
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new CompanyAdapter(models);
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<EstateCompanyModel>>> getService() {
        return new EstatePropertyCompanyService(this)::getAll;
    }
}
