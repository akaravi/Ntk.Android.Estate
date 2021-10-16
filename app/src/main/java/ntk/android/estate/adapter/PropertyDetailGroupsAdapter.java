package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;

public class PropertyDetailGroupsAdapter extends BaseRecyclerAdapter<EstatePropertyDetailGroupModel, PropertyDetailGroupsAdapter.VH> {
    public PropertyDetailGroupsAdapter(List<EstatePropertyDetailGroupModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.setData(getItem(position));
    }

    public class VH extends RecyclerView.ViewHolder {
        public VH(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(EstatePropertyDetailGroupModel m) {
//            if (m.SalePrice != null || m.SalePriceByAgreement) {
//                priceTitle1.setVisibility(View.VISIBLE);
//                price1.setVisibility(View.VISIBLE);
//                priceTitle1.setText("فروش :  ");
//                if (m.SalePrice != null && m.SalePrice != 0)
//                    price1.setText(m.SalePrice + "  " + m.UnitSalePrice);
//                if (m.SalePriceByAgreement)
//                    price1.setText(price1.getText().toString().isEmpty() ? "توافقی" : price1.getText().toString() + "|| توافقی");
//            } else {
//                priceTitle1.setVisibility(View.GONE);
//                price1.setVisibility(View.GONE);
//
//            }
//            if (m.RentPrice != null || m.RentPriceByAgreement) {
//                priceTitle2.setVisibility(View.VISIBLE);
//                price2.setVisibility(View.VISIBLE);
//                priceTitle2.setText("اجاره :  ");
//                if (m.RentPrice != null && m.RentPrice != 0)
//                    price2.setText(m.RentPrice + "  " + m.UnitSalePrice);
//                if (m.RentPriceByAgreement)
//                    price2.setText(price2.getText().toString().isEmpty() ? "توافقی" : price2.getText().toString() + "|| توافقی");
//            } else {
//                priceTitle2.setVisibility(View.GONE);
//                price2.setVisibility(View.GONE);
//
//            }
//            if (m.DepositPrice != null || m.DepositPriceByAgreement) {
//                priceTitle3.setVisibility(View.VISIBLE);
//                price3.setVisibility(View.VISIBLE);
//                priceTitle3.setText("رهن :  ");
//                if (m.DepositPrice != null && m.DepositPrice != 0)
//                    price3.setText(m.DepositPrice + "  " + m.UnitSalePrice);
//                if (m.DepositPriceByAgreement)
//                    price3.setText(price3.getText().toString().isEmpty() ? "توافقی" : price3.getText().toString() + "|| توافقی");
//            } else {
//                priceTitle3.setVisibility(View.GONE);
//                price3.setVisibility(View.GONE);
//
//            }
        }
    }
}
