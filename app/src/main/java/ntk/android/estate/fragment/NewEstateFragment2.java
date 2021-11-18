package ntk.android.estate.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import java9.util.stream.Collectors;
import java9.util.stream.StreamSupport;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeUsageModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.estate.EstatePropertyTypeLanduseService;
import ntk.android.base.services.estate.EstatePropertyTypeService;
import ntk.android.base.services.estate.EstatePropertyTypeUsageService;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;
import ntk.android.estate.adapter.EstatePropertyLandUseAdapterSelector;
import ntk.android.estate.adapter.EstatePropertyTypeAdapterSelector;

public class NewEstateFragment2 extends BaseFragment {

    private int count;
    private List<EstatePropertyTypeUsageModel> typeUsages;
    private List<EstatePropertyTypeModel> contractTypes;
    private List<EstatePropertyTypeLanduseModel> landUses;

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        count = 0;
        estateActivity().showProgress();
        getContractType();
        getTypeUsage();
        getTypeLandUse();
    }

    private void getTypeLandUse() {
        ServiceExecute.execute(new EstatePropertyTypeLanduseService(getContext()).getAll(new FilterModel().setRowPerPage(100)))
                .subscribe(new NtkObserver<ErrorException<EstatePropertyTypeLanduseModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<EstatePropertyTypeLanduseModel> response) {
                        landUses = response.ListItems;
                        showData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void getContractType() {
        ServiceExecute.execute(new EstatePropertyTypeService(getContext()).getAll(new FilterModel().setRowPerPage(100))).subscribe(new NtkObserver<ErrorException<EstatePropertyTypeModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyTypeModel> response) {
                contractTypes = response.ListItems;
                showData();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                estateActivity().showErrorView();
            }
        });
    }

    private void getTypeUsage() {

        ServiceExecute.execute(new EstatePropertyTypeUsageService(getContext()).getAll(new FilterModel().setRowPerPage(100)))
                .subscribe(new NtkObserver<>() {
                    @Override
                    public void onNext(@NonNull ErrorException<EstatePropertyTypeUsageModel> response) {
                        typeUsages = response.ListItems;
                        showData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        estateActivity().showErrorView();
                    }
                });
    }


    private synchronized void showData() {
        if (count == 2) {
            EstatePropertyTypeAdapterSelector adapter = new EstatePropertyTypeAdapterSelector(typeUsages,estateActivity().model().PropertyTypeUsage, estatePropertyTypeUsageModel -> setTypeUsage(estatePropertyTypeUsageModel));
            RecyclerView rc = findViewById(R.id.estateTypeRc);
            rc.setAdapter(adapter);
            rc.setLayoutManager(new GridLayoutManager(getContext(), 4));
            if(estateActivity().model().PropertyTypeUsage!=null){
                setTypeUsage(estateActivity().model().PropertyTypeUsage);
            }
            estateActivity().showContent();
        } else ++count;
    }

    private void setTypeUsage(EstatePropertyTypeUsageModel estatePropertyTypeUsageModel) {

        List<EstatePropertyTypeModel> mappers = StreamSupport.stream(contractTypes)
                .filter(t -> t.LinkPropertyTypeUsageId.equals(estatePropertyTypeUsageModel.Id))
                .collect(Collectors.toList());
        List<EstatePropertyTypeLanduseModel> models = StreamSupport.stream(landUses).
                filter(t -> StreamSupport.stream(mappers)
                        .anyMatch(k -> k.LinkPropertyTypeLanduseId.equals(t.Id)))
                .collect(Collectors.toList());
        RecyclerView rc = findViewById(R.id.EstateLandUsedRc);
        rc.setAdapter(new EstatePropertyLandUseAdapterSelector(models, t -> {
            estateActivity().model().PropertyTypeLanduse = t;
            changeUi();
        }));
        rc.setLayoutManager(new GridLayoutManager(getContext(), 3));


    }

    private void changeUi() {

    }

    public boolean isValidForm() {

        if (estateActivity().model().PropertyTypeUsage==null)
        {
            Toasty.error(getContext(), "نوع کاربری را انتخاب نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        }
        return true;
    }

    private NewEstateActivity estateActivity() {
        return ((NewEstateActivity) getActivity());
    }


}