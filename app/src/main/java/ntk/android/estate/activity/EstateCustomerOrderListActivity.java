package ntk.android.estate.activity;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstateCustomerOrderModel;
import ntk.android.base.services.estate.EstateCustomerOrderService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstateCustomerOrderAdapter;

public class EstateCustomerOrderListActivity extends BaseFilterModelListActivity<EstateCustomerOrderModel> {

    @Override
    protected void onCreated() {
        super.onCreated();
        ((TextView) findViewById(R.id.lblTitle)).setText(R.string.per_customer_order);
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new EstateCustomerOrderAdapter(models);
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<EstateCustomerOrderModel>>> getService() {
        return new EstateCustomerOrderService(this)::getAll;
    }
}
