package ntk.android.estate.activity;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import io.reactivex.Observable;
import java8.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstateCustomerOrderModel;
import ntk.android.base.services.estate.EstateCustomerOrderService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstateCustomerOrderAdapter;

public class EstateCustomerOrderListActivity extends BaseFilterModelListActivity<EstateCustomerOrderModel> {
    @Override
    public int getResourceLayout() {
        return R.layout.activity_order_estate_list;
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        ((TextView) findViewById(R.id.lblTitle)).setText(R.string.per_customer_order);
        findViewById(ntk.android.base.R.id.imgSearch).setVisibility(View.GONE);
        MaterialButton newBtn = findViewById(R.id.addNew);
        MaterialButton newBtn2 = findViewById(R.id.addNewOnList);
        newBtn.setText("ثبت سفارش جدید");
        newBtn2.setText("ثبت سفارش جدید");
        newBtn.setOnClickListener(view -> {
            NewCustomerOrderActivity.START_ACTIVITY(view.getContext());
        });
        newBtn2.setOnClickListener(view -> {
            NewCustomerOrderActivity.START_ACTIVITY(view.getContext());
        });
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new EstateCustomerOrderAdapter(models);
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<EstateCustomerOrderModel>>> getService() {
        return new EstateCustomerOrderService(this)::getAllEditor;
    }

    @Override
    protected void onShowNewItem() {
        findViewById(R.id.addNew).setVisibility(View.VISIBLE);
    }
}
