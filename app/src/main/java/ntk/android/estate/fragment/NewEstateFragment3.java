package ntk.android.estate.fragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
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
                RecyclerView rc = (findViewById(R.id.estateDetailGroupValueRc));
                rc.setAdapter(adapter);
                rc.setLayoutManager(new GridLayoutManager(getContext(), 3));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                estateActivity().showErrorView();
            }
        });
    }
    public NewEstateActivity estateActivity() {
        return ((NewEstateActivity) getActivity());
    }

    public boolean isValidForm() {
        return true;
    }
}
