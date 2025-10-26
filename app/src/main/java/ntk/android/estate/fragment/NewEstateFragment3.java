package ntk.android.estate.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

es.dmoral.toasty.Toasty;
import ntk.android.base.Extras;
import ntk.android.base.entitymodel.enums.EnumManageUserAccessUserTypes;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.utill.FontManager;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.estate.R;
import ntk.android.estate.activity.GetLocationActivity;
import ntk.android.estate.activity.NewEstateActivity;


public class NewEstateFragment3 extends BaseFragment {
    Marker marker;
    GoogleMap myMap;

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_3);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFont();


        TextInputEditText codeEt = findViewById(R.id.EstateCodeEditText);
        TextInputEditText titleEt = findViewById(R.id.EstateTitleEditText);
        TextInputEditText descEt = findViewById(R.id.EstateDescEditText);
        TextInputEditText addressEt = findViewById(R.id.EstateAddressEditText);
        EstatePropertyModel model = estateActivity().model();
        if (model.CaseCode != null)
            codeEt.setText(model.CaseCode);
        else
            codeEt.setText(new Random().nextInt(900000) + 100000 + "");
        if (model.Title != null)
            titleEt.setText(model.Title);
        if (model.Description != null)
            descEt.setText(model.Description);
        if (EnumManageUserAccessUserTypes.isAdmin(Preferences.with(getContext()).UserInfo().getTokenInfo().UserAccessUserType)) {
            findViewById(R.id.hideEstateCardView).setVisibility(View.VISIBLE);
            findViewById(R.id.hideEstatePadding).setVisibility(View.VISIBLE);
            if (model.ViewConfigHiddenInList)
                ((MaterialCheckBox) findViewById(R.id.checkBox)).setChecked(true);
        }
        if (model.LinkLocationIdTitle != null)
            ((MaterialAutoCompleteTextView) (findViewById(R.id.EstateProvinceAutoComplete))).setText(model.LinkLocationIdTitle);
        if (model.Address != null)
            addressEt.setText(model.Address);

        MapView mapView = findViewById(R.id.map_view);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                myMap = googleMap;
                myMap.setMinZoomPreference(12);
                myMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(35.689198, 51.388973)));
                if (model.Geolocationlatitude != null) {
                    LatLng loc = new LatLng(model.Geolocationlatitude, model.Geolocationlongitude);
                    marker = myMap.addMarker(GetLocationActivity.MakeMarker(getContext(), loc));
                    myMap.animateCamera(CameraUpdateFactory.newLatLng(loc));
                }
            }
        });
        //set custom color for custom hint Title
        TextView hint = findViewById(R.id.customHint);
        hint.setTextColor(codeEt.getHintTextColors());
        //get location listener
        findViewById(R.id.getLocationBtn).setOnClickListener(view1 -> getLocation());
        //set hide estate listener
        TextView title = (TextView) findViewById(R.id.txt);
        title.setText("این ملک به صورت مخفی ثبت شود و به صورت عمومی نمایش داده نشود");
        title.setOnClickListener(v -> ((MaterialCheckBox) findViewById(R.id.checkBox)).toggle());
        getData();
    }

    private void getLocation() {
        GetLocationActivity.REGISTER_FOR_RESULT(estateActivity(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null && result.getData().getExtras() != null) {
                    estateActivity().model().Geolocationlatitude = result.getData().getExtras().getDouble(Extras.EXTRA_FIRST_ARG);
                    estateActivity().model().Geolocationlongitude = result.getData().getExtras().getDouble(Extras.EXTRA_SECOND_ARG);
                    LatLng latLng = new LatLng(estateActivity().model().Geolocationlatitude, estateActivity().model().Geolocationlongitude);

                    //remove previous marker
                    if (marker != null)
                        marker.remove();
                    if (myMap != null) {
                        marker = myMap.addMarker(GetLocationActivity.MakeMarker(getContext(), latLng));
                        myMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    }
                }
            }
        });
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
        spinner.setOnClickListener(view -> showLocationDialog());
    }

    private void showLocationDialog() {
        SelectProviceDialog.START_DIALOG(
                selectedModel -> {
                    if (selectedModel != null) {
                        ((MaterialAutoCompleteTextView) (findViewById(R.id.EstateProvinceAutoComplete))).setText(selectedModel.Title);
                        estateActivity().model().LinkLocationId = selectedModel.Id;
                        estateActivity().model().LinkLocationIdTitle = selectedModel.Title;
                        if (estateActivity().model().Geolocationlatitude == null) {
                            if (selectedModel.GeoLocationLatitude != null && selectedModel.GeoLocationLongitude != null) {
                                if (myMap != null)
                                    marker = myMap.addMarker(GetLocationActivity.MakeMarker(getContext(), new LatLng(selectedModel.GeoLocationLatitude, selectedModel.GeoLocationLongitude)));
                            }
                        }
                    }
                });
        dialog.show(getActivity().getSupportFragmentManager(), "dialog");
    }

    public NewEstateActivity estateActivity() {
        return ((NewEstateActivity) getActivity());
    }

    public boolean isValidForm() {
        TextInputEditText codeEt = findViewById(R.id.EstateCodeEditText);
        TextInputEditText titleEt = findViewById(R.id.EstateTitleEditText);
        TextInputEditText descEt = findViewById(R.id.EstateDescEditText);
        TextInputEditText addressEt = findViewById(R.id.EstateAddressEditText);

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
        if (estateActivity().model().LinkLocationId == 0) {
            Toasty.error(getContext(), "منظقه مورد نظر  را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;

        }
        if (addressEt.getText().toString().trim().equals("")) {
            Toasty.error(getContext(), " آدرس را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        } else {
            estateActivity().model().Address = addressEt.getText().toString().trim();
            estateActivity().model().ViewConfigHiddenInList = ((MaterialCheckBox) findViewById(R.id.checkBox)).isChecked();
        }
        return true;
    }
}
