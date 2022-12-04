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
import java9.util.stream.IntStream;
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
    EstateContractTypeModel selectedModel;

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
            et1.setText(orderActivity().model().SalePriceMax + "");
            TextInputEditText et2 = findViewById(R.id.etRent);
            et2.addTextChangedListener(new NumberTextWatcherForThousand(et2));
            et2.setText(orderActivity().model().RentPriceMax + "");
            TextInputEditText et3 = findViewById(R.id.etDeposit);
            et3.addTextChangedListener(new NumberTextWatcherForThousand(et3));
            et3.setText(orderActivity().model().DepositPriceMax + "");
            TextInputEditText et4 = findViewById(R.id.etPeriodPayment);
            et4.addTextChangedListener(new NumberTextWatcherForThousand(et4));
            et4.setText(orderActivity().model().PeriodPriceMax + "");
        }
        {
            TextInputEditText et1 = findViewById(R.id.etSale2);
            et1.addTextChangedListener(new NumberTextWatcherForThousand(et1));
            et1.setText(orderActivity().model().SalePriceMin + "");
            TextInputEditText et2 = findViewById(R.id.etRent2);
            et2.addTextChangedListener(new NumberTextWatcherForThousand(et2));
            et2.setText(orderActivity().model().RentPriceMin + "");
            TextInputEditText et3 = findViewById(R.id.etDeposit2);
            et3.addTextChangedListener(new NumberTextWatcherForThousand(et3));
            et3.setText(orderActivity().model().DepositPriceMin + "");
            TextInputEditText et4 = findViewById(R.id.etPeriodPayment2);
            et4.addTextChangedListener(new NumberTextWatcherForThousand(et4));
            et4.setText(orderActivity().model().PeriodPriceMin + "");
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
                String lastSelectedId = orderActivity().model().LinkContractTypeId + "";

                //find last position of selected model or -1
                int lastSelected = IntStream.range(0, model.ListItems.size()).filter(i -> lastSelectedId.equals(model.ListItems.get(i).Id)).findFirst().orElse(-1);
                EstateContractAdapterSelector adapter = new EstateContractAdapterSelector(lastSelected, model.ListItems, NewOrderFragment3.this::changeView);
                RecyclerView rc = findViewById(R.id.contractsRc);
                rc.setAdapter(adapter);
                FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
                flowLayoutManager.setAutoMeasureEnabled(true);
                flowLayoutManager.setAlignment(Alignment.RIGHT);
                rc.setLayoutManager(flowLayoutManager);
                if (lastSelected > -1) {
                    changeView(model.ListItems.get(lastSelected));
                    setLatsSelected(model.ListItems.get(lastSelected));
                }
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

    private void setLatsSelected(EstateContractTypeModel estateContractTypeModel) {
        {
            TextInputEditText et1 = findViewById(R.id.etSale);
            if (orderActivity().model().SalePriceMax != null && orderActivity().model().SalePriceMax != 0)
                et1.setText(orderActivity().model().SalePriceMax + "");
            TextInputEditText et2 = findViewById(R.id.etRent);
            if (orderActivity().model().RentPriceMax != null && orderActivity().model().RentPriceMax != 0)
                et2.setText(orderActivity().model().RentPriceMax + "");
            TextInputEditText et3 = findViewById(R.id.etDeposit);
            if (orderActivity().model().DepositPriceMax != null && orderActivity().model().DepositPriceMax != 0)
                et3.setText(orderActivity().model().DepositPriceMax + "");
            TextInputEditText et4 = findViewById(R.id.etPeriodPayment);
            if (orderActivity().model().PeriodPriceMax != null && orderActivity().model().PeriodPriceMax != 0)
                et4.setText(orderActivity().model().PeriodPriceMax + "");
        }
        {
            TextInputEditText et1 = findViewById(R.id.etSale2);
            if (orderActivity().model().SalePriceMin != null && orderActivity().model().SalePriceMin != 0)
                et1.setText(orderActivity().model().SalePriceMin + "");
            TextInputEditText et2 = findViewById(R.id.etRent2);
            if (orderActivity().model().RentPriceMin != null && orderActivity().model().RentPriceMin != 0)
                et2.setText(orderActivity().model().RentPriceMin + "");
            TextInputEditText et3 = findViewById(R.id.etDeposit2);
            if (orderActivity().model().DepositPriceMin != null && orderActivity().model().DepositPriceMin != 0)
                et3.setText(orderActivity().model().DepositPriceMin + "");
            TextInputEditText et4 = findViewById(R.id.etPeriodPayment2);
            if (orderActivity().model().PeriodPriceMin != null && orderActivity().model().PeriodPriceMin != 0)
                et4.setText(orderActivity().model().PeriodPriceMin + "");
        }
    }


    private void changeView(EstateContractTypeModel model) {
        clearAllInput();
        orderActivity().model().LinkContractTypeId = model.Id;
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

    private NewCustomerOrderActivity orderActivity() {
        return ((NewCustomerOrderActivity) getActivity());
    }

    public boolean isValidForm() {
        //if contracts add can go to next page
        if (orderActivity().model().LinkContractTypeId == null || orderActivity().model().LinkContractTypeId.equals("")) {
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