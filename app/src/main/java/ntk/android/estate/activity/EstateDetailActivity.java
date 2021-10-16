package ntk.android.estate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.appclass.UpdateClass;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.GenericErrors;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.utill.AppUtill;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstateConstractAdapter;
import ntk.android.estate.adapter.ImageSliderAdapter;
import ntk.android.estate.adapter.PropertyDetailGroupsAdapter;

public class EstateDetailActivity extends BaseActivity {
    public String Id = "";
    private EstatePropertyModel model;
    ImageSliderAdapter imageSlider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estate_dtail_activity);
        initView();
        getContent();
    }

    private void initView() {
        Id = getIntent().getExtras().getString(Extras.EXTRA_FIRST_ARG);
        ((TextView) findViewById(R.id.lblTitleDetail)).setText(getIntent().getExtras().getString(Extras.EXTRA_SECOND_ARG));
        findViewById(R.id.imgBackDetail).setOnClickListener(view -> finish());
        findViewById(R.id.imgShareDetail).setOnClickListener(view -> ClickShare());
        SliderView sliderView = findViewById(R.id.imageSlider);
        imageSlider = new ImageSliderAdapter(this);
        sliderView.setSliderAdapter(imageSlider);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();
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
        List<String> images = new ArrayList<>();
        images.add(model.LinkMainImageIdSrc);
        images.addAll(model.LinkExtraImageIdsSrc);
        imageSlider.renewUrls(images);
        //correct location add
        //add contracts
        RecyclerView contractsRc = findViewById(R.id.contractsRc);
        contractsRc.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        contractsRc.setAdapter(new EstateConstractAdapter(model.Contracts));
        //add details
        RecyclerView detailsRc = findViewById(R.id.detailsGroupRc);
        detailsRc.setAdapter(new PropertyDetailGroupsAdapter(model.PropertyDetailGroups));
    }

    private String createShareMassage() {
        String message = getIntent().getExtras().getString(Extras.EXTRA_SECOND_ARG);
        if (model != null) {
            message += "\n" + model.Description + "\n";
            return message;
        }
        return message;
    }

    public void ClickShare() {
        UpdateClass updateInfo = Preferences.with(this).appVariableInfo().updateInfo();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String message = createShareMassage();
        shareIntent.putExtra(Intent.EXTRA_TEXT, message + "\n\n\n" + this.getString(ntk.android.base.R.string.app_name) + "\n" + getString(ntk.android.base.R.string.per_download_link) + "\n" + updateInfo.url);
        shareIntent.setType("text/txt");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.startActivity(Intent.createChooser(shareIntent, getString(ntk.android.base.R.string.per_share_to)));
    }


}
