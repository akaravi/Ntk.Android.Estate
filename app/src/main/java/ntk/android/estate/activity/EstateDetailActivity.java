package ntk.android.estate.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carto.styles.AnimationStyle;
import com.carto.styles.AnimationStyleBuilder;
import com.carto.styles.AnimationType;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.google.android.material.button.MaterialButton;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.neshan.common.model.LatLng;
import org.neshan.mapsdk.MapView;
import org.neshan.mapsdk.internal.utils.BitmapUtils;
import org.neshan.mapsdk.model.Marker;

import java.util.ArrayList;
import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.appclass.UpdateClass;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.GenericErrors;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.utill.AppUtill;
import ntk.android.base.utill.FontManager;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstateContractAdapter;
import ntk.android.estate.adapter.EstatePropertyAdapter;
import ntk.android.estate.adapter.ImageSliderAdapter;
import ntk.android.estate.adapter.PropertyDetailGroupsAdapter;

public class EstateDetailActivity extends BaseActivity {
    public String Id = "";
    private EstatePropertyModel model;
    ImageSliderAdapter sliderAdapter;
    FilterModel getAllFilter;
    private boolean mapIsSet = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate_dtail);

        initView();
        setFont();
        getContent();
    }

    private void setFont() {
        ((TextView) findViewById(R.id.lblTitleDetail)).setTypeface(FontManager.T1_Typeface(this));
        ((TextView) findViewById(R.id.idTextView)).setTypeface(FontManager.T1_Typeface(this));
        ((TextView) findViewById(R.id.dateTv)).setTypeface(FontManager.T1_Typeface(this));
    }

    private void initView() {
        Id = getIntent().getExtras().getString(Extras.EXTRA_FIRST_ARG);
        ((TextView) findViewById(R.id.lblTitleDetail)).setText(getIntent().getExtras().getString(Extras.EXTRA_SECOND_ARG));
        findViewById(R.id.imgBackDetail).setOnClickListener(view -> finish());
        findViewById(R.id.imgShareDetail).setOnClickListener(view -> ClickShare());
        SliderView sliderView = findViewById(R.id.imageSlider);
        sliderAdapter = new ImageSliderAdapter(this);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();
        findViewById(R.id.toggleMaps).setOnClickListener(view -> {
            View mapView = findViewById(R.id.map);
            MaterialButton button = (findViewById(R.id.toggleMaps));
            SliderView slider = findViewById(R.id.imageSlider);
            if (mapView.getVisibility() == View.VISIBLE) {
                button.setText("نقشه");
                slider.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.GONE);
            } else {
                button.setText("jwh");
                slider.setVisibility(View.GONE);
                mapView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getContent() {
        if (AppUtill.isNetworkAvailable(this)) {
            switcher.showProgressView();
            ServiceExecute.execute(new EstatePropertyService(this).getOne(Id))
                    .subscribe(new ErrorExceptionObserver<EstatePropertyModel>(switcher::showErrorView) {

                        @Override
                        protected void SuccessResponse(ErrorException<EstatePropertyModel> ContentResponse) {
                            model = ContentResponse.Item;
                            switcher.showContentView();
                            bindContentData();

                        }

                        @Override
                        protected Runnable tryAgainMethod() {
                            return EstateDetailActivity.this::getContent;
                        }

                    });
            //get all related estate
            getAllFilter = new FilterModel();
            ServiceExecute.execute(new EstatePropertyService(this).getAll(getAllFilter))
                    .subscribe(new ErrorExceptionObserver<EstatePropertyModel>(switcher::showErrorView) {

                        @Override
                        protected void SuccessResponse(ErrorException<EstatePropertyModel> response) {
                            EstatePropertyAdapter adapter = new EstatePropertyAdapter(EstateDetailActivity.this, response.ListItems);
                            RecyclerView rc = findViewById(R.id.RcAllEstate);
                            rc.setAdapter(adapter);
                            rc.setLayoutManager(new LinearLayoutManager(EstateDetailActivity.this, RecyclerView.HORIZONTAL, false));
                        }

                        @Override
                        protected Runnable tryAgainMethod() {
                            return () -> getContent();
                        }
                    });

        } else {
            new GenericErrors().netError(switcher::showErrorView, this::getContent);
        }

    }

    private void bindContentData() {
        if (model.CaseCode != null)
            ((TextView) findViewById(R.id.idTextView)).setText("شماره ملک : " + model.CaseCode);
        ((TextView) findViewById(R.id.dateTv)).setText("" + AppUtill.DateDifferenceNow(model.CreatedDate));
        List<String> images = new ArrayList<>();
        images.add(model.LinkMainImageIdSrc);
        images.addAll(model.LinkExtraImageIdsSrc);
        sliderAdapter.renewUrls(images);
        //correct location add
        //add contracts
        RecyclerView contractsRc = findViewById(R.id.contractsRc);
        contractsRc.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        contractsRc.setAdapter(new EstateContractAdapter(model.Contracts));
        //add desc
        WebView webViewBody = findViewById(R.id.WebViewDesc);
        webViewBody.loadData("<html dir=\"rtl\" lang=\"\"><body>" + model.Description + "</body></html>", "text/html; charset=utf-8", "UTF-8");

        //add details
        RecyclerView detailsRc = findViewById(R.id.detailsGroupRc);
        detailsRc.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        detailsRc.setAdapter(PropertyDetailGroupsAdapter.INIT(model.PropertyDetailGroups, model.PropertyDetailValues));
//        check location is set or not
        if (model.Geolocationlatitude != null & model.Geolocationlongitude != null) {
            (findViewById(R.id.toggleMaps)).setVisibility(View.VISIBLE);
            LatLng point = new LatLng(53.529929, 35.164676);
            ((MapView) findViewById(R.id.map)).addMarker(createMarker(point));

        }
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


    public void onMapReady(@NonNull int googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        // googleMapOptions.mapType(googleMap.MAP_TYPE_HYBRID)
//        //todo set defualt locaiton
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0.0, 0.0), 15));
//        // Add a marker in Sydney and move the camera
//        if (model != null) {
//            //if model lat lang is not null
//            LatLng latLng = new LatLng(model.Geolocationlatitude, model.Geolocationlongitude);
//            mMap.addMarker(new MarkerOptions()
//                    .position(latLng)
//                    .title("مکان مورد نظر"));
//            mapIsSet = true;
//        }
    }

    // This method gets a LatLng as input and adds a marker on that position
    private Marker createMarker(LatLng loc) {
        // Creating animation for marker. We should use an object of type AnimationStyleBuilder, set
        // all animation features on it and then call buildStyle() method that returns an object of type
        // AnimationStyle
        AnimationStyle animSt;
        AnimationStyleBuilder animStBl = new AnimationStyleBuilder();
        animStBl.setFadeAnimationType(AnimationType.ANIMATION_TYPE_SMOOTHSTEP);
        animStBl.setSizeAnimationType(AnimationType.ANIMATION_TYPE_SPRING);
        animStBl.setPhaseInDuration(0.5f);
        animStBl.setPhaseOutDuration(0.5f);
        animSt = animStBl.buildStyle();

        // Creating marker style. We should use an object of type MarkerStyleCreator, set all features on it
        // and then call buildStyle method on it. This method returns an object of type MarkerStyle
        MarkerStyleBuilder markStCr = new MarkerStyleBuilder();
        markStCr.setSize(30f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker)));
        // AnimationStyle object - that was created before - is used here
        markStCr.setAnimationStyle(animSt);
        MarkerStyle markSt = markStCr.buildStyle();

        // Creating marker
        return new Marker(loc, markSt);
    }
}
