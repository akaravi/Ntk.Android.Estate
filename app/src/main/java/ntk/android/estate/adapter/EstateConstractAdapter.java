package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstateContractModel;
import ntk.android.estate.R;

public class EstateConstractAdapter extends BaseRecyclerAdapter<EstateContractModel, EstateConstractAdapter.VC> {
    public EstateConstractAdapter(List<EstateContractModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public VC onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VC(inflate(parent, R.layout.row_recycler_contract));
    }

    @Override
    public void onBindViewHolder(@NonNull VC holder, int position) {
        holder.setData(getItem(position));
    }

    public class VC extends RecyclerView.ViewHolder {
        TextView price1;
        TextView title;
        ImageView icon;

        public VC(@NonNull View itemView) {
            super(itemView);
            price1 = itemView.findViewById(R.id.txtPrice1);
            title = itemView.findViewById(R.id.txtPrice1Title1);
            icon = itemView.findViewById(R.id.priceImage1);
        }

        public void setData(EstateContractModel item) {
            icon.setImageResource(R.drawable.contracts);
            if (item.SalePrice != null || item.SalePriceByAgreement) {
                title.setText("فروش :  ");
                if (item.SalePrice != null && item.SalePrice != 0)
                    price1.setText(item.SalePrice + "  " + item.UnitSalePrice);
                if (item.SalePriceByAgreement)
                    price1.setText(price1.getText().toString().isEmpty() ? "توافقی" : price1.getText().toString() + "|| توافقی");
            } else if (item.RentPrice != null || item.RentPriceByAgreement) {
                title.setText("اجاره :  ");
                if (item.RentPrice != null && item.RentPrice != 0)
                    price1.setText(item.RentPrice + "  " + item.UnitSalePrice);
                if (item.RentPriceByAgreement)
                    price1.setText(price1.getText().toString().isEmpty() ? "توافقی" : price1.getText().toString() + "|| توافقی");
            } else if (item.DepositPrice != null || item.DepositPriceByAgreement) {
                title.setText("رهن :  ");
                if (item.DepositPrice != null && item.DepositPrice != 0)
                    price1.setText(item.DepositPrice + "  " + item.UnitSalePrice);
                if (item.DepositPriceByAgreement)
                    price1.setText(price1.getText().toString().isEmpty() ? "توافقی" : price1.getText().toString() + "|| توافقی");
            }
        }
    }
}
