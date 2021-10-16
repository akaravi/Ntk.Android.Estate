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
        TextView title1;
        ImageView icon1;
        TextView price2;
        TextView title2;
        ImageView icon2;

        public VC(@NonNull View itemView) {
            super(itemView);
            price1 = itemView.findViewById(R.id.txtPrice1);
            title1 = itemView.findViewById(R.id.txtPrice1Title1);
            icon1 = itemView.findViewById(R.id.priceImage1);
            price2 = itemView.findViewById(R.id.txtPrice2);
            title2 = itemView.findViewById(R.id.txtPriceTitle2);
            icon2 = itemView.findViewById(R.id.priceImage2);
        }

        public void setData(EstateContractModel item) {
            price2.setVisibility(View.GONE);
            title2.setVisibility(View.GONE);
            icon2.setVisibility(View.GONE);
            icon1.setImageResource(R.drawable.contracts);

            if (item.SalePrice != null || item.SalePriceByAgreement) {

                title1.setText("فروش :  ");
                if (item.SalePrice != null && item.SalePrice != 0)
                    price1.setText(item.SalePrice + "  " + item.UnitSalePrice);
                if (item.SalePriceByAgreement)
                    price1.setText(price1.getText().toString().isEmpty() ? "توافقی" : price1.getText().toString() + "|| توافقی");

            } else if (item.RentPrice != null || item.RentPriceByAgreement) {
                title1.setText("اجاره :  ");
                if (item.RentPrice != null && item.RentPrice != 0)
                    price1.setText(item.RentPrice + "  " + item.UnitSalePrice);
                if (item.RentPriceByAgreement)
                    price1.setText(price1.getText().toString().isEmpty() ? "توافقی" : price1.getText().toString() + "|| توافقی");
                if (item.DepositPrice != null || item.DepositPriceByAgreement) {
                    price2.setVisibility(View.VISIBLE);
                    title2.setVisibility(View.VISIBLE);
                    icon2.setVisibility(View.VISIBLE);

                    icon2.setImageResource(R.drawable.contracts);
                    title2.setText("رهن :  ");
                    if (item.DepositPrice != null && item.DepositPrice != 0)
                        price2.setText(item.DepositPrice + "  " + item.UnitSalePrice);
                    if (item.DepositPriceByAgreement)
                        price2.setText(price2.getText().toString().isEmpty() ? "توافقی" : price2.getText().toString() + "|| توافقی");
                }
            }
        }
    }
}
