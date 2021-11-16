package ntk.android.estate.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;
import ntk.android.estate.adapter.EstateContractAdapterSelector;
import ntk.android.estate.adapter.RemovableContractsAdapter;

public class NewEstateFragment4 extends BaseFragment {

    private EstateContractTypeModel selectedModel;

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_4);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
        RecyclerView editContractsRc = findViewById(R.id.contractsEditRc);
        editContractsRc.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        editContractsRc.setAdapter(new RemovableContractsAdapter(new ArrayList<>()));
        findViewById(R.id.addNewEstateBtn).setOnClickListener(view1 -> addItem());
    }


    private void getData() {
        ServiceExecute.execute(new EstateContractTypeService(getContext()).getAll(new FilterModel())).subscribe(new NtkObserver<ErrorException<EstateContractTypeModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<EstateContractTypeModel> model) {
                estateActivity().showContent();
                EstateContractAdapterSelector adapter = new EstateContractAdapterSelector(model.ListItems, NewEstateFragment4.this::changeView);
                RecyclerView rc = findViewById(R.id.contractsRc);
                rc.setAdapter(adapter);
                rc.setLayoutManager(new GridLayoutManager(getContext(), 3));
            }


            @Override
            public void onError(@NonNull Throwable e) {
                estateActivity().showErrorView();
            }
        });
    }

    private void changeView(EstateContractTypeModel model) {
        selectedModel = model;
        findViewById(R.id.addNewEstateBtn).setVisibility(View.VISIBLE);
        TextInputLayout et1 = findViewById(R.id.etl1);
        et1.setHint("قیمت اجاره");
        et1.setVisibility(model.HasRentPrice ? View.VISIBLE : View.GONE);
        findViewById(R.id.checkbox_row1).setVisibility(model.RentPriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_row1).findViewById(R.id.cbText)).setText("قیمت توافقی");
        TextInputLayout et2 = findViewById(R.id.etl2);
        et2.setVisibility(model.HasSalePrice ? View.VISIBLE : View.GONE);
        et2.setHint("قیمت فروش");
        findViewById(R.id.checkbox_row2).setVisibility(model.SalePriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_row2).findViewById(R.id.cbText)).setText("قیمت توافقی");
        TextInputLayout et3 = findViewById(R.id.etl3);
        et3.setVisibility(model.HasDepositPrice ? View.VISIBLE : View.GONE);
        et3.setHint(model.Title);
        et3.setHint("قیمت رهن");
        findViewById(R.id.checkbox_row3).setVisibility(model.DepositPriceAllowAgreement ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.checkbox_row3).findViewById(R.id.cbText)).setText("قیمت توافقی");
    }

    private void addItem() {
        if (selectedModel != null) {
            EstateContractModel contract = new EstateContractModel();
            contract.ContractType=selectedModel;
            if (selectedModel.HasRentPrice || selectedModel.RentPriceAllowAgreement) {
                if (((MaterialCheckBox) findViewById(R.id.checkbox_row1).findViewById(R.id.cb)).isChecked())
                    contract.RentPriceByAgreement = true;
                else {
                    TextInputEditText et = (TextInputEditText) findViewById(R.id.et1);
                    if (et.getText().toString().trim().equals("")) {
                        Toasty.info(getContext(), "مبلغ احاره مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
                        return;
                    } else
                        contract.RentPrice = Double.valueOf(et.getText().toString());
                }
            }
            if (selectedModel.HasSalePrice || selectedModel.SalePriceAllowAgreement) {
                if (((MaterialCheckBox) findViewById(R.id.checkbox_row2).findViewById(R.id.cb)).isChecked())
                    contract.SalePriceByAgreement = true;
                else {
                    TextInputEditText et = (TextInputEditText) findViewById(R.id.et2);
                    if (et.getText().toString().trim().equals("")) {
                        Toasty.info(getContext(), "مبلغ فروش مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
                        return;
                    } else
                        contract.SalePrice = Double.valueOf(et.getText().toString());
                }
            }
            if (selectedModel.HasDepositPrice || selectedModel.DepositPriceAllowAgreement) {
                if (((MaterialCheckBox) findViewById(R.id.checkbox_row3).findViewById(R.id.cb)).isChecked())
                    contract.DepositPriceByAgreement = true;
                else {
                    TextInputEditText et = (TextInputEditText) findViewById(R.id.et3);
                    if (et.getText().toString().trim().equals("")) {
                        Toasty.info(getContext(), "مبلغ رهن مورد نظر خود را وارد نمایید", Toasty.LENGTH_LONG, true).show();
                        return;
                    } else
                        contract.DepositPrice =  Double.valueOf(et.getText().toString());
                }
            }
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
        return true;
    }
}