package ntk.android.estate.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import ntk.android.base.adapter.SpinnerAdapter;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.core.CoreLocationModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.core.CoreLocationService;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;


public class NewEstateFragment1 extends BaseFragment {
    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_1);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        estateActivity().showProgress();
        getData();
    }

    private void getData() {
        ServiceExecute.execute(new CoreLocationService(getContext()).getAllProvinces(new FilterModel())).subscribe(new NtkObserver<ErrorException<CoreLocationModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<CoreLocationModel> model) {
                estateActivity().showContent();
                MaterialAutoCompleteTextView spinner = (findViewById(R.id.EstateProvinceAutoComplete));
                List<String> names = new ArrayList<>();
                for (CoreLocationModel t : model.ListItems)
                    names.add(t.Title);
                SpinnerAdapter<CoreLocationModel> locationAdapter = new SpinnerAdapter<CoreLocationModel>(getContext(), names);
                spinner.setOnItemClickListener((parent, view, position, id) -> {
                    CoreLocationModel selectedModel = model.ListItems.get(position);
                    //todo set id
                });
                spinner.setAdapter(locationAdapter);
                // Do something for lollipop and above versions
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    //if departments is 0
                    spinner.setText(locationAdapter.getItem(0), false);
                    //todo set id
                }
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
