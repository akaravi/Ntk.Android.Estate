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
import ntk.android.base.utill.FontManager;
import ntk.android.base.view.NViewUtils;
import ntk.android.estate.R;

public class EstateContractAdapter extends BaseRecyclerAdapter<EstateContractModel, EstateContractAdapter.VH> {
    public EstateContractAdapter(List<EstateContractModel> list) {
        super(list);
        drawable=R.drawable.sweet_error_center_x;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_recycler_contract));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.setData(getItem(position));
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView contractTitle;
        TextView price1;
        TextView title1;
        ImageView icon1;
        TextView price2;
        TextView title2;
        ImageView icon2;
        TextView price3;
        TextView title3;
        ImageView icon3;

        public VH(@NonNull View itemView) {
            super(itemView);
            contractTitle = itemView.findViewById(R.id.title);
            price1 = itemView.findViewById(R.id.txtPrice1);
            title1 = itemView.findViewById(R.id.txtPrice1Title1);
            icon1 = itemView.findViewById(R.id.priceImage1);
            title2 = itemView.findViewById(R.id.txtPriceTitle2);
            price2 = itemView.findViewById(R.id.txtPrice2);
            icon2 = itemView.findViewById(R.id.priceImage2);
            title3 = itemView.findViewById(R.id.txtPriceTitle3);
            price3 = itemView.findViewById(R.id.txtPrice3);
            icon3 = itemView.findViewById(R.id.priceImage3);
            icon1.setImageResource(R.drawable.contracts);
            icon2.setImageResource(R.drawable.contracts);
            icon3.setImageResource(R.drawable.contracts);
            setFont(price1, price2, price3);
            setFont(title1, title2, title3);
            contractTitle.setTypeface(FontManager.T1_Typeface(itemView.getContext()));
        }

        private void setFont(TextView t1, TextView t2, TextView t3) {
            t1.setTypeface(FontManager.T1_Typeface(itemView.getContext()));
            t2.setTypeface(FontManager.T1_Typeface(itemView.getContext()));
            t3.setTypeface(FontManager.T1_Typeface(itemView.getContext()));
        }

        public void setData(EstateContractModel item) {
            contractTitle.setText(item.ContractType.Title);
            if (item.ContractType.HasSalePrice) {
                itemView.findViewById(R.id.linear1).setVisibility(View.VISIBLE);
                title1.setText(item.ContractType.TitleSalePrice + "  :   ");
                if (item.SalePrice != null || item.SalePriceByAgreement) {
                    if (item.SalePrice != null && item.SalePrice != 0)
                        if (item.SalePriceByAgreement)
                            price1.setText(NViewUtils.PriceFormat(item.SalePrice) + "  " + item.UnitSalePrice + "|| توافقی");
                        else
                            price1.setText(NViewUtils.PriceFormat(item.SalePrice) + "  " + item.UnitSalePrice);
                    else if (item.SalePriceByAgreement)
                        price1.setText("توافقی");

                }
            } else
                itemView.findViewById(R.id.linear1).setVisibility(View.GONE);
            if (item.ContractType.HasDepositPrice) {
                itemView.findViewById(R.id.linear2).setVisibility(View.VISIBLE);
                title2.setText(item.ContractType.TitleDepositPrice + "  :   ");
                if (item.DepositPrice != null || item.DepositPriceByAgreement) {

                    if (item.DepositPrice != null && item.DepositPrice != 0)
                        if (item.DepositPriceByAgreement)
                            price2.setText(NViewUtils.PriceFormat(item.DepositPrice) + "  " + item.UnitSalePrice + "|| توافقی");
                        else
                            price2.setText(NViewUtils.PriceFormat(item.DepositPrice) + "  " + item.UnitSalePrice);
                    else if (item.DepositPriceByAgreement)
                        price2.setText("توافقی");
                }
            } else {
                itemView.findViewById(R.id.linear2).setVisibility(View.GONE);
            }
            if (item.ContractType.HasRentPrice) {
                itemView.findViewById(R.id.linear3).setVisibility(View.VISIBLE);
                title3.setText(item.ContractType.TitleRentPrice + "  :   ");
                if (item.RentPrice != null || item.RentPriceByAgreement) {
                    if (item.RentPrice != null && item.RentPrice != 0)
                        if (item.RentPriceByAgreement)
                            price3.setText(NViewUtils.PriceFormat(item.RentPrice) + "  " + item.UnitSalePrice + "|| توافقی");
                        else
                            price3.setText(NViewUtils.PriceFormat(item.RentPrice) + "  " + item.UnitSalePrice);
                    else if (item.RentPriceByAgreement)
                        price3.setText("توافقی");
                }
            } else
                itemView.findViewById(R.id.linear3).setVisibility(View.GONE);
        }
    }
}
