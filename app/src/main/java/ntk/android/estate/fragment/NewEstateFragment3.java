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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.neshan.common.model.LatLng;
import org.neshan.mapsdk.MapView;
import org.neshan.mapsdk.model.Marker;

import es.dmoral.toasty.Toasty;
import ntk.android.base.Extras;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.GetLocationActivity;
import ntk.android.estate.activity.NewEstateActivity;
import ntk.android.estate.view.component.LocaionAutoCompleteTextView;


public class NewEstateFragment3 extends BaseFragment {
    Marker marker;

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
        if (model.Title != null)
            titleEt.setText(model.Title);
        if (model.Description != null)
            descEt.setText(model.Description);
        if (model.LinkLocationIdTitle!=null)
            ((MaterialAutoCompleteTextView) (findViewById(R.id.EstateProvinceAutoComplete))).setText(model.LinkLocationIdTitle);
        if (model.Address != null)
            addressEt.setText(model.Address);
        if (model.Geolocationlatitude != null) {
            MapView map = findViewById(R.id.mapView);
            LatLng loc = new LatLng(model.Geolocationlatitude, model.Geolocationlongitude);
            marker = GetLocationActivity.MakeMarker(getContext(), loc);
            map.addMarker(marker);
            map.moveCamera(loc, 5);
        }
        //set custom color for custom hint Title
        TextView hint = (TextView) findViewById(R.id.customHint);
        hint.setTextColor(codeEt.getHintTextColors());
        //get location listener
        findViewById(R.id.getLocationBtn).setOnClickListener(view1 -> getLocation());
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
                    MapView map = findViewById(R.id.mapView);
                    //remove previous marker
                    if (marker != null)
                        map.removeMarker(marker);
                    marker = GetLocationActivity.MakeMarker(getContext(), latLng);
                    map.addMarker(marker);
                    map.moveCamera(latLng, 3);
                }
            }
        });
    }

    private void setFont() {
        Typeface t1 = FontManager.T1_Typeface(getContext());
        //textview
        ((TextView) findViewById(R.id.customHint)).setTypeface(t1);
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
        LocaionAutoCompleteTextView locaionAutoCompleteTextView = new LocaionAutoCompleteTextView();
        locaionAutoCompleteTextView.addOnAutoCompleteTextViewTextChangedObserver(spinner);
        locaionAutoCompleteTextView.addOnAutoCompleteTextViewItemClickedSubscriber(spinner,
                coreLocationModel -> {
                    estateActivity().model().LinkLocationId = coreLocationModel.Id;
                    estateActivity().model().LinkLocationIdTitle=coreLocationModel.Title;
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
        if (estateActivity().model().LinkLocationId == 0) {
            Toasty.error(getContext(), "منظقه مورد نظر  را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;

        }if (addressEt.getText().toString().trim().equals("")) {
            Toasty.error(getContext(), " آدرس را وارد نمایید", Toasty.LENGTH_LONG, true).show();
            return false;
        } else
            estateActivity().model().Address = addressEt.getText().toString().trim();

        return true;
    }
}
