package ntk.android.estate.activity;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import java9.util.stream.Collectors;
import java9.util.stream.StreamSupport;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.adapter.SpinnerAdapter;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.core.CoreLocationModel;
import ntk.android.base.entitymodel.estate.EstateContractTypeModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailValueModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeUsageModel;
import ntk.android.base.services.core.CoreLocationService;
import ntk.android.base.services.estate.EstateContractTypeService;
import ntk.android.base.services.estate.EstatePropertyDetailGroupService;
import ntk.android.base.services.estate.EstatePropertyTypeLanduseService;
import ntk.android.base.services.estate.EstatePropertyTypeService;
import ntk.android.base.services.estate.EstatePropertyTypeUsageService;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstateContractAdapterSelector;
import ntk.android.estate.adapter.EstatePropertyDetailGroupAdapterSelector;
import ntk.android.estate.adapter.EstatePropertyLandUseAdapterSelector;
import ntk.android.estate.adapter.EstatePropertyTypeAdapterSelector;
import ntk.android.estate.adapter.SearchPropertyDetailGroupAdapterSelector;

public class SearchEstateActivity extends BaseActivity {
    int totalReq = 1;
    private List<EstatePropertyTypeUsageModel> typeUsages;
    private List<EstatePropertyTypeModel> propertyType;
    private List<EstatePropertyTypeLanduseModel> landUses;

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
        ((TextView) findViewById(R.id.propertyTypeExpandTv)).setTypeface(t1);
        ((TextView) findViewById(R.id.typeUsageExpandTv)).setTypeface(t1);
        ((TextView) findViewById(R.id.contractTypeExpandTv)).setTypeface(t1);
        ((TextView) findViewById(R.id.areaExpandTv)).setTypeface(t1);
        //add expand listener
        findViewById(R.id.titleExpander).setOnClickListener(expandLister(findViewById(R.id.EstateTitleTextInput), findViewById(R.id.titleExpandIcon)));
        findViewById(R.id.locationExpander).setOnClickListener(expandLister(findViewById(R.id.EstateProvinceTextInput), findViewById(R.id.locationExpandIcon)));
        findViewById(R.id.propertyTypeExpander).setOnClickListener(expandLister(findViewById(R.id.propertyTypeRv), findViewById(R.id.propertyTypeExpandIcon)));
        findViewById(R.id.contractTypeExpander).setOnClickListener(expandLister(findViewById(R.id.contractsRc), findViewById(R.id.contractTypeExpandIcon)));
        findViewById(R.id.typeUsageExpander).setOnClickListener(expandLister(findViewById(R.id.TypeUsageRc), findViewById(R.id.typeUsageExpandIcon)));
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
//                  todo show          findViewById(R.id.cardLandUsesView).setVisibility(View.VISIBLE);
                                    //todo set value .PropertyTypeUsage = estatePropertyTypeUsageModel;
                                    setTypeUsage(estatePropertyTypeUsageModel);
                                });
                        RecyclerView rc = findViewById(R.id.propertyTypeRv);
                        rc.setAdapter(adapter);
                        rc.setLayoutManager(new GridLayoutManager(SearchEstateActivity.this, 4));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
        ServiceExecute.execute(new EstatePropertyTypeService(this).getAll(new FilterModel().setRowPerPage(100))).subscribe(new NtkObserver<ErrorException<EstatePropertyTypeModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyTypeModel> response) {
                propertyType=response.ListItems;
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
        ServiceExecute.execute(new EstatePropertyTypeLanduseService(this).getAll(new FilterModel().setRowPerPage(100)))
                .subscribe(new NtkObserver<ErrorException<EstatePropertyTypeLanduseModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<EstatePropertyTypeLanduseModel> response) {
                        landUses=response.ListItems;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void setTypeUsage(EstatePropertyTypeUsageModel estatePropertyTypeUsageModel) {
        if (findViewById(R.id.TypeUsageCardView).getVisibility()==View.GONE) {
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
             //todo set value .PropertyTypeLanduse = t;
                    getAllDetails(t);
                }));
        rc.setLayoutManager(new GridLayoutManager(this, 3));
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
                SearchPropertyDetailGroupAdapterSelector adapter = new SearchPropertyDetailGroupAdapterSelector(response.ListItems,findViewById(R.id.nestedScrool));
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
        ServiceExecute.execute(new CoreLocationService(this).getAllProvinces(new FilterModel())).subscribe(new NtkObserver<ErrorException<CoreLocationModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<CoreLocationModel> model) {

                MaterialAutoCompleteTextView spinner = (findViewById(R.id.EstateProvinceAutoComplete));
                List<String> names = new ArrayList<>();
                names.add("انتخاب نشده");
                for (CoreLocationModel t : model.ListItems)
                    names.add(t.Title);
                SpinnerAdapter<CoreLocationModel> locationAdapter = new SpinnerAdapter<CoreLocationModel>(SearchEstateActivity.this, names);
                spinner.setOnItemClickListener((parent, view, position, id) -> {
                    CoreLocationModel selectedModel;
                    if (position == 0)
                        selectedModel = null;
                    else
                        selectedModel = model.ListItems.get(position - 1);
                });
                spinner.setAdapter(locationAdapter);
                // Do something for lollipop and above versions
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    spinner.setText(locationAdapter.getItem(0), false);
                }
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
                EstateContractAdapterSelector adapter = new EstateContractAdapterSelector(model.ListItems, SearchEstateActivity.this::changeView);
                RecyclerView rc = findViewById(R.id.contractsRc);
                rc.setAdapter(adapter);
                rc.setLayoutManager(new GridLayoutManager(SearchEstateActivity.this, 3));
            }


            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private void changeView(EstateContractTypeModel estateContractTypeModel) {

    }
}
