package ntk.android.estate.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import java9.util.stream.IntStream;
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
import ntk.android.base.services.core.CoreLocationService;
import ntk.android.base.services.estate.EstateContractTypeService;
import ntk.android.base.services.estate.EstatePropertyDetailGroupService;
import ntk.android.base.services.estate.EstatePropertyTypeLanduseService;
import ntk.android.base.services.estate.EstatePropertyTypeService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstateContractAdapterSelector;
import ntk.android.estate.fragment.NewEstateFragment4;

public class SearchEstateActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate_search);
        findViewById(R.id.imgToolbarBack).setOnClickListener(view -> finish());
        ((TextView) findViewById(R.id.txtToolbarTitle)).setText("جست و جو");
        getLocations();
        getEstateType();
        getContractTypes();
    }

    private void getEstateType() {
        ServiceExecute.execute(new EstatePropertyTypeLanduseService(this).getAll(new FilterModel().setRowPerPage(100)))
                .subscribe(new NtkObserver<ErrorException<EstatePropertyTypeLanduseModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<EstatePropertyTypeLanduseModel> response) {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
        ServiceExecute.execute(new EstatePropertyTypeService(this).getAll(new FilterModel().setRowPerPage(100))).subscribe(new NtkObserver<ErrorException<EstatePropertyTypeModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyTypeModel> response) {
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
        ServiceExecute.execute(new EstatePropertyTypeLanduseService(this).getAll(new FilterModel().setRowPerPage(100)))
                .subscribe(new NtkObserver<ErrorException<EstatePropertyTypeLanduseModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<EstatePropertyTypeLanduseModel> response) {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
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
                for (CoreLocationModel t : model.ListItems)
                    names.add(t.Title);
                SpinnerAdapter<CoreLocationModel> locationAdapter = new SpinnerAdapter<CoreLocationModel>(SearchEstateActivity.this, names);
                spinner.setOnItemClickListener((parent, view, position, id) -> {
                    CoreLocationModel selectedModel = model.ListItems.get(position);
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
                EstateContractAdapterSelector adapter = new EstateContractAdapterSelector(model.ListItems, NewEstateFragment4.this::changeView);
                RecyclerView rc = findViewById(R.id.contractsRc);
                rc.setAdapter(adapter);
                rc.setLayoutManager(new GridLayoutManager(SearchEstateActivity.this, 3));
            }


            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }
}
