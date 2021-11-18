package ntk.android.estate.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import java9.util.stream.IntStream;
import java9.util.stream.StreamSupport;
import ntk.android.base.adapter.SpinnerAdapter;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.core.CoreLocationModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
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
        TextInputEditText codeEt = findViewById(R.id.EstateCodeEditText);
        TextInputEditText titleEt = findViewById(R.id.EstateTitleEditText);
        TextInputEditText descEt = findViewById(R.id.EstateDescEditText);
        TextInputEditText addressEt = findViewById(R.id.EstateAddressEditText);
        EstatePropertyModel model = estateActivity().model();
        if (model.CaseCode != null)
            codeEt.setText(model.CaseCode);
        if (model.Title != null)
            titleEt.setText(model.Title);
        if (model.Description != null)
            descEt.setText(model.Description);
        if (model.Address != null)
            addressEt.setText(model.Address);
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
                    estateActivity().model().LinkLocationId = selectedModel.Id;
                });
                spinner.setAdapter(locationAdapter);
                // Do something for lollipop and above versions
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    //if departments is 0
                    if (estateActivity().model().LinkLocationId == 0)
                        spinner.setText(locationAdapter.getItem(0), false);
                    else {
                        int index = IntStream.range(0, model.ListItems.size()).filter(value -> model.ListItems.get(value).Id.equals(estateActivity().model().LinkLocationId)).findFirst().getAsInt();
                        spinner.setText(locationAdapter.getItem(index), false);

                    }
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
        TextInputEditText codeEt = findViewById(R.id.EstateCodeEditText);
        TextInputEditText titleEt = findViewById(R.id.EstateTitleEditText);
        TextInputEditText descEt = findViewById(R.id.EstateDescEditText);
        TextInputEditText addressEt = findViewById(R.id.EstateAddressEditText);
        if (codeEt.getText().toString().trim().equals("")) {
            Toasty.error(getContext(), "کد ملک را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        } else
            estateActivity().model().CaseCode = codeEt.getText().toString().trim();
        if (titleEt.getText().toString().trim().equals("")) {
            Toasty.error(getContext(), "عنوان  را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        } else
            estateActivity().model().Title = titleEt.getText().toString().trim();
        if (descEt.getText().toString().trim().equals("")) {
            Toasty.error(getContext(), "توضیحات  را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        } else
            estateActivity().model().Description = descEt.getText().toString().trim();
        if (addressEt.getText().toString().trim().equals("")) {
            Toasty.error(getContext(), " آدرس را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        } else
            estateActivity().model().Address = addressEt.getText().toString().trim();

        return true;
    }
}
