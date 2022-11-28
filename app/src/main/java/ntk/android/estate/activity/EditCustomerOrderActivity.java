package ntk.android.estate.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import es.dmoral.toasty.Toasty;
import ntk.android.base.Extras;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.estate.EstateCustomerOrderModel;
import ntk.android.base.services.estate.EstateCustomerOrderService;

public class EditCustomerOrderActivity extends NewCustomerOrderActivity {
    public static void NEW(Context c, EstateCustomerOrderModel model) {
        Intent i = new Intent(c, EditCustomerOrderActivity.class);
        i.putExtra(Extras.Extra_5_ARG, new Gson().toJson(model));
        c.startActivity(i);
    }

    @Override
    protected void afterCreate() {
        model = new Gson().fromJson(getIntent().getExtras().getString(Extras.Extra_5_ARG), EstateCustomerOrderModel.class);
        showFragment1();
    }

    @Override
    protected void createModel() {
        showProgress();
        model.PropertyDetailGroups = null;
        model.LinkCoreCurrencyId = selectedCurrency.Id;
        ServiceExecute.execute(new EstateCustomerOrderService(this).edit(model)).subscribe(new NtkObserver<ErrorException<EstateCustomerOrderModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstateCustomerOrderModel> response) {
                if (response.IsSuccess) {
                    Toasty.success(EditCustomerOrderActivity.this, "سفارش شما ویرایش شد").show();
                    finish();
                } else {
                    Toasty.error(EditCustomerOrderActivity.this, "هنگام ویرایش خطا رخ داد مجددا تلاش نمایید" + "\n+" +
                            response.ErrorMessage).show();
                    showContent();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toasty.error(EditCustomerOrderActivity.this, "هنگام ویرایش خطا رخ داد مجددا تلاش نمایید").show();
                showContent();
            }
        });
    }
}
