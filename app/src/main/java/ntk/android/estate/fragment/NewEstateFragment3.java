package ntk.android.estate.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
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
        getAllDetails(estateActivity().model().PropertyTypeLanduse);
    }

    //https://apicms.ir/api/v1/EstatePropertyDetailGroup/getAll [
    //  {
    //    "Filters": [],
    //    "PropertyName": "LinkPropertyTypeLanduseId",
    //    "Value": "e334e039-504a-4be2-2e7c-08d92262c427"
    //  }
    //]
    private void getAllDetails(EstatePropertyTypeLanduseModel t) {
        FilterModel f = new FilterModel().addFilter(new FilterDataModel().setPropertyName("LinkPropertyTypeLanduseId")
                .setStringValue(t.Id));
        ServiceExecute.execute(new EstatePropertyDetailGroupService(getContext()).getAll(f
        )).subscribe(new NtkObserver<ErrorException<EstatePropertyDetailGroupModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyDetailGroupModel> response) {
                EstatePropertyDetailGroupAdapterSelector adapter = new EstatePropertyDetailGroupAdapterSelector(response.ListItems);
                RecyclerView rc = (findViewById(R.id.estateDetailGroupRc));
                rc.setAdapter(adapter);
                rc.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                estateActivity().showErrorView();
            }
        });
    }

    public boolean isValidForm() {
        return true;
    }

    private NewEstateActivity estateActivity() {
        return ((NewEstateActivity) getActivity());
    }
}
