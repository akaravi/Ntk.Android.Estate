package ntk.android.estate.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.GenericErrors;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.utill.AppUtill;
import ntk.android.estate.R;

public class EstateDetailActivity extends BaseActivity {
    public String Id = "";
    private EstatePropertyModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estate_dtail_activity);
        initView();
        getContent();
    }

    private void getContent() {
        if (AppUtill.isNetworkAvailable(this)) {

            ServiceExecute.execute(new EstatePropertyService(this).getOne(Id))
                    .subscribe(new ErrorExceptionObserver<EstatePropertyModel>(switcher::showErrorView) {

                        @Override
                        protected void SuccessResponse(ErrorException<EstatePropertyModel> ContentResponse) {
                            model = ContentResponse.Item;
                            bindContentData();
                        }

                        @Override
                        protected Runnable tryAgainMethod() {
                            return EstateDetailActivity.this::getContent;
                        }

                    });
        } else {
            new GenericErrors().netError(switcher::showErrorView, this::getContent);
        }

    }

    private void bindContentData() {

    }

    private void initView() {
        Id = getIntent().getExtras().getString(Extras.EXTRA_FIRST_ARG);

    }
}
