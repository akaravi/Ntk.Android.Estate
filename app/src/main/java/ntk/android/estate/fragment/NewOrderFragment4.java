package ntk.android.estate.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

import es.dmoral.toasty.Toasty;
import ntk.android.base.entitymodel.estate.EstateCustomerOrderModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.GetLocationActivity;
import ntk.android.estate.activity.NewCustomerOrderActivity;

public class NewOrderFragment4 extends BaseFragment {
    List<String> locationTitles;
    List<Long> locationId;

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_order_4);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFont();


        TextInputEditText titleEt = findViewById(R.id.EstateTitleEditText);
        TextInputEditText descEt = findViewById(R.id.EstateDescEditText);
        EstateCustomerOrderModel model = orderActivity().model();

        if (model.Title != null)
            titleEt.setText(model.Title);
        if (model.Description != null)
            descEt.setText(model.Description);


        getData();
    }

    private void setFont() {
        Typeface t1 = FontManager.T1_Typeface(getContext());
        //input layout
        ((TextInputLayout) findViewById(R.id.EstateTitleTextInput)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.EstateDescTextInput)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.locationTextInputLayout)).setTypeface(t1);
        //edit text
        ((TextInputEditText) findViewById(R.id.EstateTitleEditText)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.EstateDescEditText)).setTypeface(t1);
    }

    private void getData() {
//        MaterialAutoCompleteTextView spinner = (findViewById(R.id.EstateProvinceAutoComplete));
//        spinner.setOnClickListener(view -> {
//            SelectProviceDialog dialog = SelectProviceDialog.START_DIALOG(
//                    selectedModel -> {
////                        if (selectedModel != null) {
////                            ((MaterialAutoCompleteTextView) (findViewById(R.id.EstateProvinceAutoComplete))).setText(selectedModel.Title);
////                            orderActivity().model().LinkLocationId = selectedModel.Id;
////                            orderActivity().model().LinkLocationIdTitle = selectedModel.Title;
////                            if (orderActivity().model().Geolocationlatitude == null) {
////                                if (selectedModel.GeoLocationLatitude != null && selectedModel.GeoLocationLongitude != null) {
////                                    if (myMap != null)
////                                        marker = myMap.addMarker(GetLocationActivity.MakeMarker(getContext(), new LatLng(selectedModel.GeoLocationLatitude, selectedModel.GeoLocationLongitude)));
////                                }
////                            }
////                        }
//                    });
//            dialog.show(getActivity().getSupportFragmentManager(), "dialog");
//        });
    }

    public NewCustomerOrderActivity orderActivity() {
        return ((NewCustomerOrderActivity) getActivity());
    }

    public boolean isValidForm() {
        TextInputEditText titleEt = findViewById(R.id.EstateTitleEditText);
        TextInputEditText descEt = findViewById(R.id.EstateDescEditText);

        if (titleEt.getText().toString().trim().equals("")) {
            Toasty.error(getContext(), "عنوان  را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        } else
            orderActivity().model().Title = titleEt.getText().toString().trim();

        return true;
    }
}