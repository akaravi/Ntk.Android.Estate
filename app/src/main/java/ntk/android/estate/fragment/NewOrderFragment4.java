package ntk.android.estate.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.Random;

import es.dmoral.toasty.Toasty;
import ir.map.sdk_map.MapirStyle;
import ir.map.sdk_map.maps.MapView;
import ntk.android.base.Extras;
import ntk.android.base.entitymodel.enums.EnumManageUserAccessUserTypes;
import ntk.android.base.entitymodel.estate.EstateCustomerOrderModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.utill.FontManager;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.estate.R;
import ntk.android.estate.activity.GetLocationActivity;
import ntk.android.estate.activity.NewCustomerOrderActivity;

public class NewOrderFragment4 extends BaseFragment {

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_order_4);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFont();


        TextInputEditText codeEt = findViewById(R.id.EstateCodeEditText);
        TextInputEditText titleEt = findViewById(R.id.EstateTitleEditText);
        TextInputEditText descEt = findViewById(R.id.EstateDescEditText);
        TextInputEditText addressEt = findViewById(R.id.EstateAddressEditText);
        EstateCustomerOrderModel model = orderActivity().model();
        if (model.CaseCode != null)
            codeEt.setText(model.CaseCode);
        else
            codeEt.setText(new Random().nextInt(900000) + 100000 + "");
        if (model.Title != null)
            titleEt.setText(model.Title);
        if (model.Description != null)
            descEt.setText(model.Description);

        //todo add locaions
//        if (model.LinkLocationIdTitle != null)
//            ((MaterialAutoCompleteTextView) (findViewById(R.id.EstateProvinceAutoComplete))).setText(model.LinkLocationIdTitle);
//        if (model.Address != null)
//            addressEt.setText(model.Address);


        getData();
    }

    private void setFont() {
        Typeface t1 = FontManager.T1_Typeface(getContext());
        //textview
        ((TextView) findViewById(R.id.customHint)).setTypeface(t1);
        ((TextView) findViewById(R.id.hideEstateTv)).setTypeface(t1);
        ((TextView) findViewById(R.id.txt)).setTypeface(t1);
        //button
        ((MaterialButton) findViewById(R.id.getLocationBtn)).setTypeface(t1);
        //input layout
        ((TextInputLayout) findViewById(R.id.EstateCodeTextInput)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.EstateTitleTextInput)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.EstateDescTextInput)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.EstateProvinceTextInput)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.EstateAddressTextInput)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.locationTextInputLayout)).setTypeface(t1);
        //edit text
        ((TextInputEditText) findViewById(R.id.EstateCodeEditText)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.EstateTitleEditText)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.EstateDescEditText)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.EstateAddressEditText)).setTypeface(t1);
        ((MaterialAutoCompleteTextView) (findViewById(R.id.EstateProvinceAutoComplete))).setTypeface(t1);
    }

    private void getData() {
        MaterialAutoCompleteTextView spinner = (findViewById(R.id.EstateProvinceAutoComplete));
        spinner.setOnClickListener(view -> {
            SelectProviceDialog dialog = SelectProviceDialog.START_DIALOG(
                    selectedModel -> {
                        if (selectedModel != null) {
                            ((MaterialAutoCompleteTextView) (findViewById(R.id.EstateProvinceAutoComplete))).setText(selectedModel.Title);
                            orderActivity().model().LinkLocationId = selectedModel.Id;
                            orderActivity().model().LinkLocationIdTitle = selectedModel.Title;
                            if (orderActivity().model().Geolocationlatitude == null) {
                                if (selectedModel.GeoLocationLatitude != null && selectedModel.GeoLocationLongitude != null) {
                                    if (myMap != null)
                                        marker = myMap.addMarker(GetLocationActivity.MakeMarker(getContext(), new LatLng(selectedModel.GeoLocationLatitude, selectedModel.GeoLocationLongitude)));
                                }
                            }
                        }
                    });
            dialog.show(getActivity().getSupportFragmentManager(), "dialog");
        });
    }

    public NewCustomerOrderActivity orderActivity() {
        return ((NewCustomerOrderActivity) getActivity());
    }

    public boolean isValidForm() {
        TextInputEditText codeEt = findViewById(R.id.EstateCodeEditText);
        TextInputEditText titleEt = findViewById(R.id.EstateTitleEditText);
        TextInputEditText descEt = findViewById(R.id.EstateDescEditText);
        TextInputEditText addressEt = findViewById(R.id.EstateAddressEditText);

        orderActivity().model().CaseCode = codeEt.getText().toString().trim();
        if (titleEt.getText().toString().trim().equals("")) {
            Toasty.error(getContext(), "عنوان  را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        } else
            orderActivity().model().Title = titleEt.getText().toString().trim();
        if (descEt.getText().toString().trim().equals("")) {
            Toasty.error(getContext(), "توضیحات  را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        } else
            orderActivity().model().Description = descEt.getText().toString().trim();
        if (orderActivity().model().LinkLocationId == 0) {
            Toasty.error(getContext(), "منظقه مورد نظر  را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;

        }
        if (addressEt.getText().toString().trim().equals("")) {
            Toasty.error(getContext(), " آدرس را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        } else {
            orderActivity().model().Address = addressEt.getText().toString().trim();
            orderActivity().model().ViewConfigHiddenInList = ((MaterialCheckBox) findViewById(R.id.checkBox)).isChecked();
        }
        return true;
    }
}