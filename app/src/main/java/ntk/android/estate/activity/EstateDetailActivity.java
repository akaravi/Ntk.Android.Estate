package ntk.android.estate.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import ir.map.sdk_map.MapirStyle;
import ir.map.sdk_map.maps.MapView;
import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.appclass.UpdateClass;
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
    MapboxMap map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate_dtail);

        initView();
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
        //favorite button listener
        findViewById(R.id.imgFavDetail).setOnClickListener(view -> {
            if (model != null) {
                Observer<ErrorExceptionBase> subscriptor = new NtkObserver<ErrorExceptionBase>() {
                    @Override
                    public void onNext(@NonNull ErrorExceptionBase errorExceptionBase) {
                        if (errorExceptionBase.IsSuccess) {
                            model.IsFavorite = !model.IsFavorite;
                            if (model.IsFavorite) {
                                ((ImageView) findViewById(R.id.imgHeartDetail)).setImageResource(R.drawable.ic_fav_full);
                                Toasty.success(view.getContext(), "به لیست علافه مندی اضافه شد", Toasty.LENGTH_SHORT).show();
                            } else {
                                ((ImageView) findViewById(R.id.imgHeartDetail)).setImageResource(R.drawable.ic_fav);
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
        MapView mapView = findViewById(R.id.map_view);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                map = mapboxMap;
                mapboxMap.setMinZoomPreference(12);
                map.setStyle(new Style.Builder().fromUri(MapirStyle.MAIN_MOBILE_VECTOR_STYLE), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
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
            ((ImageView) findViewById(R.id.imgHeartDetail)).setImageResource(R.drawable.ic_fav_full);
        else
            ((ImageView) findViewById(R.id.imgHeartDetail)).setImageResource(R.drawable.ic_fav);
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

                map.addMarker(GetLocationActivity.MakeMarker(this, point));

            }
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

    private void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.AboutAgentTel));
        startActivity(intent);
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

    public void showReportDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(ntk.android.base.R.layout.dialog_report_add);
        TextView Lbl = dialog.findViewById(R.id.lblTitleDialogAddReport);
        Lbl.setTypeface(FontManager.T1_Typeface(this));


        EditText subject = dialog.findViewById(R.id.txtContentDialogAddReport);
        subject.setTypeface(FontManager.T1_Typeface(this));


        Button Btn = dialog.findViewById(ntk.android.base.R.id.btnSubmitDialogReportAdd);
        Btn.setTypeface(FontManager.T1_Typeface(this));

        Btn.setOnClickListener(v -> {
            if (subject.getText().toString().isEmpty()) {
                Toast.makeText(this, ntk.android.base.R.string.per_insert_num, Toast.LENGTH_SHORT).show();
            } else {
                if (AppUtil.isNetworkAvailable(this)) {
//                        NewsCommentModel add = new NewsCommentModel();
                    String text = subject.getText().toString();
                    CoreModuleReportAbuseDtoModel m = new CoreModuleReportAbuseDtoModel();
                    m.ModuleEntityId = model.Id;
                    m.SubjectBody = text;
                    ServiceExecute.execute(new EstatePropertyService(this).report(m))
                            .subscribe(new NtkObserver<ErrorException<EstatePropertyModel>>() {
                                @Override
                                public void onNext(ErrorException<EstatePropertyModel> e) {
                                    if (e.IsSuccess) {

                                        dialog.dismiss();
                                        Toasty.success(EstateDetailActivity.this, ntk.android.base.R.string.success_comment).show();
                                    } else {
                                        dialog.dismiss();
                                        Toasty.warning(EstateDetailActivity.this, e.ErrorMessage).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Snackbar.make(findViewById(ntk.android.base.R.id.mainLayoutDetail), ntk.android.base.R.string.error_raised, Snackbar.LENGTH_INDEFINITE).setAction(ntk.android.base.R.string.try_again, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //todo add
                                        }
                                    }).show();
                                }
                            });
                } else {
                    Toasty.error(EstateDetailActivity.this, "خطا در ارسال اطلاعات").show();

                }
            }
        });

        dialog.show();
    }
}
