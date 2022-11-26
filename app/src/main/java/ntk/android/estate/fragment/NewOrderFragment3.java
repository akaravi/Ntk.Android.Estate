package ntk.android.estate.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
import ntk.android.base.entitymodel.estate.EstateContractTypeModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.core.CoreCurrencyService;
import ntk.android.base.services.estate.EstateContractTypeService;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewCustomerOrderActivity;
import ntk.android.estate.adapter.EstateContractAdapterSelector;
import ntk.android.estate.view.NumberTextWatcherForThousand;

public class NewOrderFragment3 extends BaseFragment {

    private EstateContractTypeModel selectedModel;
    private int stepData = 0;


    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_order_3);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //add separator to editText
        {
            TextInputEditText et1 = findViewById(R.id.etSale);
            et1.addTextChangedListener(new NumberTextWatcherForThousand(et1));
            TextInputEditText et2 = findViewById(R.id.etRent);
            et2.addTextChangedListener(new NumberTextWatcherForThousand(et2));
            TextInputEditText et3 = findViewById(R.id.etDeposit);
            et3.addTextChangedListener(new NumberTextWatcherForThousand(et3));
            TextInputEditText et4 = findViewById(R.id.etPeriodPayment);
            et4.addTextChangedListener(new NumberTextWatcherForThousand(et4));
        }
        {
            TextInputEditText et1 = findViewById(R.id.etSale2);
            et1.addTextChangedListener(new NumberTextWatcherForThousand(et1));
            TextInputEditText et2 = findViewById(R.id.etRent2);
            et2.addTextChangedListener(new NumberTextWatcherForThousand(et2));
            TextInputEditText et3 = findViewById(R.id.etDeposit2);
            et3.addTextChangedListener(new NumberTextWatcherForThousand(et3));
            TextInputEditText et4 = findViewById(R.id.etPeriodPayment2);
            et4.addTextChangedListener(new NumberTextWatcherForThousand(et4));
        }
        //set font for views
        setFont();
        //get new data
        getData();
    }

    private void setFont() {
        //for textView
        Typeface t1 = FontManager.T1_Typeface(getContext());
        ((TextView) findViewById(R.id.tv1)).setTypeface(t1);
        //for textInput layout
        ((TextInputLayout) findViewById(R.id.etlSale)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etlRent)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etlDeposit)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etlPeriodPayment)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etlSale2)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etlRent2)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etlDeposit2)).setTypeface(t1);
        ((TextInputLayout) findViewById(R.id.etlPeriodPayment2)).setTypeface(t1);
        //for TextInputEditText
        ((TextInputEditText) findViewById(R.id.etSale)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.etRent)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.etDeposit)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.etPeriodPayment)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.etSale2)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.etRent2)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.etDeposit2)).setTypeface(t1);
        ((TextInputEditText) findViewById(R.id.etPeriodPayment2)).setTypeface(t1);
        //for checkView
    }


    private void getData() {
        //show loading
        orderActivity().showProgress();
        ServiceExecute.execute(new EstateContractTypeService(getContext()).getAll(new FilterModel())).subscribe(new NtkObserver<ErrorException<EstateContractTypeModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstateContractTypeModel> model) {
                stepData++;
                if (stepData == 2) orderActivity().showContent();
                EstateContractAdapterSelector adapter = new EstateContractAdapterSelector(model.ListItems, NewOrderFragment3.this::changeView);
                RecyclerView rc = findViewById(R.id.contractsRc);
                rc.setAdapter(adapter);
                FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
                flowLayoutManager.setAutoMeasureEnabled(true);
                flowLayoutManager.setAlignment(Alignment.RIGHT);
                rc.setLayoutManager(flowLayoutManager);
            }


            @Override
            public void onError(@NonNull Throwable e) {
                orderActivity().showErrorView();
            }
        });
        ServiceExecute.execute(new CoreCurrencyService(getContext()).getAll(new FilterModel())).subscribe(new NtkObserver<ErrorException<CoreCurrencyModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<CoreCurrencyModel> model) {
                stepData++;
                if (stepData == 2) orderActivity().showContent();
                try {
                    MaterialAutoCompleteTextView spinner = getView().findViewById(R.id.CurrencyAutoComplete);
                    List<CoreCurrencyModel> currencyList = model.ListItems;
                    List<String> names = new ArrayList<>();
                    for (CoreCurrencyModel t : currencyList)
                        names.add(t.Title);
                    if (names.size() == 0) names.add("موردی یافت نشد");
                    else orderActivity().selectedCurrency = currencyList.get(0);
                    SpinnerAdapter<CoreCurrencyModel> currencyAdapter = new SpinnerAdapter<CoreCurrencyModel>(getContext(), names);
                    spinner.setOnItemClickListener((parent, view, position, id) -> {
                        orderActivity().selectedCurrency = currencyList.get(position);
                    });
                    spinner.setAdapter(currencyAdapter);
                    // Do something for lollipop and above versions
                    spinner.setText(currencyAdapter.getItem(0), false);
                } catch (Exception e) {
                }
            }


            @Override
            public void onError(@NonNull Throwable e) {
                orderActivity().showErrorView();
            }
        });
    }

    private void changeView(EstateContractTypeModel model) {
        clearAllInput();
        selectedModel = model;

        TextInputLayout et1 = findViewById(R.id.etlSale);
        TextInputLayout et2 = findViewById(R.id.etlSale2);
        et1.setHint("حداکثر " + model.TitleSalePriceML);
        et2.setHint("حداقل " + model.TitleSalePriceML);
        et1.setVisibility(model.HasSalePrice ? View.VISIBLE : View.GONE);
        et2.setVisibility(model.HasSalePrice ? View.VISIBLE : View.GONE);

        TextInputLayout et3 = findViewById(R.id.etlRent);
        TextInputLayout et4 = findViewById(R.id.etlRent2);
        et3.setVisibility(model.HasRentPrice ? View.VISIBLE : View.GONE);
        et4.setVisibility(model.HasRentPrice ? View.VISIBLE : View.GONE);
        et3.setHint("حداکثر " + model.TitleRentPriceML);
        et4.setHint("حداقل " + model.TitleRentPriceML);

        TextInputLayout et5 = findViewById(R.id.etlDeposit);
        TextInputLayout et6 = findViewById(R.id.etlDeposit2);
        et5.setVisibility(model.HasDepositPrice ? View.VISIBLE : View.GONE);
        et6.setVisibility(model.HasDepositPrice ? View.VISIBLE : View.GONE);
        et5.setHint("حداکثر " + model.TitleDepositPriceML);
        et6.setHint("حداقل " + model.TitleDepositPriceML);

        TextInputLayout et7 = findViewById(R.id.etlPeriodPayment);
        TextInputLayout et8 = findViewById(R.id.etlPeriodPayment2);
        et7.setVisibility(model.HasPeriodPrice ? View.VISIBLE : View.GONE);
        et8.setVisibility(model.HasPeriodPrice ? View.VISIBLE : View.GONE);
        et7.setHint("حداکثر " + model.TitlePeriodPriceML);
        et8.setHint("حداقل " + model.TitlePeriodPriceML);
    }

    private void clearAllInput() {
        TextInputEditText et1 = findViewById(R.id.etSale);
        et1.setText("");
        TextInputEditText et2 = findViewById(R.id.etSale2);
        et2.setText("");
        TextInputEditText et3 = findViewById(R.id.etRent);
        et3.setText("");
        TextInputEditText et4 = findViewById(R.id.etRent2);
        et4.setText("");
        TextInputEditText et5 = findViewById(R.id.etDeposit);
        et5.setText("");
        TextInputEditText et6 = findViewById(R.id.etDeposit2);
        et6.setText("");
        TextInputEditText et7 = findViewById(R.id.etPeriodPayment);
        et7.setText("");
        TextInputEditText et8 = findViewById(R.id.etPeriodPayment2);
        et8.setText("");
    }

