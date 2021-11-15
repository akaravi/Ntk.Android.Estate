package ntk.android.estate.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstateContractModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.estate.EstateContractService;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;
import ntk.android.estate.adapter.EstateContractAdapterSelector;

public class NewEstateFragment4 extends BaseFragment {

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_4);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    private void getData() {
        ServiceExecute.execute(new EstateContractService(getContext()).getAll(new FilterModel())).subscribe(new NtkObserver<ErrorException<EstateContractModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstateContractModel> model) {
                estateActivity().showContent();
                EstateContractAdapterSelector adapter = new EstateContractAdapterSelector(model.ListItems, NewEstateFragment4.this::changeView);
                RecyclerView rc = findViewById(R.id.contractsRc);
                rc.setAdapter(adapter);
                rc.setLayoutManager(new GridLayoutManager(getContext(), 2));
            }


            @Override
            public void onError(@NonNull Throwable e) {
                estateActivity().showErrorView();
            }
        });
    }

    private void changeView(EstateContractModel estateContractModel) {

    }

    private NewEstateActivity estateActivity() {
        return ((NewEstateActivity) getActivity());
    }

    public boolean isValidForm() {
        return true;
    }
}