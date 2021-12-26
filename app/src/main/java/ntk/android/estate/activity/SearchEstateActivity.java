package ntk.android.estate.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

import java9.util.stream.Collectors;
import java9.util.stream.StreamSupport;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.core.CoreLocationModel;
import ntk.android.base.entitymodel.enums.EnumClauseType;
import ntk.android.base.entitymodel.enums.EnumSearchType;
import ntk.android.base.entitymodel.estate.EstateContractTypeModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailValueModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeUsageModel;
import ntk.android.base.services.estate.EstateContractTypeService;
import ntk.android.base.services.estate.EstatePropertyDetailGroupService;
import ntk.android.base.services.estate.EstatePropertyTypeLanduseService;
import ntk.android.base.services.estate.EstatePropertyTypeService;
import ntk.android.base.services.estate.EstatePropertyTypeUsageService;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstateContractAdapterSelector;
import ntk.android.estate.adapter.EstatePropertyLandUseAdapterSelector;
import ntk.android.estate.adapter.EstatePropertyTypeAdapterSelector;
import ntk.android.estate.adapter.SearchPropertyDetailGroupAdapterSelector;
import ntk.android.estate.view.component.LocaionAutoCompleteTextView;

public class SearchEstateActivity extends BaseActivity {
    private List<EstatePropertyTypeUsageModel> typeUsages;
    private List<EstatePropertyTypeModel> propertyType;
    private List<EstatePropertyTypeLanduseModel> landUses;
    CoreLocationModel selectedLocation;
    private EstatePropertyTypeUsageModel PropertyTypeUsage;
    private EstatePropertyTypeLanduseModel PropertyTypeLanduse;
    private EstateContractTypeModel ContractModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate_search);

        initView();
        getLocations();
        getEstateType();
        getContractTypes();
    }

    private void initView() {
        findViewById(R.id.imgToolbarBack).setOnClickListener(view -> finish());
        ((TextView) findViewById(R.id.txtToolbarTitle)).setText("جست و جو");
        //set font
        Typeface t1 = FontManager.T1_Typeface(this);
        ((TextView) findViewById(R.id.txtToolbarTitle)).setTypeface(t1);
        ((TextView) findViewById(R.id.titleExpandTv)).setTypeface(t1);
        ((TextView) findViewById(R.id.locationExpandTv)).setTypeface(t1);
        ((TextView) findViewById(R.id.contractTypeExpandTv)).setTypeface(t1);
        ((TextView) findViewById(R.id.contractDetailExpandTv)).setTypeface(t1);
        ((TextView) findViewById(R.id.typeUsageExpandTv)).setTypeface(t1);
        ((TextView) findViewById(R.id.propertyTypeExpandTv)).setTypeface(t1);
        ((TextView) findViewById(R.id.areaExpandTv)).setTypeface(t1);
        //font for contract detail
        //for textInput layout
        ((TextInputLayout) findViewById(R.id.etl1)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etl2)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etl3)).setTypeface(t1);
        //for TextInputEditText
        ((TextInputEditText) findViewById(R.id.et1)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.et2)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.et3)).setTypeface(t1);
        //for checkView
        ((TextView) findViewById(R.id.checkbox_row1).findViewById(R.id.cbText)).setTypeface(t1);
        ((TextView) findViewById(R.id.checkbox_row2).findViewById(R.id.cbText)).setTypeface(t1);
        ((TextView) findViewById(R.id.checkbox_row3).findViewById(R.id.cbText)).setTypeface(t1);
        //add expand listener
        findViewById(R.id.titleExpander).setOnClickListener(expandLister(findViewById(R.id.EstateTitleTextInput), findViewById(R.id.titleExpandIcon)));
        findViewById(R.id.locationExpander).setOnClickListener(expandLister(findViewById(R.id.EstateProvinceTextInput), findViewById(R.id.locationExpandIcon)));
        findViewById(R.id.contractTypeExpander).setOnClickListener(expandLister(findViewById(R.id.contractsRc), findViewById(R.id.contractTypeExpandIcon)));
        findViewById(R.id.contractDetailExpander).setOnClickListener(expandLister(findViewById(R.id.ContractDetailView), findViewById(R.id.contractDetailExpandIcon)));
        findViewById(R.id.propertyTypeExpander).setOnClickListener(expandLister(findViewById(R.id.propertyTypeRv), findViewById(R.id.propertyTypeExpandIcon)));
        findViewById(R.id.typeUsageExpander).setOnClickListener(expandLister(findViewById(R.id.TypeUsageRc), findViewById(R.id.typeUsageExpandIcon)));
        findViewById(R.id.areaExpander).setOnClickListener(expandLister(findViewById(R.id.EstateAreaTextInput), findViewById(R.id.areaExpandIcon)));
        findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                Search();
                view.setEnabled(true);
            }
        });
        MaterialAutoCompleteTextView spinner = (findViewById(R.id.EstateProvinceAutoComplete));
        LocaionAutoCompleteTextView locaionAutoCompleteTextView = new LocaionAutoCompleteTextView();
        locaionAutoCompleteTextView.addOnAutoCompleteTextViewTextChangedObserver(spinner,
                location -> {
                    selectedLocation = location;

                });
    }

    private void Search() {
        FilterModel filter = new FilterModel();
        String title = ((TextInputEditText) findViewById(R.id.EstateTitleEditText)).getText().toString().trim();
        if (!title.equalsIgnoreCase("")) {
            filter.addFilter(new FilterDataModel().setPropertyName("Title").setStringValue(title).setSearchType(EnumSearchType.Contains).setClauseType(EnumClauseType.Or)
                    .addInnerFilter(new FilterDataModel().setPropertyName("Description").setStringValue(title).setSearchType(EnumSearchType.Contains).setClauseType(EnumClauseType.Or)));
        }
        if (selectedLocation != null) {
            filter.addFilter(new FilterDataModel().setPropertyName("LinkLocationId").setIntValue(Long.valueOf(selectedLocation.Id)).setSearchType(EnumSearchType.Equal).setClauseType(EnumClauseType.And));
        }
    }

    private View.OnClickListener expandLister(View expandableView, ImageView arrowBtn) {
        return view -> {
            if (expandableView.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(findViewById(R.id.nestedScrool));
                expandableView.setVisibility(View.VISIBLE);
                arrowBtn.setRotation(0);
            } else if (expandableView.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(findViewById(R.id.nestedScrool));
                expandableView.setVisibility(View.GONE);
                arrowBtn.setRotation(180);
            }
        };
    }

    private void getEstateType() {
        ServiceExecute.execute(new EstatePropertyTypeUsageService(this).getAll(new FilterModel().setRowPerPage(100)))
                .subscribe(new NtkObserver<>() {
                    @Override
                    public void onNext(@NonNull ErrorException<EstatePropertyTypeUsageModel> response) {
                        typeUsages = response.ListItems;
                        EstatePropertyTypeAdapterSelector adapter = new EstatePropertyTypeAdapterSelector(typeUsages, null,
                                estatePropertyTypeUsageModel -> {
                                    PropertyTypeUsage = estatePropertyTypeUsageModel;
                                    setTypeUsage(estatePropertyTypeUsageModel);
                                });
                        RecyclerView rc = findViewById(R.id.propertyTypeRv);
                        rc.setAdapter(adapter);
                        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
                        flowLayoutManager.setAutoMeasureEnabled(true);
                        flowLayoutManager.setAlignment(Alignment.RIGHT);
                        rc.setLayoutManager(flowLayoutManager);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
        ServiceExecute.execute(new EstatePropertyTypeService(this).getAll(new FilterModel().setRowPerPage(100))).subscribe(new NtkObserver<ErrorException<EstatePropertyTypeModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyTypeModel> response) {
                propertyType = response.ListItems;
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
        ServiceExecute.execute(new EstatePropertyTypeLanduseService(this).getAll(new FilterModel().setRowPerPage(100)))
                .subscribe(new NtkObserver<ErrorException<EstatePropertyTypeLanduseModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<EstatePropertyTypeLanduseModel> response) {
                        landUses = response.ListItems;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void setTypeUsage(EstatePropertyTypeUsageModel estatePropertyTypeUsageModel) {
        //show cards
        if (findViewById(R.id.TypeUsageCardView).getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(findViewById(R.id.nestedScrool));
            findViewById(R.id.TypeUsageCardView).setVisibility(View.VISIBLE);
        }
        List<EstatePropertyTypeModel> mappers = StreamSupport.stream(propertyType)
                .filter(t -> t.LinkPropertyTypeUsageId.equals(estatePropertyTypeUsageModel.Id))
                .collect(Collectors.toList());
        List<EstatePropertyTypeLanduseModel> models = StreamSupport.stream(landUses).
                filter(t -> StreamSupport.stream(mappers)
                        .anyMatch(k -> k.LinkPropertyTypeLanduseId.equals(t.Id)))
                .collect(Collectors.toList());
        RecyclerView rc = findViewById(R.id.TypeUsageRc);

        rc.setAdapter(new EstatePropertyLandUseAdapterSelector(models, null,
                t -> {
                    PropertyTypeLanduse = t;
                    getAllDetails(t);
                }));
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        flowLayoutManager.maxItemsPerLine(4);
        flowLayoutManager.setAlignment(Alignment.RIGHT);
        rc.setLayoutManager(flowLayoutManager);
    }

    private void getAllDetails(EstatePropertyTypeLanduseModel t) {
        FilterModel f = new FilterModel().addFilter(new FilterDataModel().setPropertyName("LinkPropertyTypeLanduseId")
                .setStringValue(t.Id));
        ServiceExecute.execute(new EstatePropertyDetailGroupService(this).getAll(f
        )).subscribe(new NtkObserver<ErrorException<EstatePropertyDetailGroupModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyDetailGroupModel> response) {
                //create list of values base on details
                StreamSupport.stream(response.ListItems).
                        forEach(estatePropertyDetailGroupModel -> {
                            estatePropertyDetailGroupModel.PropertyValues = new ArrayList<>();
                            StreamSupport.stream(estatePropertyDetailGroupModel.PropertyDetails)
                                    .forEach(estatePropertyDetailModel -> {
                                        EstatePropertyDetailValueModel value = new EstatePropertyDetailValueModel();
                                        value.LinkPropertyDetailId = estatePropertyDetailModel.Id;
                                        value.PropertyDetail = estatePropertyDetailModel;
                                        estatePropertyDetailGroupModel.PropertyValues.add(value);
                                    });
                        });
                SearchPropertyDetailGroupAdapterSelector adapter = new SearchPropertyDetailGroupAdapterSelector(response.ListItems, findViewById(R.id.nestedScrool), getSupportFragmentManager());
                RecyclerView rc = (findViewById(R.id.detailRc));
                rc.setAdapter(adapter);
                rc.setLayoutManager(new LinearLayoutManager(SearchEstateActivity.this, RecyclerView.VERTICAL, false));

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private void getLocations() {

    }

    private void getContractTypes() {
        ServiceExecute.execute(new EstateContractTypeService(this).getAll(new FilterModel())).subscribe(new NtkObserver<ErrorException<EstateContractTypeModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstateContractTypeModel> model) {
                EstateContractAdapterSelector adapter = new EstateContractAdapterSelector(model.ListItems, SearchEstateActivity.this::changeView);
                RecyclerView rc = findViewById(R.id.contractsRc);
                rc.setAdapter(adapter);
                FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
                flowLayoutManager.setAutoMeasureEnabled(true);
                flowLayoutManager.setAlignment(Alignment.RIGHT);
                rc.setLayoutManager(flowLayoutManager);
            }


            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private void changeView(EstateContractTypeModel model) {
        clearAllInput();
        ContractModel = model;
        if (model.HasSalePrice || model.HasRentPrice || model.HasDepositPrice) {
            if (findViewById(R.id.contractDetailCardView).getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(findViewById(R.id.nestedScrool));
                findViewById(R.id.contractDetailCardView).setVisibility(View.VISIBLE);
            }
        } else {
            //hide details

            if (findViewById(R.id.contractDetailCardView).getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(findViewById(R.id.nestedScrool));
                findViewById(R.id.contractDetailCardView).setVisibility(View.GONE);
            }
        }
        TextInputLayout et1 = findViewById(R.id.etl1);
        String preTitle = "محدوده ی مبلغ برای ";
        et1.setHint(preTitle + model.TitleRentPrice);
        et1.setVisibility(model.HasRentPrice ? View.VISIBLE : View.GONE);
        ( findViewById(R.id.et1)).setFocusable(false);
        findViewById(R.id.checkbox_row1).setVisibility(model.RentPriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_row1).findViewById(R.id.cbText)).setText("قیمت توافقی");
        TextInputLayout et2 = findViewById(R.id.etl2);
        et2.setVisibility(model.HasSalePrice ? View.VISIBLE : View.GONE);
        et2.setHint(preTitle + model.TitleSalePrice);
        ( findViewById(R.id.et2)).setFocusable(false);
        findViewById(R.id.checkbox_row2).setVisibility(model.SalePriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_row2).findViewById(R.id.cbText)).setText("قیمت توافقی");
        TextInputLayout et3 = findViewById(R.id.etl3);
        et3.setVisibility(model.HasDepositPrice ? View.VISIBLE : View.GONE);
        et3.setHint(preTitle + model.TitleDepositPrice);
        ( findViewById(R.id.et3)).setFocusable(false);
        findViewById(R.id.checkbox_row3).setVisibility(model.DepositPriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_row3).findViewById(R.id.cbText)).setText("قیمت توافقی");

    }

    private void clearAllInput() {
        ((MaterialCheckBox) findViewById(R.id.checkbox_row1).findViewById(R.id.cb)).setChecked(false);
        TextInputEditText et1 = (TextInputEditText) findViewById(R.id.et1);
        et1.setText("");
        et1.clearFocus();
        ((MaterialCheckBox) findViewById(R.id.checkbox_row2).findViewById(R.id.cb)).setChecked(false);
        TextInputEditText et2 = (TextInputEditText) findViewById(R.id.et2);
        et2.setText("");
        et2.clearFocus();
        ((MaterialCheckBox) findViewById(R.id.checkbox_row3).findViewById(R.id.cb)).setChecked(false);
        TextInputEditText et3 = (TextInputEditText) findViewById(R.id.et3);
        et3.setText("");
        et3.clearFocus();
    }
}
