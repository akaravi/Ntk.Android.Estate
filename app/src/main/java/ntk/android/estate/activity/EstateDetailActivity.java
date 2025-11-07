package ntk.android.estate.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.maplibre.android.annotations.MarkerOptions;
import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.OnMapReadyCallback;
import org.maplibre.android.maps.Style;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.GenericErrors;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.dtomodel.core.CoreModuleReportAbuseDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.ErrorExceptionBase;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.enums.EnumSortType;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.utill.AppUtil;
import ntk.android.base.utill.FontManager;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstateContractAdapter;
import ntk.android.estate.adapter.EstatePropertiesInDetailAdapter;
import ntk.android.estate.adapter.ImageSliderAdapter;
import ntk.android.estate.adapter.PropertyDetailGroupsAdapter;

public class EstateDetailActivity extends BaseActivity {
    public String Id = "";
    private EstatePropertyModel model;
    ImageSliderAdapter sliderAdapter;
    MapLibreMap map;
    MapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate_dtail);

        initView(savedInstanceState);
        setFont();
        getContent();
    }

    private void setFont() {
        Typeface tf = FontManager.T1_Typeface(this);
        ((TextView) findViewById(R.id.lblTitleDetail)).setTypeface(tf);
        ((TextView) findViewById(R.id.txtArea)).setTypeface(tf);
        ((TextView) findViewById(R.id.idTextView)).setTypeface(tf);
        ((TextView) findViewById(R.id.dateTv)).setTypeface(tf);
        ((TextView) findViewById(R.id.textView)).setTypeface(tf);
        ((MaterialButton) (findViewById(R.id.toggleMaps))).setTypeface(tf);
        ((MaterialButton) (findViewById(R.id.phoneButton))).setTypeface(tf);
    }

    private void initView(Bundle savedInstanceState) {
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
        //toggle map is gone until model get
        (findViewById(R.id.toggleMaps)).setVisibility(View.INVISIBLE);
        findViewById(R.id.toggleMaps).setOnClickListener(view -> {
            View mapView = findViewById(R.id.map_view);
            MaterialButton button = (findViewById(R.id.toggleMaps));
            SliderView slider = findViewById(R.id.imageSlider);
            if (mapView.getVisibility() == View.VISIBLE) {
                button.setText("نقشه");
                slider.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.GONE);
            } else {
                button.setText("تصاویر");
                slider.setVisibility(View.GONE);
                mapView.setVisibility(View.VISIBLE);
            }
        });
        if (Preferences.with(this).appVariableInfo().isLogin())
            findViewById(R.id.imgFavDetail).setVisibility(View.INVISIBLE);
        else
            findViewById(R.id.imgFavDetail).setVisibility(View.VISIBLE);
        //favorite button listener
        findViewById(R.id.imgFavDetail).setOnClickListener(view -> {
            if (model != null) {
                Observer<ErrorExceptionBase> subscriptor = new NtkObserver<ErrorExceptionBase>() {
                    @Override
                    public void onNext(@NonNull ErrorExceptionBase errorExceptionBase) {
                        if (errorExceptionBase.IsSuccess) {
                            model.IsFavorite = !model.IsFavorite;
                            if (model.IsFavorite) {
                                ((ImageView) findViewById(R.id.imgFavDetail)).setImageResource(R.drawable.ic_fav_full);
                                Toasty.success(view.getContext(), "به لیست علافه مندی اضافه شد", Toasty.LENGTH_SHORT).show();
                            } else {
                                ((ImageView) findViewById(R.id.imgFavDetail)).setImageResource(R.drawable.ic_fav);
                                Toasty.success(view.getContext(), "از لیست علافه مندی خارج شد", Toasty.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toasty.error(EstateDetailActivity.this, "خطا در انجام عملیات").show();
                    }
                };
                if (model.IsFavorite)
                    ServiceExecute.execute(new EstatePropertyService(EstateDetailActivity.this).removeFavorite(model.Id)).subscribe(subscriptor);
                else
                    ServiceExecute.execute(new EstatePropertyService(EstateDetailActivity.this).addFavorite(model.Id)).subscribe(subscriptor);

            }
        });
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapLibreMap mapLibreMap) {
                map = mapLibreMap;
                map.setStyle(new Style.Builder().fromUri("https://demotiles.maplibre.org/style.json"), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        if (model != null && model.Geolocationlatitude != null && model.Geolocationlongitude != null && model.Geolocationlatitude != 0 && model.Geolocationlongitude != 0) {
                            (findViewById(R.id.toggleMaps)).setVisibility(View.VISIBLE);
                            LatLng point = new LatLng(model.Geolocationlatitude, model.Geolocationlongitude);
                            if (map != null) {
                                map.addMarker(new MarkerOptions().position(point).title(model.Title));
                                map.moveCamera(CameraUpdateFactory.newLatLng(point));
                            }
                        }
                    }
                });
            }
        });

        //call button
        findViewById(R.id.phoneButton).setOnClickListener(view -> call());
        findViewById(R.id.reportBtn).setOnClickListener(view -> showReportDialog());
    }


    private void getContent() {
        if (AppUtil.isNetworkAvailable(this)) {
            switcher.showProgressView();
            ServiceExecute.execute(new EstatePropertyService(this).getOne(Id))
                    .subscribe(new ErrorExceptionObserver<EstatePropertyModel>(switcher::showErrorView) {

                        @Override
                        protected void SuccessResponse(ErrorException<EstatePropertyModel> ContentResponse) {
                            model = ContentResponse.Item;
                            switcher.showContentView();
                            bindContentData();
                            //get all related estate
                            FilterModel getAllFilter = new FilterModel();
                            getAllFilter.setSortType(EnumSortType.Descending.index());
                            getAllFilter.addFilter(new FilterDataModel().setPropertyName("LinkPropertyTypeLanduseId")
                                    .setStringValue(model.LinkPropertyTypeLanduseId));
                            getSimilar(getAllFilter);
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

    public void getSimilar(FilterModel getAllFilter) {
        ServiceExecute.execute(new EstatePropertyService(this).getAll(getAllFilter))
                .subscribe(new ErrorExceptionObserver<EstatePropertyModel>(switcher::showErrorView) {

                    @Override
                    protected void SuccessResponse(ErrorException<EstatePropertyModel> response) {
                        EstatePropertiesInDetailAdapter adapter = new EstatePropertiesInDetailAdapter(response.ListItems);
                        RecyclerView rc = findViewById(R.id.RcAllEstate);
                        rc.setAdapter(adapter);
                        rc.setLayoutManager(new LinearLayoutManager(EstateDetailActivity.this, RecyclerView.HORIZONTAL, false));
                    }

                    @Override
                    protected Runnable tryAgainMethod() {
                        return () -> getContent();
                    }
                });
    }

    private void bindContentData() {
        if (model.AboutAgentTel != null) {
            findViewById(R.id.phoneButton).setVisibility(View.VISIBLE);
            findViewById(R.id.telPadding).setVisibility(View.VISIBLE);

        }
        ((TextView) findViewById(R.id.txtArea)).setText(model.LinkLocationIdParentTitle + " - " + model.LinkLocationIdTitle);
        if (model.IsFavorite)
            ((ImageView) findViewById(R.id.imgFavDetail)).setImageResource(R.drawable.ic_fav_full);
        else
            ((ImageView) findViewById(R.id.imgFavDetail)).setImageResource(R.drawable.ic_fav);
        if (model.CaseCode != null)
            ((TextView) findViewById(R.id.idTextView)).setText("شماره ملک : " + model.CaseCode);
        ((TextView) findViewById(R.id.dateTv)).setText("" + AppUtil.DateDifferenceNow(model.CreatedDate));
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
        if (model.Geolocationlatitude != null && model.Geolocationlongitude != null && model.Geolocationlatitude != 0 && model.Geolocationlongitude != 0) {
            (findViewById(R.id.toggleMaps)).setVisibility(View.VISIBLE);
            LatLng point = new LatLng(model.Geolocationlatitude, model.Geolocationlongitude);
            if (map != null) {
                map.moveCamera(CameraUpdateFactory.newLatLng(point));
            }
        }
    }

    private String createShareMassage() {
        String message = getIntent().getExtras().getString(Extras.EXTRA_SECOND_ARG);
        if (model != null) {
            message += "\n" + model.Description + "\n";
            return message;
        } else {
            return "";
        }
    }

    private void ClickShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String message = createShareMassage();
        if (model != null && model.LinkMainImageIdSrc != null && !model.LinkMainImageIdSrc.equals("")) {
            Uri imageUri = Uri.parse(model.LinkMainImageIdSrc);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        } else
            intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(message).toString());
        startActivity(Intent.createChooser(intent, "اشتراک گذاری در..."));
    }

    private void call() {
        if (model.AboutAgentTel != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", model.AboutAgentTel, null));
            startActivity(intent);
        }

    }

    public void showReportDialog() {
        new CoreModuleReportAbuseDtoModel().show(this, model.Id, getSupportFragmentManager());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
