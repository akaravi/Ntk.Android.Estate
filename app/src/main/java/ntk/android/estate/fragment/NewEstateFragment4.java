package ntk.android.estate.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import es.dmoral.toasty.Toasty;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstateContractTypeModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.estate.EstateContractTypeService;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;
import ntk.android.estate.adapter.EstateContractAdapterSelector;

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
            if (selectedModel.HasRentPrice || selectedModel.RentPriceAllowAgreement) {

            }
            if (selectedModel.HasSalePrice || selectedModel.SalePriceAllowAgreement) {

            }
            if (selectedModel.HasDepositPrice || selectedModel.DepositPriceAllowAgreement) {

            }
        } else
            Toasty.info(getContext(), "نوع معامله ی مورد نظر خود را انتخاب کنید");
    }

    private NewEstateActivity estateActivity() {
        return ((NewEstateActivity) getActivity());
    }

    public boolean isValidForm() {
        return true;
    }
}