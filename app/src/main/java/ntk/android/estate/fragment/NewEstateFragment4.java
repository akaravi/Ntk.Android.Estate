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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ntk.android.base.adapter.SpinnerAdapter;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.coremain.CoreCurrencyModel;
import ntk.android.base.entitymodel.estate.EstateContractModel;
import ntk.android.base.entitymodel.estate.EstateContractTypeModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.core.CoreCurrencyService;
import ntk.android.base.services.estate.EstateContractTypeService;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;
import ntk.android.estate.adapter.EstateContractAdapterSelector;
import ntk.android.estate.adapter.RemovableContractsAdapter;
import ntk.android.estate.view.NumberTextWatcherForThousand;

public class NewEstateFragment4 extends BaseFragment {

    private EstateContractTypeModel selectedModel;
    private int stepData = 0;


    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_4);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //click on all of view to affect on  toggling checkBox
        findViewById(R.id.checkbox_rowSale).setOnClickListener(v -> ((CheckBox) v.findViewById(R.id.cb)).toggle());
        findViewById(R.id.checkbox_rowRent).setOnClickListener(v -> ((CheckBox) v.findViewById(R.id.cb)).toggle());
        findViewById(R.id.checkbox_rowDeposit).setOnClickListener(v -> ((CheckBox) v.findViewById(R.id.cb)).toggle());
        findViewById(R.id.checkbox_rowPeriodPayment).setOnClickListener(v -> ((CheckBox) v.findViewById(R.id.cb)).toggle());
        //toggle state of Edittext on toggling Checkbox
        ((CheckBox) findViewById(R.id.checkbox_rowSale).findViewById(R.id.cb)).setOnCheckedChangeListener((compoundButton, b) -> findViewById(R.id.etSale).setEnabled(!b));
        ((CheckBox) findViewById(R.id.checkbox_rowRent).findViewById(R.id.cb)).setOnCheckedChangeListener((compoundButton, b) -> findViewById(R.id.etRent).setEnabled(!b));
        ((CheckBox) findViewById(R.id.checkbox_rowDeposit).findViewById(R.id.cb)).setOnCheckedChangeListener((compoundButton, b) -> findViewById(R.id.etDeposit).setEnabled(!b));
        ((CheckBox) findViewById(R.id.checkbox_rowPeriodPayment).findViewById(R.id.cb)).setOnCheckedChangeListener(
                (compoundButton, b) -> {
                    findViewById(R.id.etPeriodPayment).setEnabled(!b);
                });
        //add separator to editText
        TextInputEditText et1 = findViewById(R.id.etSale);
        et1.addTextChangedListener(new NumberTextWatcherForThousand(et1));
        TextInputEditText et2 = findViewById(R.id.etRent);
        et2.addTextChangedListener(new NumberTextWatcherForThousand(et2));
        TextInputEditText et3 = findViewById(R.id.etDeposit);
        et3.addTextChangedListener(new NumberTextWatcherForThousand(et3));
        TextInputEditText et4 = findViewById(R.id.etPeriodPayment);
        et4.addTextChangedListener(new NumberTextWatcherForThousand(et4));

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
        ((TextInputLayout) findViewById(R.id.etlSale)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etlRent)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etlDeposit)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etlPeriodPayment)).setTypeface(t1);
        //for TextInputEditText
        ((TextInputEditText) findViewById(R.id.etSale)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.etRent)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.etDeposit)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.etPeriodPayment)).setTypeface(t1);
        //for checkView
        ((TextView) findViewById(R.id.checkbox_rowSale).findViewById(R.id.cbText)).setTypeface(t1);
        ((TextView) findViewById(R.id.checkbox_rowRent).findViewById(R.id.cbText)).setTypeface(t1);
        ((TextView) findViewById(R.id.checkbox_rowDeposit).findViewById(R.id.cbText)).setTypeface(t1);
        ((TextView) findViewById(R.id.checkbox_rowPeriodPayment).findViewById(R.id.cbText)).setTypeface(t1);
        //for button
        ((MaterialButton) findViewById(R.id.addNewEstateBtn)).setTypeface(t1);
    }


    private void getData() {
        //show loading
        estateActivity().showProgress();
        ServiceExecute.execute(new EstateContractTypeService(getContext()).getAll(new FilterModel())).subscribe(new NtkObserver<ErrorException<EstateContractTypeModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstateContractTypeModel> model) {
                stepData++;
                if (stepData == 2)
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
        ServiceExecute.execute(new CoreCurrencyService(getContext()).getAll(new FilterModel())).subscribe(new NtkObserver<ErrorException<CoreCurrencyModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<CoreCurrencyModel> model) {
                stepData++;
                if (stepData == 2)
                    estateActivity().showContent();
                try {
                    MaterialAutoCompleteTextView spinner = getView().findViewById(R.id.CurrencyAutoComplete);
                    List<CoreCurrencyModel> currencyList = model.ListItems;
                    List<String> names = new ArrayList<>();
                    for (CoreCurrencyModel t : currencyList)
                        names.add(t.Title);
                    if (names.size() == 0)
                        names.add("موردی یافت نشد");
                    else
                        estateActivity().selectedCurrency = currencyList.get(0);
                    SpinnerAdapter<CoreCurrencyModel> currencyAdapter = new SpinnerAdapter<CoreCurrencyModel>(getContext(), names);
                    spinner.setOnItemClickListener((parent, view, position, id) -> {
                        estateActivity().selectedCurrency = currencyList.get(position);
                    });
                    spinner.setAdapter(currencyAdapter);
                    // Do something for lollipop and above versions
                    spinner.setText(currencyAdapter.getItem(0), false);
                } catch (Exception e) {
                }
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

        TextInputLayout et1 = findViewById(R.id.etlSale);
        et1.setHint(model.TitleSalePriceML);
        et1.setVisibility(model.HasSalePrice ? View.VISIBLE : View.GONE);
        findViewById(R.id.checkbox_rowSale).setVisibility(model.SalePriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_rowSale).findViewById(R.id.cbText)).setText("قیمت توافقی");

        TextInputLayout et2 = findViewById(R.id.etlRent);
        et2.setVisibility(model.HasRentPrice ? View.VISIBLE : View.GONE);
        et2.setHint(model.TitleRentPriceML);
        findViewById(R.id.checkbox_rowRent).setVisibility(model.RentPriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_rowRent).findViewById(R.id.cbText)).setText("قیمت توافقی");

        TextInputLayout et3 = findViewById(R.id.etlDeposit);
        et3.setVisibility(model.HasDepositPrice ? View.VISIBLE : View.GONE);
        et3.setHint(model.TitleDepositPriceML);
        findViewById(R.id.checkbox_rowDeposit).setVisibility(model.DepositPriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_rowDeposit).findViewById(R.id.cbText)).setText("قیمت توافقی");

        TextInputLayout et4 = findViewById(R.id.etlPeriodPayment);
        et4.setVisibility(model.HasPeriodPrice ? View.VISIBLE : View.GONE);
        et4.setHint(model.TitlePeriodPriceML);
        findViewById(R.id.checkbox_rowPeriodPayment).setVisibility(model.PeriodPriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_rowPeriodPayment).findViewById(R.id.cbText)).setText("قیمت توافقی");
    }

    private void clearAllInput() {
        ((MaterialCheckBox) findViewById(R.id.checkbox_rowSale).findViewById(R.id.cb)).setChecked(false);
        TextInputEditText et1 = findViewById(R.id.etSale);
        et1.setText("");
        ((MaterialCheckBox) findViewById(R.id.checkbox_rowRent).findViewById(R.id.cb)).setChecked(false);
        TextInputEditText et2 = findViewById(R.id.etRent);
        et2.setText("");
        ((MaterialCheckBox) findViewById(R.id.checkbox_rowDeposit).findViewById(R.id.cb)).setChecked(false);
        TextInputEditText et3 = findViewById(R.id.etDeposit);
        et3.setText("");
        ((MaterialCheckBox) findViewById(R.id.checkbox_rowPeriodPayment).findViewById(R.id.cb)).setChecked(false);
        TextInputEditText et4 = findViewById(R.id.etPeriodPayment);
        et4.setText("");
    }

    private void addItem() {
        if (selectedModel != null) {
            EstateContractModel contract = new EstateContractModel();
            contract.ContractType = selectedModel;
            contract.LinkEstateContractTypeId = selectedModel.Id;
            if (selectedModel.HasRentPrice || selectedModel.RentPriceAllowAgreement) {
                if (((MaterialCheckBox) findViewById(R.id.checkbox_rowRent).findViewById(R.id.cb)).isChecked())
                    contract.RentPriceByAgreement = true;
                else {

                    TextInputEditText et = findViewById(R.id.etRent);
                    if (et.getText().toString().trim().equals("")) {
                        Toasty.info(getContext(), selectedModel.TitleRentPriceML + " مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
                        return;
                    } else {
                        contract.RentPriceByAgreement = false;
                        contract.RentPrice = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et.getText().toString()));
                    }
                }
            } else
                contract.RentPriceByAgreement = false;
            if (selectedModel.HasSalePrice || selectedModel.SalePriceAllowAgreement) {
                if (((MaterialCheckBox) findViewById(R.id.checkbox_rowSale).findViewById(R.id.cb)).isChecked())
                    contract.SalePriceByAgreement = true;
                else {
                    TextInputEditText et = findViewById(R.id.etSale);
                    if (et.getText().toString().trim().equals("")) {
                        Toasty.info(getContext(), selectedModel.TitleSalePriceML + " مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
                        return;
                    } else {
                        contract.SalePriceByAgreement = false;
                        contract.SalePrice = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et.getText().toString()));
                    }
                }
            } else
                contract.SalePriceByAgreement = false;
            if (selectedModel.HasDepositPrice || selectedModel.DepositPriceAllowAgreement) {
                if (((MaterialCheckBox) findViewById(R.id.checkbox_rowDeposit).findViewById(R.id.cb)).isChecked())
                    contract.DepositPriceByAgreement = true;
                else {
                    TextInputEditText et = findViewById(R.id.etDeposit);
                    if (et.getText().toString().trim().equals("")) {
                        Toasty.info(getContext(), selectedModel.TitleDepositPriceML + " مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
                        return;
                    } else {
                        contract.DepositPriceByAgreement = false;
                        contract.DepositPrice = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et.getText().toString()));
                    }
                }
            } else
                contract.DepositPriceByAgreement = false;
            if (selectedModel.HasPeriodPrice || selectedModel.PeriodPriceAllowAgreement) {
                if (((MaterialCheckBox) findViewById(R.id.checkbox_rowPeriodPayment).findViewById(R.id.cb)).isChecked())
                    contract.PeriodPriceByAgreement = true;
                else {
                    TextInputEditText et4 = findViewById(R.id.etPeriodPayment);
                    if (et4.getText().toString().equals("")) {
                        Toasty.info(getContext(), selectedModel.TitlePeriodPriceML + " مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
                        return;
                    } else {
                        contract.PeriodPriceByAgreement = false;
                        contract.PeriodPrice = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et4.getText().toString()));
                    }
                }
            } else
                contract.PeriodPriceByAgreement = false;


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