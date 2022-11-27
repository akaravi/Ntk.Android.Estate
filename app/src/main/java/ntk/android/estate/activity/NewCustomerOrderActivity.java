package ntk.android.estate.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

import es.dmoral.toasty.Toasty;
import ntk.android.base.NTKApplication;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.activity.common.AuthWithSmsActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.coremain.CoreCurrencyModel;
import ntk.android.base.entitymodel.estate.EstateCustomerOrderModel;
import ntk.android.base.services.estate.EstateCustomerOrderService;
import ntk.android.base.utill.FontManager;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.base.view.dialog.SweetAlertDialog;
import ntk.android.estate.R;
import ntk.android.estate.fragment.NewOrderFragment1;
import ntk.android.estate.fragment.NewOrderFragment2;
import ntk.android.estate.fragment.NewOrderFragment3;
import ntk.android.estate.fragment.NewOrderFragment4;

public class NewCustomerOrderActivity extends BaseActivity {

    EstateCustomerOrderModel model = new EstateCustomerOrderModel();
    public CoreCurrencyModel selectedCurrency;
    TextView title;
    protected int stepNumber;
    //boolean used for prevent add model until uploading pic finished
    boolean uploadProcess = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_estate_and_order);
        title = findViewById(R.id.txtToolbarTitle);
        findViewById(R.id.imgToolbarBack).setOnClickListener(view -> finish());
        findViewById(R.id.backBtn).setOnClickListener(view -> {
            onBackPressed();
        });
        setFont();
        afterCreate();
    }

    protected void afterCreate() {
        showFragment1();
    }

    private void setFont() {
        Typeface t1 = FontManager.T1_Typeface(this);
        ((TextView) findViewById(R.id.txtToolbarTitle)).setTypeface(t1);
        ((MaterialButton) findViewById(R.id.continueBtn)).setTypeface(t1);
        ((MaterialButton) findViewById(R.id.backBtn)).setTypeface(t1);
        ((MaterialButton) findViewById(R.id.addNewBtn)).setTypeface(t1);
    }


    protected void showFragment1() {
        stepNumber = 1;
        title.setText("مشخصات اصلی");
        findViewById(R.id.backBtn).setVisibility(View.GONE);

        NewOrderFragment1 fragment = new NewOrderFragment1();
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                showFragment2();
        });
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    protected void showFragment2() {
        stepNumber = 2;
        title.setText("جزئیات و مشخصات");
        findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        NewOrderFragment2 fragment = new NewOrderFragment2();
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                showFragment3();
        });
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    protected void showFragment3() {
        stepNumber = 3;
        title.setText("شرایط معامله");
        findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.addNewBtn).setVisibility(View.GONE);
        findViewById(R.id.continueBtn).setVisibility(View.VISIBLE);
        NewOrderFragment3 fragment = new NewOrderFragment3();
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                showFragment4();
        });
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    protected void showFragment4() {
        stepNumber = 4;
        title.setText("سایر مشخصات");
        findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.addNewBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.continueBtn).setVisibility(View.GONE);
        NewOrderFragment4 fragment = new NewOrderFragment4();
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                createModel();
        });
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }

    protected void createModel() {
        showProgress();
        model.PropertyDetailGroups = null;
        model.LinkCoreCurrencyId = selectedCurrency.Id;
        ServiceExecute.execute(new EstateCustomerOrderService(this).add(model)).subscribe(new NtkObserver<ErrorException<EstateCustomerOrderModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstateCustomerOrderModel> response) {
                if (response.IsSuccess) {
                    Toasty.success(NewCustomerOrderActivity.this, "ملک شما ثبت شد").show();
                    finish();
                } else {
                    Toasty.error(NewCustomerOrderActivity.this, "هنگام ثبت خطا رخ داد مجددا تلاش نمایید" + "\n+" +
                            response.ErrorMessage).show();
                    showContent();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toasty.error(NewCustomerOrderActivity.this, "هنگام ثبت خطا رخ داد مجددا تلاش نمایید").show();
                showContent();
            }
        });
    }


    @Override
    public void onBackPressed() {
        stepNumber--;
        if (stepNumber == 4)
            showFragment4();
        else if (stepNumber == 3)
            showFragment3();
        else if (stepNumber == 2)
            showFragment2();
        else if (stepNumber == 1)
            showFragment1();
        else super.onBackPressed();
    }

    public void showErrorView() {
        switcher.showErrorView();
    }

    public void showProgress() {
        switcher.showProgressView();
    }

    public void showContent() {
        switcher.showContentView();
    }

    public EstateCustomerOrderModel model() {
        return model;
    }

    public static void START_ACTIVITY(Context c) {
        //user has logged in and saved his user Id
        if (Preferences.with(c).appVariableInfo().isLogin() && Preferences.with(c).UserInfo().userId() > 0)
            c.startActivity(new Intent(c, NewCustomerOrderActivity.class));
        else {
            //show dialog to go to login page
            SweetAlertDialog dialog = new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE);
            dialog.setTitle("خطا در انجام عملیات");
            dialog.setContentText("برای ثبت ملک نیاز است که به حساب خود وارد شوید. آیا مایلید به صفحه ی ورود هدایت شوید؟");
            dialog.setConfirmButton("بلی", d -> {
                Preferences.with(d.getContext()).appVariableInfo().set_registerNotInterested(false);
                Preferences.with(d.getContext()).appVariableInfo().setIsLogin(false);
                Intent i = new Intent(d.getContext(), AuthWithSmsActivity.class);
                //clear all activity that open before
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NTKApplication.get().startActivity(i);
                d.dismiss();
            });
            dialog.setCancelButton("تمایل ندارم", SweetAlertDialog::dismiss);
            dialog.show();
        }
    }
}


