package ntk.android.estate.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.GenericErrors;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.estate.EstateCompanyModel;
import ntk.android.base.services.estate.EstatePropertyCompanyService;
import ntk.android.base.utill.AppUtil;
import ntk.android.estate.R;
import ntk.android.estate.adapter.ImageSliderAdapter;

public class CompanyDetailActivity extends BaseActivity {
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);
        id = getIntent().getExtras().getString(Extras.EXTRA_FIRST_ARG);
        findViewById(R.id.imgBackDetail).setOnClickListener(v -> finish());
        getData();
    }

    private void getData() {

        if (AppUtil.isNetworkAvailable(this)) {
            switcher.showProgressView();
            ServiceExecute.execute(new EstatePropertyCompanyService(this).getOne(id)).subscribe(new ErrorExceptionObserver<EstateCompanyModel>(switcher::showErrorView) {

                @Override
                protected void SuccessResponse(ErrorException<EstateCompanyModel> ContentResponse) {
                    bindContentData(ContentResponse.Item);
                    switcher.showContentView();
                }

                @Override
                protected Runnable tryAgainMethod() {
                    return CompanyDetailActivity.this::getData;
                }

            });

        } else {
            new GenericErrors().netError(switcher::showErrorView, this::getData);
        }
    }

    private void bindContentData(EstateCompanyModel item) {
        ((TextView) findViewById(R.id.idTextView)).setText(item.Title);
        ((TextView) findViewById(R.id.lblTitleDetail)).setText(item.Title);
        WebView description = findViewById(R.id.WebViewDesc);
        WebView body = findViewById(R.id.WebViewBody);
        description.getSettings().setJavaScriptEnabled(true);
        description.getSettings().setBuiltInZoomControls(true);
        body.getSettings().setJavaScriptEnabled(true);
        body.getSettings().setBuiltInZoomControls(true);
        if (item.Description != null && !item.Description.equalsIgnoreCase("null"))
            description.loadData("<html dir=\"rtl\" lang=\"\"><body>" + item.Description + "</body></html>", "text/html; charset=utf-8", "UTF-8");
        else
            description.setVisibility(View.GONE);
        if (item.Body != null && !item.Body.equalsIgnoreCase("null"))
            body.loadData("<html dir=\"rtl\" lang=\"\"><body>" + item.Body + "</body></html>", "text/html; charset=utf-8", "UTF-8");
        else
            body.setVisibility(View.GONE);
        SliderView sliderView = findViewById(R.id.imageSlider);
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(this);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();
        List<String> images = new ArrayList<>();
        images.add(item.LinkMainImageIdSrc);
        images.addAll(item.LinkExtraImageIdsSrc);
        sliderAdapter.renewUrls(images);
    }
}
