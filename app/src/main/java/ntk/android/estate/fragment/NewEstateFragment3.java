package ntk.android.estate.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import java9.util.stream.StreamSupport;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailValueModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.estate.EstatePropertyDetailGroupService;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;
import ntk.android.estate.adapter.EstatePropertyDetailGroupAdapterSelector;

public class NewEstateFragment3 extends BaseFragment {
    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_3);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (estateActivity().model().PropertyDetailGroups == null || estateActivity().model().PropertyDetailGroups.size() == 0)
            getAllDetails(estateActivity().model().PropertyTypeLanduse);
        else {
            showView();
        }
    }


    private void getAllDetails(EstatePropertyTypeLanduseModel t) {
        FilterModel f = new FilterModel().addFilter(new FilterDataModel().setPropertyName("LinkPropertyTypeLanduseId")
                .setStringValue(t.Id));
        ServiceExecute.execute(new EstatePropertyDetailGroupService(getContext()).getAll(f
        )).subscribe(new NtkObserver<ErrorException<EstatePropertyDetailGroupModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyDetailGroupModel> response) {
                estateActivity().model().PropertyDetailGroups = response.ListItems;
                //create list of values base on details
                StreamSupport.stream(estateActivity().model().PropertyDetailGroups).
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
                showView();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                estateActivity().showErrorView();
            }
        });
    }

    private void showView() {
        EstatePropertyDetailGroupAdapterSelector adapter = new EstatePropertyDetailGroupAdapterSelector(
                estateActivity().getSupportFragmentManager(), estateActivity().model().PropertyDetailGroups);
        RecyclerView rc = (findViewById(R.id.estateDetailGroupRc));
        rc.setAdapter(adapter);
        rc.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    public boolean isValidForm() {
        estateActivity().model().PropertyDetailValues=new ArrayList<>();
        return true;
    }

    private NewEstateActivity estateActivity() {
        return ((NewEstateActivity) getActivity());
    }
}
