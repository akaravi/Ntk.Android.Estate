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

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.activity.common.AuthWithSmsActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.coremain.CoreCurrencyModel;
import ntk.android.base.entitymodel.estate.EstateContractModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.utill.FontManager;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.base.view.dialog.SweetAlertDialog;
import ntk.android.estate.R;
import ntk.android.estate.fragment.NewEstateFragment1;
import ntk.android.estate.fragment.NewEstateFragment2;
import ntk.android.estate.fragment.NewEstateFragment3;
import ntk.android.estate.fragment.NewEstateFragment4;
import ntk.android.estate.fragment.NewEstateFragment5;

public class NewEstateActivity extends BaseActivity {
    public String MainImage_GUID;
    public String MainImage_FilePath;
    public List<String> OtherImageIds = new ArrayList<>();
    public List<String> OtherImageSrc = new ArrayList<>();
    EstatePropertyModel model = new EstatePropertyModel();
    public CoreCurrencyModel selectedCurrency;
    TextView title;
    protected int stepNumber;
    //boolean used for prevent add model until uploading pic finished
    boolean uploadProcess = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_estate);
        title = findViewById(R.id.txtToolbarTitle);
        findViewById(R.id.imgToolbarBack).setOnClickListener(view -> finish());
        findViewById(R.id.backBtn).setOnClickListener(view -> {
            onBackPressed();
        });
        setFont();
        afterCreate();
    }

    protected void afterCreate() {
        showFragment4();
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
        title.setText("مشخصات ملک");
        findViewById(R.id.backBtn).setVisibility(View.GONE);

        NewEstateFragment1 fragment = new NewEstateFragment1();
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
        NewEstateFragment2 fragment = new NewEstateFragment2();
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                showFragment3();
        });
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    protected void showFragment3() {
        stepNumber = 3;
        title.setText("مشخصات آگهی");
        findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        NewEstateFragment3 fragment = new NewEstateFragment3();
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                showFragment4();
        });
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    protected void showFragment4() {
        stepNumber = 4;
        title.setText("شرایط معامله");
        findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.addNewBtn).setVisibility(View.GONE);
        findViewById(R.id.continueBtn).setVisibility(View.VISIBLE);
        NewEstateFragment4 fragment = new NewEstateFragment4();
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                showFragment5();
        });
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }

    protected void showFragment5() {
        stepNumber = 5;
        title.setText("تصاویر ملک");
        findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.addNewBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.continueBtn).setVisibility(View.GONE);

        NewEstateFragment5 fragment = new NewEstateFragment5();
        findViewById(R.id.addNewBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                if (!uploadProcess) {
                    createModel();
                } else {
                    Toasty.info(NewEstateActivity.this, "در حال بارگذاری فایل انتخابی شما...", Toasty.LENGTH_LONG).show();
                }
        });

        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }

    protected void createModel() {
        showProgress();
        model.PropertyDetailGroups = null;
        model.UploadFileGUID = new ArrayList<>();
        if (MainImage_GUID != null && !MainImage_GUID.equalsIgnoreCase(""))
            model.UploadFileGUID.add(MainImage_GUID);
        model.UploadFileGUID.addAll(OtherImageIds);
        for (EstateContractModel model :
                model.Contracts) {
            model.LinkCoreCurrencyId = selectedCurrency.Id;
        }
        ServiceExecute.execute(new EstatePropertyService(this).add(model)).subscribe(new NtkObserver<ErrorException<EstatePropertyModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyModel> response) {
                if (response.IsSuccess) {
                    Toasty.success(NewEstateActivity.this, "ملک شما ثبت شد").show();
                    finish();
                } else {
                    Toasty.error(NewEstateActivity.this, "هنگام ثبت خطا رخ داد مجددا تلاش نمایید" + "\n+" +
                            response.ErrorMessage).show();
                    showContent();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toasty.error(NewEstateActivity.this, "هنگام ثبت خطا رخ داد مجددا تلاش نمایید").show();
                showContent();
            }
        });
    }

    public void onUploadingStep() {
        uploadProcess = true;
    }

    public void uploadFinished() {
        uploadProcess = false;
    }
    public boolean isUploaded() {
       return !uploadProcess ;
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

    public EstatePropertyModel model() {
        return model;
    }

    public static void START_ACTIVITY(Context c) {
        //user has logged in and saved his user Id
        if (Preferences.with(c).appVariableInfo().isLogin() && Preferences.with(c).UserInfo().userId() > 0)
            c.startActivity(new Intent(c, NewEstateActivity.class));
        else {
            //show dialog to go to login page
            SweetAlertDialog dialog = new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE);
            dialog.setTitle("خطا در انجام عملیات");
            dialog.setContentText("برای ثبت ملک نیاز است که به حساب خود وارد شوید. آیا مایلید به صفحه ی ورود هدایت شوید؟");
            dialog.setConfirmButton("بلی", d -> {
                Preferences.with(d.getContext()).appVariableInfo().set_registerNotInterested(false);
                Intent i = new Intent(d.getContext(), AuthWithSmsActivity.class);
                //clear all activity that open before
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                d.getContext().startActivity(i);
                d.dismiss();
            });
            dialog.setCancelButton("تمایل ندارم", SweetAlertDialog::dismiss);
            dialog.show();
        }
    }
}
