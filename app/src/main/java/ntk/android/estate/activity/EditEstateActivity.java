package ntk.android.estate.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import java9.util.stream.StreamSupport;
import ntk.android.base.Extras;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.GenericErrors;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailValueModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.utill.AppUtil;
import ntk.android.estate.adapter.PropertyDetailGroupsAdapter;

public class EditEstateActivity extends NewEstateActivity {
    String Id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Id = getIntent().getExtras().getString(Extras.EXTRA_FIRST_ARG);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void afterCreate() {
        if (AppUtil.isNetworkAvailable(this)) {
            switcher.showProgressView();
            ServiceExecute.execute(new EstatePropertyService(this).getOne(Id))
                    .subscribe(new ErrorExceptionObserver<EstatePropertyModel>(switcher::showErrorView) {

                        @Override
                        protected void SuccessResponse(ErrorException<EstatePropertyModel> ContentResponse) {
                            model = ContentResponse.Item;
                            //create list of values base on details
//                            StreamSupport.stream(model.PropertyDetailGroups).
//                                    forEach(estatePropertyDetailGroupModel -> {
//                                        estatePropertyDetailGroupModel.PropertyValues = new ArrayList<>();
//                                        StreamSupport.stream(estatePropertyDetailGroupModel.PropertyDetails)
//                                                .forEach(estatePropertyDetailModel -> {
//                                                    EstatePropertyDetailValueModel value = new EstatePropertyDetailValueModel();
//                                                    value.LinkPropertyDetailId = estatePropertyDetailModel.Id;
//                                                    value.PropertyDetail = estatePropertyDetailModel;
//                                                    estatePropertyDetailGroupModel.PropertyValues.add(value);
//                                                });
//                                    });
                            //sync property and its values
                            for (EstatePropertyDetailGroupModel detail :
                                    model.PropertyDetailGroups) {
                                detail.PropertyValues = new ArrayList<>();
                                for (EstatePropertyDetailValueModel value :
                                        model.PropertyDetailValues) {
                                    if ( StreamSupport.stream(detail.PropertyDetails).anyMatch(j -> j.Id.equals(value.LinkPropertyDetailId))) {
                                        detail.PropertyValues.add(value);
                                    }
                                }
                            }
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
        model.UploadFileGUID.addAll(OtherImageIds);

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
