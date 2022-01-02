package ntk.android.estate.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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
import ntk.android.base.appclass.FromToClass;
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
import ntk.android.estate.fragment.FilterValuePickerDialog;
import ntk.android.estate.view.component.LocaionAutoCompleteTextView;

public class SearchEstateActivity extends BaseActivity {
    private List<EstatePropertyTypeUsageModel> typeUsages;
    private List<EstatePropertyTypeModel> propertyType;
    private List<EstatePropertyTypeLanduseModel> landUses;
    CoreLocationModel selectedLocation;
    private EstatePropertyTypeUsageModel selectedPropertyTypeUsage;
    private EstatePropertyTypeLanduseModel selectPropertyTypeLanduse;
    private EstateContractTypeModel selectedContractType;
    private List<EstatePropertyDetailGroupModel> SelectedPropertyDetailGroupModel;
    private String hintContractTitle1;
    private String hintContractTitle2;
    private String hintContractTitle3;
    private String hintCreatedYearTitle;
    private String hintPartitionTitle;

    FromToClass rentFromTo;
    FromToClass saleFromTo;
    FromToClass depositFromTo;
    FromToClass areaFromTo;
    FromToClass createdYearFromTo;
    FromToClass partitionFromTo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate_search);

        initView();
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
        //for textInput layout contract detail
        ((TextInputLayout) findViewById(R.id.etl1)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etl2)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etl3)).setTypeface(t1);
        //for TextInputEditText  contract detail
        ((TextInputEditText) findViewById(R.id.et1)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.et2)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.et3)).setTypeface(t1);
        //for checkView  contract detail
        ((TextView) findViewById(R.id.checkbox_row1).findViewById(R.id.cbText)).setTypeface(t1);
        ((TextView) findViewById(R.id.checkbox_row2).findViewById(R.id.cbText)).setTypeface(t1);
        ((TextView) findViewById(R.id.checkbox_row3).findViewById(R.id.cbText)).setTypeface(t1);

        //for landUSe details
        //for textInput layout landUSe detail
        ((TextInputLayout) findViewById(R.id.EstateAreaTextInput)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.EstatePropertyOneTextInput)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.EstatePropertyTowTextInput)).setTypeface(t1);
        //for TextInputEditText  landUSe detail
        ((TextInputEditText) findViewById(R.id.EstateAreaEditText)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.EstatePropertyOneEditText)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.EstatePropertyTowEditText)).setTypeface(t1);
        //add expand listener
        findViewById(R.id.titleExpander).setOnClickListener(expandLister(findViewById(R.id.EstateTitleTextInput), findViewById(R.id.titleExpandIcon)));
        findViewById(R.id.locationExpander).setOnClickListener(expandLister(findViewById(R.id.EstateProvinceTextInput), findViewById(R.id.locationExpandIcon)));
        findViewById(R.id.contractTypeExpander).setOnClickListener(expandLister(findViewById(R.id.contractsRc), findViewById(R.id.contractTypeExpandIcon)));
        findViewById(R.id.contractDetailExpander).setOnClickListener(expandLister(findViewById(R.id.ContractDetailView), findViewById(R.id.contractDetailExpandIcon)));
        findViewById(R.id.propertyTypeExpander).setOnClickListener(expandLister(findViewById(R.id.propertyTypeRv), findViewById(R.id.propertyTypeExpandIcon)));
        findViewById(R.id.typeUsageExpander).setOnClickListener(expandLister(findViewById(R.id.TypeUsageRc), findViewById(R.id.typeUsageExpandIcon)));
        findViewById(R.id.LandUseExpander).setOnClickListener(expandLister(findViewById(R.id.landUedDetailView), findViewById(R.id.LandUsedExpandIcon)));
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
        contractDetailInit();
        landUedDetailInit();
    }


    private void contractDetailInit() {
        //click on all of view to affect on  toggling checkBox
        findViewById(R.id.checkbox_row1).setOnClickListener(v -> ((CheckBox) v.findViewById(R.id.cb)).toggle());
        findViewById(R.id.checkbox_row2).setOnClickListener(v -> ((CheckBox) v.findViewById(R.id.cb)).toggle());
        findViewById(R.id.checkbox_row3).setOnClickListener(v -> ((CheckBox) v.findViewById(R.id.cb)).toggle());
        //toggle state of Edittext on toggling Checkbox
        ((CheckBox) findViewById(R.id.checkbox_row1).findViewById(R.id.cb)).setOnCheckedChangeListener((compoundButton, b) -> findViewById(R.id.et1).setEnabled(!b));
        ((CheckBox) findViewById(R.id.checkbox_row2).findViewById(R.id.cb)).setOnCheckedChangeListener((compoundButton, b) -> findViewById(R.id.et2).setEnabled(!b));
        ((CheckBox) findViewById(R.id.checkbox_row3).findViewById(R.id.cb)).setOnCheckedChangeListener((compoundButton, b) -> findViewById(R.id.et3).setEnabled(!b));
        findViewById(R.id.et1).setOnClickListener(view -> new FilterValuePickerDialog(SearchEstateActivity.this).setTitle(hintContractTitle1)
                .setLable(findViewById(R.id.et1)).setPreviousValue(rentFromTo).setCallBack(o -> rentFromTo = o).show());
        findViewById(R.id.et2).setOnClickListener(view -> new FilterValuePickerDialog(SearchEstateActivity.this).setTitle(hintContractTitle2)
                .setLable(findViewById(R.id.et2)).setPreviousValue(saleFromTo).setCallBack(o -> saleFromTo = o).show());
        findViewById(R.id.et3).setOnClickListener(view -> new FilterValuePickerDialog(SearchEstateActivity.this).setTitle(hintContractTitle3)
                .setLable(findViewById(R.id.et3)).setPreviousValue(depositFromTo).setCallBack(o -> depositFromTo = o).show());
    }

    private void landUedDetailInit() {
        findViewById(R.id.EstateAreaEditText).setOnClickListener(view -> new FilterValuePickerDialog(SearchEstateActivity.this).setTitle("مساحت مورد نظر (متر)")
                .setLable(findViewById(R.id.EstateAreaEditText)).setPreviousValue(areaFromTo).setCallBack(o -> areaFromTo = o).show());
        findViewById(R.id.EstatePropertyOneEditText).setOnClickListener(view -> new FilterValuePickerDialog(SearchEstateActivity.this).setTitle(hintCreatedYearTitle)
                .setLable(findViewById(R.id.EstatePropertyOneEditText)).setPreviousValue(createdYearFromTo).setCallBack(o -> createdYearFromTo = o).show());
        findViewById(R.id.EstatePropertyTowEditText).setOnClickListener(view -> new FilterValuePickerDialog(SearchEstateActivity.this).setTitle(hintPartitionTitle)
                .setLable(findViewById(R.id.EstatePropertyTowEditText)).setPreviousValue(partitionFromTo).setCallBack(o -> partitionFromTo = o).show());

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
        if (selectPropertyTypeLanduse != null)
            filter.addFilter(new FilterDataModel().setPropertyName("LinkPropertyTypeLanduseId").setStringValue(selectPropertyTypeLanduse.Id).setSearchType(EnumSearchType.Equal).setClauseType(EnumClauseType.And));
        if (selectedPropertyTypeUsage != null)
            filter.addFilter(new FilterDataModel().setPropertyName("LinkPropertyTypeUsageId").setStringValue(selectedPropertyTypeUsage.Id).setSearchType(EnumSearchType.Equal).setClauseType(EnumClauseType.And));
        //for contract
        if (selectedContractType != null) {
            filter.addFilter(new FilterDataModel().setPropertyName("Contracts").setPropertyAnyName("Id").setStringValue(selectedContractType.Id).setSearchType(EnumSearchType.Equal).setClauseType(EnumClauseType.And));
            CheckBox priceCB = (CheckBox) findViewById(R.id.checkbox_row1).findViewById(R.id.cb);
            CheckBox rentCB = (CheckBox) findViewById(R.id.checkbox_row2).findViewById(R.id.cb);
            CheckBox depositCB = (CheckBox) findViewById(R.id.checkbox_row3).findViewById(R.id.cb);
            if (!rentCB.isChecked()) {
                if (selectedContractType.HasRentPrice)
                    //for contract Type1
                    if (rentFromTo != null) {
                        if (rentFromTo.getFrom() != null)
                            filter.addFilter(new FilterDataModel().setPropertyName("Contracts").setPropertyAnyName("RentPrice").setIntValue(((Long) rentFromTo.getFrom())).setSearchType(EnumSearchType.GreaterThan).setClauseType(EnumClauseType.And));
                        if (rentFromTo.getTo() != null)
                            filter.addFilter(new FilterDataModel().setPropertyName("Contracts").setPropertyAnyName("RentPrice").setIntValue(((Long) rentFromTo.getTo())).setSearchType(EnumSearchType.LessThan).setClauseType(EnumClauseType.And));

                    }
            }else {
                filter.addFilter(new FilterDataModel().setPropertyName("Contracts").setPropertyAnyName("RentPriceByAgreement").setBooleanValue(true).setSearchType(EnumSearchType.Equal).setClauseType(EnumClauseType.And));
            }if (!priceCB.isChecked()) {
                if (selectedContractType.HasSalePrice)
                    //for contract Type2
                    if ((saleFromTo) != null) {
                        if (saleFromTo.getFrom() != null)
                            filter.addFilter(new FilterDataModel().setPropertyName("Contracts").setPropertyAnyName("SalePrice").setIntValue(((Long) saleFromTo.getFrom())).setSearchType(EnumSearchType.GreaterThan).setClauseType(EnumClauseType.And));
                        if (saleFromTo.getTo() != null)
                            filter.addFilter(new FilterDataModel().setPropertyName("Contracts").setPropertyAnyName("SalePrice").setIntValue(((Long) saleFromTo.getTo())).setSearchType(EnumSearchType.LessThan).setClauseType(EnumClauseType.And));

                    }
            }else{
                filter.addFilter(new FilterDataModel().setPropertyName("Contracts").setPropertyAnyName("SalePriceByAgreement").setBooleanValue(true).setSearchType(EnumSearchType.Equal).setClauseType(EnumClauseType.And));

            }
            if (!depositCB.isChecked()) {
                if (selectedContractType.HasDepositPrice) {
                    //for contract Type3
                    if (depositFromTo.getFrom() != null)
                        filter.addFilter(new FilterDataModel().setPropertyName("Contracts").setPropertyAnyName("DepositPrice").setIntValue(((Long) depositFromTo.getFrom())).setSearchType(EnumSearchType.GreaterThan).setClauseType(EnumClauseType.And));
                    if (depositFromTo.getTo() != null)
                        filter.addFilter(new FilterDataModel().setPropertyName("Contracts").setPropertyAnyName("DepositPrice").setIntValue(((Long) depositFromTo.getTo())).setSearchType(EnumSearchType.LessThan).setClauseType(EnumClauseType.And));
                }
            }else{
                filter.addFilter(new FilterDataModel().setPropertyName("Contracts").setPropertyAnyName("DepositPriceByAgreement").setBooleanValue(true).setSearchType(EnumSearchType.Equal).setClauseType(EnumClauseType.And));

            }
            //for area filter
            if (areaFromTo != null) {
                if (areaFromTo.getFrom() != null)
                    filter.addFilter(new FilterDataModel().setPropertyName("Area").setIntValue((Long) areaFromTo.getFrom()).setSearchType(EnumSearchType.GreaterThan).setClauseType(EnumClauseType.And));
                if (areaFromTo.getTo() != null)
                    filter.addFilter(new FilterDataModel().setPropertyName("Area").setIntValue((Long) areaFromTo.getTo()).setSearchType(EnumSearchType.LessThan).setClauseType(EnumClauseType.And));

            }
            //for created year
            if (createdYearFromTo != null) {
                if (createdYearFromTo.getFrom() != null)
                    filter.addFilter(new FilterDataModel().setPropertyName("Area").setIntValue((Long) createdYearFromTo.getFrom()).setSearchType(EnumSearchType.GreaterThan).setClauseType(EnumClauseType.And));
                if (createdYearFromTo.getTo() != null)
                    filter.addFilter(new FilterDataModel().setPropertyName("Area").setIntValue((Long) createdYearFromTo.getTo()).setSearchType(EnumSearchType.LessThan).setClauseType(EnumClauseType.And));
            }
            if (partitionFromTo != null) {
                if (partitionFromTo.getFrom() != null)
                    filter.addFilter(new FilterDataModel().setPropertyName("Area").setIntValue((Long) partitionFromTo.getFrom()).setSearchType(EnumSearchType.GreaterThan).setClauseType(EnumClauseType.And));
                if (partitionFromTo.getTo() != null)
                    filter.addFilter(new FilterDataModel().setPropertyName("Area").setIntValue((Long) partitionFromTo.getTo()).setSearchType(EnumSearchType.LessThan).setClauseType(EnumClauseType.And));
            }
            if (SelectedPropertyDetailGroupModel != null) {
                FilterDataModel details = new FilterDataModel();
                for (int i = 0; i < SelectedPropertyDetailGroupModel.size(); i++) {
                    for (EstatePropertyDetailValueModel t : SelectedPropertyDetailGroupModel.get(i).PropertyValues) {
                        if (t.Value != null && !t.Value.equals("") && !t.Value.equals("false")) {
                            FilterDataModel detailFilterModels = new FilterDataModel();
                            FilterDataModel f1 = new FilterDataModel().setPropertyName("PropertyDetailValues").setPropertyAnyName("Id").setStringValue(t.Id).setClauseType(EnumClauseType.And);
                            FilterDataModel f2 = new FilterDataModel().setPropertyName("PropertyDetailValues").setPropertyAnyName("Value").setStringValue(t.Value).setClauseType(EnumClauseType.And);
                            detailFilterModels.addInnerFilter(f1).addInnerFilter(f2);
                            details.addInnerFilter(detailFilterModels);
                        }
                    }
                }
                filter.addFilter(details);
            }
            EstateListWithFilterActivity.START_NEW(this, filter, "جست و جو پیشرفته");
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
                                    selectedPropertyTypeUsage = estatePropertyTypeUsageModel;
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
            findViewById(R.id.TypeUsageCardView_seprator).setVisibility(View.VISIBLE);
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
                    selectPropertyTypeLanduse = t;
                    getAllDetails(t);
                }));
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        flowLayoutManager.maxItemsPerLine(4);
        flowLayoutManager.setAlignment(Alignment.RIGHT);
        rc.setLayoutManager(flowLayoutManager);
    }

    private void getAllDetails(EstatePropertyTypeLanduseModel t) {
        //change view
        //show cards
        if (findViewById(R.id.landUsedDetailCardView).getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(findViewById(R.id.nestedScrool));
            findViewById(R.id.landUsedDetailCardView).setVisibility(View.VISIBLE);
            findViewById(R.id.landUsedDetailCardView_seprator).setVisibility(View.VISIBLE);
        }
        if (selectPropertyTypeLanduse.TitleCreatedYaer != null && !selectPropertyTypeLanduse.TitleCreatedYaer.equals("") && !selectPropertyTypeLanduse.TitleCreatedYaer.equals("---")) {
            hintCreatedYearTitle = selectPropertyTypeLanduse.TitleCreatedYaer;
            findViewById(R.id.EstatePropertyOneTextInput).setVisibility(View.VISIBLE);
            ((TextInputLayout) findViewById(R.id.EstatePropertyOneTextInput)).setHint(selectPropertyTypeLanduse.TitleCreatedYaer);
        } else findViewById(R.id.EstatePropertyOneTextInput).setVisibility(View.GONE);
        if (selectPropertyTypeLanduse.TitlePartition != null && !selectPropertyTypeLanduse.TitlePartition.equals("") && !selectPropertyTypeLanduse.TitlePartition.equals("---")) {
            hintPartitionTitle = selectPropertyTypeLanduse.TitlePartition;
            findViewById(R.id.EstatePropertyTowTextInput).setVisibility(View.VISIBLE);
            ((TextInputLayout) findViewById(R.id.EstatePropertyTowTextInput)).setHint(selectPropertyTypeLanduse.TitlePartition);
        } else findViewById(R.id.EstatePropertyTowTextInput).setVisibility(View.GONE);
        FilterModel f = new FilterModel().addFilter(new FilterDataModel().setPropertyName("LinkPropertyTypeLanduseId")
                .setStringValue(t.Id));
        ServiceExecute.execute(new EstatePropertyDetailGroupService(this).getAll(f
        )).subscribe(new NtkObserver<ErrorException<EstatePropertyDetailGroupModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyDetailGroupModel> response) {
                //create list of values base on details
                SelectedPropertyDetailGroupModel = response.ListItems;
                StreamSupport.stream(SelectedPropertyDetailGroupModel).
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
                SearchPropertyDetailGroupAdapterSelector adapter = new SearchPropertyDetailGroupAdapterSelector(SelectedPropertyDetailGroupModel, findViewById(R.id.nestedScrool), getSupportFragmentManager());
                RecyclerView rc = (findViewById(R.id.detailRc));
                rc.setAdapter(adapter);
                rc.setLayoutManager(new LinearLayoutManager(SearchEstateActivity.this, RecyclerView.VERTICAL, false));

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }


    private void getContractTypes() {
        ServiceExecute.execute(new EstateContractTypeService(this).getAll(new FilterModel())).subscribe(new NtkObserver<ErrorException<EstateContractTypeModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstateContractTypeModel> model) {
                EstateContractAdapterSelector adapter = new EstateContractAdapterSelector(model.ListItems, SearchEstateActivity.this::ContractTypeSelecting);
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

    private void ContractTypeSelecting(EstateContractTypeModel model) {
        clearAllInput();
        selectedContractType = model;

        TextInputLayout et1 = findViewById(R.id.etl1);
        String preTitle = "محدوده ی مبلغ برای ";
        hintContractTitle1 = preTitle + model.TitleRentPrice;
        et1.setHint(hintContractTitle1);
        et1.setVisibility(model.HasRentPrice ? View.VISIBLE : View.GONE);
        (findViewById(R.id.et1)).setFocusable(false);
        findViewById(R.id.checkbox_row1).setVisibility(model.RentPriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_row1).findViewById(R.id.cbText)).setText("قیمت توافقی");
        TextInputLayout et2 = findViewById(R.id.etl2);
        et2.setVisibility(model.HasSalePrice ? View.VISIBLE : View.GONE);
        hintContractTitle2 = preTitle + model.TitleSalePrice;
        et2.setHint(hintContractTitle2);
        (findViewById(R.id.et2)).setFocusable(false);
        findViewById(R.id.checkbox_row2).setVisibility(model.SalePriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_row2).findViewById(R.id.cbText)).setText("قیمت توافقی");
        TextInputLayout et3 = findViewById(R.id.etl3);
        et3.setVisibility(model.HasDepositPrice ? View.VISIBLE : View.GONE);
        hintContractTitle3 = preTitle + model.TitleDepositPrice;
        et3.setHint(hintContractTitle3);
        (findViewById(R.id.et3)).setFocusable(false);
        findViewById(R.id.checkbox_row3).setVisibility(model.DepositPriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_row3).findViewById(R.id.cbText)).setText("قیمت توافقی");
        if (model.HasSalePrice || model.HasRentPrice || model.HasDepositPrice) {
            if (findViewById(R.id.contractDetailCardView).getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(findViewById(R.id.nestedScrool));
                findViewById(R.id.contractDetailCardView).setVisibility(View.VISIBLE);
                findViewById(R.id.contractDetailCardView_seprator).setVisibility(View.VISIBLE);
            }
        } else {
            //hide details

            if (findViewById(R.id.contractDetailCardView).getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(findViewById(R.id.nestedScrool));
                findViewById(R.id.contractDetailCardView).setVisibility(View.GONE);
                findViewById(R.id.contractDetailCardView_seprator).setVisibility(View.GONE);
            }
        }
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
