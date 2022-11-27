package ntk.android.estate.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import ntk.android.base.entitymodel.estate.EstateCustomerOrderModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewCustomerOrderActivity;
import ntk.android.estate.adapter.MultiLocationsAdapter;

public class NewOrderFragment4 extends BaseFragment {

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
        if (model.LinkLocationIds == null) {
            model.LinkLocationIds = new ArrayList<>();
            model.LocationTitles = new ArrayList<>();
        }
        RecyclerView rc = findViewById(R.id.multiLocationRc);
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        flowLayoutManager.setAlignment(Alignment.RIGHT);
        rc.setLayoutManager(flowLayoutManager);
        rc.setAdapter(new MultiLocationsAdapter(model.LocationTitles, model.LinkLocationIds,integer -> {
            model.LinkLocationIds.remove(integer);
            model.LocationTitles.remove(integer);
            rc.getAdapter().notifyDataSetChanged();
        }));
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
        findViewById(R.id.addLocationBtn).setOnClickListener(v -> {
            SelectProviceDialog dialog = SelectProviceDialog.START_DIALOG(
                    selectedModel -> {
                        if (selectedModel != null) {
                            if (!orderActivity().model().LinkLocationIds.contains(Long.valueOf(selectedModel.Id))) {
                                orderActivity().model().LinkLocationIds.add(Long.valueOf(selectedModel.Id));
                                orderActivity().model().LocationTitles.add(selectedModel.Title);
                                ((RecyclerView) findViewById(R.id.multiLocationRc)).getAdapter().notifyDataSetChanged();
                            } else {
                                Toasty.error(getContext(), "این موقعیت قبلا انتخاب شده است", Toasty.LENGTH_LONG, true).show();
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