//    private void addItem() {
//        if (selectedModel != null) {
//            EstateContractModel contract = new EstateContractModel();
//            contract.ContractType = selectedModel;
//            contract.LinkEstateContractTypeId = selectedModel.Id;
//            if (selectedModel.HasRentPrice || selectedModel.RentPriceAllowAgreement) {
//                if (((MaterialCheckBox) findViewById(R.id.checkbox_rowRent).findViewById(R.id.cb)).isChecked())
//                    contract.RentPriceByAgreement = true;
//                else {
//
//                    TextInputEditText et = findViewById(R.id.etRent);
//                    if (et.getText().toString().trim().equals("")) {
//                        Toasty.info(getContext(), selectedModel.TitleRentPriceML + " مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
//                        return;
//                    } else {
//                        contract.RentPriceByAgreement = false;
//                        contract.RentPrice = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et.getText().toString()));
//                    }
//                }
//            } else contract.RentPriceByAgreement = false;
//            if (selectedModel.HasSalePrice || selectedModel.SalePriceAllowAgreement) {
//                if (((MaterialCheckBox) findViewById(R.id.checkbox_rowSale).findViewById(R.id.cb)).isChecked())
//                    contract.SalePriceByAgreement = true;
//                else {
//                    TextInputEditText et = findViewById(R.id.etSale);
//                    if (et.getText().toString().trim().equals("")) {
//                        Toasty.info(getContext(), selectedModel.TitleSalePriceML + " مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
//                        return;
//                    } else {
//                        contract.SalePriceByAgreement = false;
//                        contract.SalePrice = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et.getText().toString()));
//                    }
//                }
//            } else contract.SalePriceByAgreement = false;
//            if (selectedModel.HasDepositPrice || selectedModel.DepositPriceAllowAgreement) {
//                if (((MaterialCheckBox) findViewById(R.id.checkbox_rowDeposit).findViewById(R.id.cb)).isChecked())
//                    contract.DepositPriceByAgreement = true;
//                else {
//                    TextInputEditText et = findViewById(R.id.etDeposit);
//                    if (et.getText().toString().trim().equals("")) {
//                        Toasty.info(getContext(), selectedModel.TitleDepositPriceML + " مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
//                        return;
//                    } else {
//                        contract.DepositPriceByAgreement = false;
//                        contract.DepositPrice = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et.getText().toString()));
//                    }
//                }
//            } else contract.DepositPriceByAgreement = false;
//            if (selectedModel.HasPeriodPrice || selectedModel.PeriodPriceAllowAgreement) {
//                if (((MaterialCheckBox) findViewById(R.id.checkbox_rowPeriodPayment).findViewById(R.id.cb)).isChecked())
//                    contract.PeriodPriceByAgreement = true;
//                else {
//                    TextInputEditText et4 = findViewById(R.id.etPeriodPayment);
//                    if (et4.getText().toString().equals("")) {
//                        Toasty.info(getContext(), selectedModel.TitlePeriodPriceML + " مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
//                        return;
//                    } else {
//                        contract.PeriodPriceByAgreement = false;
//                        contract.PeriodPrice = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et4.getText().toString()));
//                    }
//                }
//            } else contract.PeriodPriceByAgreement = false;
//
//
//            if (estateActivity().model().Contracts == null)
//                estateActivity().model().Contracts = new ArrayList<>();
//            estateActivity().model().Contracts.add(contract);
//            ((RecyclerView) findViewById(R.id.contractsEditRc)).setAdapter(new RemovableContractsAdapter(estateActivity().model().Contracts));
//            Toasty.success(getContext(), "به لیست معاملات اضافه شد", Toasty.LENGTH_LONG, true).show();
//
//        } else
//            Toasty.info(getContext(), "نوع معامله ی مورد نظر خود را انتخاب کنید", Toasty.LENGTH_LONG, true).show();
//    }

    private NewCustomerOrderActivity orderActivity() {
        return ((NewCustomerOrderActivity) getActivity());
    }

    public boolean isValidForm() {
        //if contracts add can go to next page
        if (selectedModel == null) {
            Toasty.error(getContext(), "لطفا برای این ملک حداقل یک نوع معامله وارد نمایید", Toasty.LENGTH_LONG).show();
            return false;
        }

        TextInputEditText et1 = findViewById(R.id.etSale);
        TextInputEditText et2 = findViewById(R.id.etSale2);
        TextInputEditText et3 = findViewById(R.id.etRent);
        TextInputEditText et4 = findViewById(R.id.etRent2);
        TextInputEditText et5 = findViewById(R.id.etDeposit);
        TextInputEditText et6 = findViewById(R.id.etDeposit2);
        TextInputEditText et7 = findViewById(R.id.etPeriodPayment);
        TextInputEditText et8 = findViewById(R.id.etPeriodPayment2);
        orderActivity().model().LinkContractTypeId = selectedModel.Id;
        double SalePriceMax = 0, SalePriceMin = 0, RentPriceMax = 0, RentPriceMin = 0,
                DepositPriceMax = 0, DepositPriceMin = 0,
                PeriodPriceMax = 0, PeriodPriceMin = 0;
        if (!et1.getText().toString().equals(""))
            SalePriceMax = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et1.getText().toString()));
        if (!et2.getText().toString().equals(""))
            SalePriceMin = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et2.getText().toString()));
        if (!et3.getText().toString().equals(""))
            RentPriceMax = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et3.getText().toString()));
        if (!et4.getText().toString().equals(""))
            RentPriceMin = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et4.getText().toString()));
        if (!et5.getText().toString().equals(""))
            DepositPriceMax = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et5.getText().toString()));
        if (!et6.getText().toString().equals(""))
            DepositPriceMin = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et6.getText().toString()));
        if (!et7.getText().toString().equals(""))
            PeriodPriceMax = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et7.getText().toString()));
        if (!et8.getText().toString().equals(""))
            PeriodPriceMin = Double.valueOf(NumberTextWatcherForThousand.trimCommaOfString(et8.getText().toString()));

        if (SalePriceMin != 0 && SalePriceMax != 0 && SalePriceMax < SalePriceMin) {
            Toasty.error(getContext(), "میزان حداقلی " + selectedModel.TitleSalePriceML + "باید کم تر از میزان حداکثری باشد", Toasty.LENGTH_LONG).show();
            return false;
        }
        if (RentPriceMin != 0 && RentPriceMax != 0 && RentPriceMax < RentPriceMin) {
            Toasty.error(getContext(), "میزان حداقلی " + selectedModel.TitleRentPriceML + "باید کم تر از میزان حداکثری باشد", Toasty.LENGTH_LONG).show();
            return false;
        }
        if (DepositPriceMin != 0 && DepositPriceMax != 0 && DepositPriceMax < DepositPriceMin) {
            Toasty.error(getContext(), "میزان حداقلی " + selectedModel.TitleDepositPriceML + "باید کم تر از میزان حداکثری باشد", Toasty.LENGTH_LONG).show();
            return false;
        }
        if (PeriodPriceMin != 0 && PeriodPriceMax != 0 && PeriodPriceMax < PeriodPriceMin) {
            Toasty.error(getContext(), "میزان حداقلی " + selectedModel.TitlePeriodPriceML + "باید کم تر از میزان حداکثری باشد", Toasty.LENGTH_LONG).show();
            return false;
        }
        if (SalePriceMin != 0)
            orderActivity().model().SalePriceMin = SalePriceMin;
        else
            orderActivity().model().SalePriceMin = null;
        if (SalePriceMax != 0)
            orderActivity().model().SalePriceMax = SalePriceMax;
        else
            orderActivity().model().SalePriceMax = null;
        if (RentPriceMin != 0)
            orderActivity().model().RentPriceMin = RentPriceMin;
        else
            orderActivity().model().RentPriceMin = null;
        if (RentPriceMax != 0)
            orderActivity().model().RentPriceMax = RentPriceMax;
        else
            orderActivity().model().RentPriceMax = null;
        if (DepositPriceMin != 0)
            orderActivity().model().DepositPriceMin = DepositPriceMin;
        else
            orderActivity().model().DepositPriceMin = null;
        if (DepositPriceMax != 0)
            orderActivity().model().DepositPriceMax = DepositPriceMax;
        else
            orderActivity().model().DepositPriceMax = null;
       if (PeriodPriceMin != 0)
            orderActivity().model().PeriodPriceMin = PeriodPriceMin;
        else
            orderActivity().model().PeriodPriceMin = null;
       if (PeriodPriceMax != 0)
            orderActivity().model().PeriodPriceMax = PeriodPriceMax;
        else
            orderActivity().model().PeriodPriceMax = null;
        return true;
    }
}