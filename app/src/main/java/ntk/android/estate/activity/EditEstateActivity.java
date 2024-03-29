package ntk.android.estate.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;
import java9.util.stream.StreamSupport;
import ntk.android.base.Extras;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.GenericErrors;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.estate.EstateContractModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailValueModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.utill.AppUtil;
import ntk.android.estate.R;
import ntk.android.estate.fragment.EditEstateFragment1;
import ntk.android.estate.fragment.EditEstateFragment5;
import ntk.android.estate.fragment.NewEstateFragment4;
import ntk.android.estate.fragment.NewEstateFragment5;

public class EditEstateActivity extends NewEstateActivity {
    String Id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Id = getIntent().getExtras().getString(Extras.EXTRA_FIRST_ARG);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void showFragment1() {
        stepNumber = 1;
        title.setText("مشخصات ملک");
        findViewById(R.id.backBtn).setVisibility(View.GONE);

        EditEstateFragment1 fragment = new EditEstateFragment1();
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                showFragment2();
        });
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }

    @Override
    protected void showFragment5() {
        stepNumber = 5;
        title.setText("تصاویر ملک");
        findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.addNewBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.continueBtn).setVisibility(View.GONE);

        EditEstateFragment5 fragment = new EditEstateFragment5();
        findViewById(R.id.addNewBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                if (!uploadProcess) {
                    createModel();
                } else {
                    Toasty.info(EditEstateActivity.this, "در حال بارگذاری فایل انتخابی شما...", Toasty.LENGTH_LONG).show();
                }
        });

        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    @Override
    protected void afterCreate() {
        if (AppUtil.isNetworkAvailable(this)) {
            switcher.showProgressView();
            ServiceExecute.execute(new EstatePropertyService(this).getOneByEdit(Id))
                    .subscribe(new ErrorExceptionObserver<EstatePropertyModel>(switcher::showErrorView) {

                        @Override
                        protected void SuccessResponse(ErrorException<EstatePropertyModel> ContentResponse) {
                            model = ContentResponse.Item;
                            //add other src
                            OtherImageSrc = new ArrayList<>();
                            OtherImageIds = new ArrayList<>();
                            OtherImageSrc.addAll(model.LinkFileIdsSrc);
                            OtherImageIds.addAll(Arrays.asList(model.LinkFileIds.split(",")));

                            //sync property and its values
                            StreamSupport.stream(model.PropertyDetailGroups).forEach(estatePropertyDetailGroupModel -> {
                                StreamSupport.stream(estatePropertyDetailGroupModel.PropertyDetails).forEach(estatePropertyDetailModel -> {
                                    EstatePropertyDetailValueModel estatePropertyDetailValueModel = StreamSupport.stream(model.PropertyDetailValues).filter(valueModel -> valueModel.LinkPropertyDetailId.equals(estatePropertyDetailModel.Id)).findFirst().orElse(null);
                                    estatePropertyDetailModel.Value = estatePropertyDetailValueModel != null ? estatePropertyDetailValueModel.Value : null;
                                });
                            });
                            switcher.showContentView();
                            showFragment1();

                        }

                        @Override
                        protected Runnable tryAgainMethod() {
                            return EditEstateActivity.this::afterCreate;
                        }

                    });

        } else {
            new GenericErrors().netError(switcher::showErrorView, this::afterCreate);
        }

    }

    @Override
    protected void createModel() {
        showProgress();
        model.PropertyDetailGroups = null;
        model.UploadFileGUID = new ArrayList<>();
        if (MainImage_GUID != null && !MainImage_GUID.equalsIgnoreCase(""))
            model.UploadFileGUID.add(MainImage_GUID);
        //remove uploaded before id form list
        OtherImageIds.removeAll(  new ArrayList<String>(Arrays.asList(model.LinkFileIds.split(","))));

        model.UploadFileGUID.addAll(OtherImageIds);
        for (EstateContractModel model :
                model.Contracts) {
            model.LinkCoreCurrencyId = selectedCurrency.Id;
        }
        ServiceExecute.execute(new EstatePropertyService(this).edit(model)).subscribe(new NtkObserver<ErrorException<EstatePropertyModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyModel> response) {
                if (response.IsSuccess) {
                    Toasty.success(EditEstateActivity.this, "ملک شما ویرایش شد").show();
                    finish();
                } else {
                    Toasty.error(EditEstateActivity.this, "هنگام ثبت خطا رخ داد مجددا تلاش نمایید" + "\n+" +
                            response.ErrorMessage).show();
                    showContent();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toasty.error(EditEstateActivity.this, "هنگام ثبت خطا رخ داد مجددا تلاش نمایید").show();
                showContent();
            }
        });
    }

}
