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
import ntk.android.estate.activity.NewCustomerOrderActivity;
import ntk.android.estate.adapter.EstatePropertyDetailGroupAdapterSelector;

public class NewOrderFragment2 extends BaseFragment {
    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_order_2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (orderActivity().model().PropertyDetailGroups == null || orderActivity().model().PropertyDetailGroups.size() == 0)
            getAllDetails(orderActivity().model().PropertyTypeLanduse);
        else {
            showView();
        }
    }


    private void getAllDetails(EstatePropertyTypeLanduseModel t) {
        orderActivity().showProgress();
        FilterModel f = new FilterModel().addFilter(new FilterDataModel().setPropertyName("LinkPropertyTypeLanduseId")
                .setStringValue(t.Id));
        ServiceExecute.execute(new EstatePropertyDetailGroupService(getContext()).getAll(f
        )).subscribe(new NtkObserver<ErrorException<EstatePropertyDetailGroupModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyDetailGroupModel> response) {
                orderActivity().model().PropertyDetailGroups = response.ListItems;
                //create list of values base on details
                StreamSupport.stream(orderActivity().model().PropertyDetailGroups).
                        forEach(estatePropertyDetailGroupModel -> {

                            StreamSupport.stream(estatePropertyDetailGroupModel.PropertyDetails)
                                    .forEach(estatePropertyDetailModel -> {
                                        EstatePropertyDetailValueModel estatePropertyDetailValueModel = StreamSupport.stream(orderActivity().model().PropertyDetailValues).filter(valueModel -> valueModel.LinkPropertyDetailId.equals(estatePropertyDetailModel.Id)).findFirst().orElse(null);
                                        estatePropertyDetailModel.Value = (estatePropertyDetailValueModel != null ? estatePropertyDetailValueModel.Value : null);
                                    });
                        });
                orderActivity().showContent();
                showView();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                orderActivity().showErrorView();
            }
        });
    }

    private void showView() {
        EstatePropertyDetailGroupAdapterSelector adapter = new EstatePropertyDetailGroupAdapterSelector(
                orderActivity().getSupportFragmentManager(), orderActivity().model().PropertyDetailGroups);
        RecyclerView rc = (findViewById(R.id.estateDetailGroupRc));
        rc.setAdapter(adapter);
        rc.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    public boolean isValidForm() {
        orderActivity().model().PropertyDetailValues = new ArrayList<>();
        for (EstatePropertyDetailGroupModel group : orderActivity().model().PropertyDetailGroups) {
            StreamSupport.stream(group.PropertyDetails).filter(estatePropertyDetailModel -> estatePropertyDetailModel.Value != null).forEach(estatePropertyDetailModel -> {
                EstatePropertyDetailValueModel value = new EstatePropertyDetailValueModel();
                value.LinkPropertyDetailId = estatePropertyDetailModel.Id;
                value.Value = estatePropertyDetailModel.Value;
                orderActivity().model().PropertyDetailValues.add(value);
            });
        }
        return true;
    }


    NewCustomerOrderActivity orderActivity() {
        return ((NewCustomerOrderActivity) getActivity());
    }
}
