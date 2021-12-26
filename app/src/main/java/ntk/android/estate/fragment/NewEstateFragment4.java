package ntk.android.estate.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstateContractModel;
import ntk.android.base.entitymodel.estate.EstateContractTypeModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.estate.EstateContractTypeService;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;
import ntk.android.estate.adapter.EstateContractAdapterSelector;
import ntk.android.estate.adapter.RemovableContractsAdapter;
import ntk.android.estate.view.NumberTextWatcherForThousand;

public class NewEstateFragment4 extends BaseFragment {

    private EstateContractTypeModel selectedModel;

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_4);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //click on all of view to affect on  toggling checkBox
        findViewById(R.id.checkbox_row1).setOnClickListener(v -> ((CheckBox) v.findViewById(R.id.cb)).toggle());
        findViewById(R.id.checkbox_row2).setOnClickListener(v -> ((CheckBox) v.findViewById(R.id.cb)).toggle());
        findViewById(R.id.checkbox_row3).setOnClickListener(v -> ((CheckBox) v.findViewById(R.id.cb)).toggle());
        //toggle state of Edittext on toggling Checkbox
        ((CheckBox) findViewById(R.id.checkbox_row1).findViewById(R.id.cb)).setOnCheckedChangeListener((compoundButton, b) -> findViewById(R.id.et1).setEnabled(!b));
        ((CheckBox) findViewById(R.id.checkbox_row2).findViewById(R.id.cb)).setOnCheckedChangeListener((compoundButton, b) -> findViewById(R.id.et2).setEnabled(!b));
        ((CheckBox) findViewById(R.id.checkbox_row3).findViewById(R.id.cb)).setOnCheckedChangeListener((compoundButton, b) -> findViewById(R.id.et3).setEnabled(!b));
        //add separator to editText
        TextInputEditText et1 = (TextInputEditText) findViewById(R.id.et1);
        et1.addTextChangedListener(new NumberTextWatcherForThousand(et1));
        TextInputEditText et2 = (TextInputEditText) findViewById(R.id.et2);
        et2.addTextChangedListener(new NumberTextWatcherForThousand(et2));
        TextInputEditText et3 = (TextInputEditText) findViewById(R.id.et3);
        et3.addTextChangedListener(new NumberTextWatcherForThousand(et3));

        RecyclerView editContractsRc = findViewById(R.id.contractsEditRc);
        editContractsRc.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        //set previous data
        if (estateActivity().model().Contracts == null)
            estateActivity().model().Contracts = new ArrayList<>();
        editContractsRc.setAdapter(new RemovableContractsAdapter(estateActivity().model().Contracts));
        findViewById(R.id.addNewEstateBtn).setOnClickListener(view1 -> addItem());
        //set font for views
        setFont();
        //get new data
        getData();
    }

    private void setFont() {
        //for textView
        Typeface t1 = FontManager.T1_Typeface(getContext());
        ((TextView) findViewById(R.id.tv1)).setTypeface(t1);
        ((TextView) findViewById(R.id.tv3)).setTypeface(t1);
        //for textInput layout
        ((TextInputLayout) findViewById(R.id.etl1)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etl2)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etl3)).setTypeface(t1);
        //for TextInputEditText
        ((TextInputEditText) findViewById(R.id.et1)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.et2)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.et3)).setTypeface(t1);
        //for checkView
        ((TextView) findViewById(R.id.checkbox_row1).findViewById(R.id.cbText)).setTypeface(t1);
        ((TextView) findViewById(R.id.checkbox_row2).findViewById(R.id.cbText)).setTypeface(t1);
        ((TextView) findViewById(R.id.checkbox_row3).findViewById(R.id.cbText)).setTypeface(t1);
        //for button
        ((MaterialButton) findViewById(R.id.addNewEstateBtn)).setTypeface(t1);
    }


    private void getData() {
        //show loading
        estateActivity().showProgress();
        ServiceExecute.execute(new EstateContractTypeService(getContext()).getAll(new FilterModel())).subscribe(new NtkObserver<ErrorException<EstateContractTypeModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstateContractTypeModel> model) {
                estateActivity().showContent();
                EstateContractAdapterSelector adapter = new EstateContractAdapterSelector(model.ListItems, NewEstateFragment4.this::changeView);
                RecyclerView rc = findViewById(R.id.contractsRc);
                rc.setAdapter(adapter);
                FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
                flowLayoutManager.setAutoMeasureEnabled(true);
                flowLayoutManager.setAlignment(Alignment.RIGHT);
                rc.setLayoutManager(flowLayoutManager);
            }


            @Override
            public void onError(@NonNull Throwable e) {
                estateActivity().showErrorView();
            }
        });
    }

    private void changeView(EstateContractTypeModel model) {
        clearAllInput();
        selectedModel = model;
        findViewById(R.id.addNewEstateBtn).setVisibility(View.VISIBLE);
        TextInputLayout et1 = findViewById(R.id.etl1);
        et1.setHint(model.TitleRentPrice);

        et1.setVisibility(model.HasRentPrice ? View.VISIBLE : View.GONE);
        findViewById(R.id.checkbox_row1).setVisibility(model.RentPriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_row1).findViewById(R.id.cbText)).setText("قیمت توافقی");
        TextInputLayout et2 = findViewById(R.id.etl2);
        et2.setVisibility(model.HasSalePrice ? View.VISIBLE : View.GONE);
        et2.setHint(model.TitleSalePrice);

        findViewById(R.id.checkbox_row2).setVisibility(model.SalePriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_row2).findViewById(R.id.cbText)).setText("قیمت توافقی");
        TextInputLayout et3 = findViewById(R.id.etl3);
        et3.setVisibility(model.HasDepositPrice ? View.VISIBLE : View.GONE);
        et3.setHint(model.TitleDepositPrice);

        findViewById(R.id.checkbox_row3).setVisibility(model.DepositPriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_row3).findViewById(R.id.cbText)).setText("قیمت توافقی");
    }

    private void clearAllInput() {
        ((MaterialCheckBox) findViewById(R.id.checkbox_row1).findViewById(R.id.cb)).setChecked(false);
        TextInputEditText et1 = (TextInputEditText) findViewById(R.id.et1);
        et1.setText("");
        ((MaterialCheckBox) findViewById(R.id.checkbox_row2).findViewById(R.id.cb)).setChecked(false);
        TextInputEditText et2 = (TextInputEditText) findViewById(R.id.et2);
        et2.setText("");
        ((MaterialCheckBox) findViewById(R.id.checkbox_row3).findViewById(R.id.cb)).setChecked(false);
        TextInputEditText et3 = (TextInputEditText) findViewById(R.id.et3);
        et3.setText("");
    }

    private void addItem() {
        if (selectedModel != null) {
            EstateContractModel contract = new EstateContractModel();
            contract.ContractType = selectedModel;
            contract.LinkEstateContractTypeId = selectedModel.Id;
            if (selectedModel.HasRentPrice || selectedModel.RentPriceAllowAgreement) {
                if (((MaterialCheckBox) findViewById(R.id.checkbox_row1).findViewById(R.id.cb)).isChecked())
                    contract.RentPriceByAgreement = true;
                else {

                    TextInputEditText et = (TextInputEditText) findViewById(R.id.et1);
                    if (et.getText().toString().trim().equals("")) {
                        Toasty.info(getContext(), selectedModel.TitleRentPrice + " مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
                        return;
                    } else {
                        contract.RentPriceByAgreement = false;
                        contract.RentPrice = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et.getText().toString()));
                    }
                }
            } else
                contract.RentPriceByAgreement = false;
            if (selectedModel.HasSalePrice || selectedModel.SalePriceAllowAgreement) {
                if (((MaterialCheckBox) findViewById(R.id.checkbox_row2).findViewById(R.id.cb)).isChecked())
                    contract.SalePriceByAgreement = true;
                else {
                    TextInputEditText et = (TextInputEditText) findViewById(R.id.et2);
                    if (et.getText().toString().trim().equals("")) {
                        Toasty.info(getContext(), selectedModel.TitleSalePrice + " مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
                        return;
                    } else {
                        contract.SalePriceByAgreement = false;
                        contract.SalePrice = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et.getText().toString()));
                    }
                }
            } else
                contract.SalePriceByAgreement = false;

            if (selectedModel.HasDepositPrice || selectedModel.DepositPriceAllowAgreement) {
                if (((MaterialCheckBox) findViewById(R.id.checkbox_row3).findViewById(R.id.cb)).isChecked())
                    contract.DepositPriceByAgreement = true;
                else {
                    TextInputEditText et = (TextInputEditText) findViewById(R.id.et3);
                    if (et.getText().toString().trim().equals("")) {
                        Toasty.info(getContext(), selectedModel.TitleDepositPrice + " مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
                        return;
                    } else {
                        contract.DepositPriceByAgreement = false;
                        contract.DepositPrice = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et.getText().toString()));
                    }
                }
            } else
                contract.DepositPriceByAgreement = false;
            if (estateActivity().model().Contracts == null)
                estateActivity().model().Contracts = new ArrayList<>();
            estateActivity().model().Contracts.add(contract);
            ((RecyclerView) findViewById(R.id.contractsEditRc)).setAdapter(new RemovableContractsAdapter(estateActivity().model().Contracts));
            Toasty.success(getContext(), "به لیست معاملات اضافه شد", Toasty.LENGTH_LONG, true).show();

        } else
            Toasty.info(getContext(), "نوع معامله ی مورد نظر خود را انتخاب کنید", Toasty.LENGTH_LONG, true).show();
    }

    private NewEstateActivity estateActivity() {
        return ((NewEstateActivity) getActivity());
    }

    public boolean isValidForm() {
        //if contracts add can go to next page
        if (estateActivity().model().Contracts.size() == 0) {
            Toasty.error(getContext(), "لطفا برای این ملک حداقل یک نوع معامله وارد نمایید", Toasty.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}