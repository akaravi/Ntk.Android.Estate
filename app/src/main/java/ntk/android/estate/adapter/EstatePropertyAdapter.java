package ntk.android.estate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstateContractModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.utill.AppUtill;
import ntk.android.estate.R;
import ntk.android.estate.activity.EstateDetailActivity;

public class EstatePropertyAdapter extends BaseRecyclerAdapter<EstatePropertyModel, EstatePropertyAdapter.VH> {
    Date now;

    public EstatePropertyAdapter(Context c, List<EstatePropertyModel> models) {
        super(models);
        now = AppUtill.Now();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_recycler_estateproperty));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstatePropertyModel item = getItem(position);
        holder.setContract(item);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), EstateDetailActivity.class);
            intent.putExtra(Extras.EXTRA_FIRST_ARG, item.Id);
            view.getContext().startActivity(intent);
        });
        holder.title.setText(item.Title);
        holder.date.setText(AppUtill.DateDifference(item.CreatedDate, now));
        loadImage(item.LinkMainImageIdSrc, holder.image);
        holder.setPictureCount(item);

    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title;
        TextView property1;
        TextView property2;
        TextView property3;
        TextView date;
        TextView pictureCount;
        TextView price1;
        TextView price2;
        TextView price3;
        TextView priceTitle1;
        TextView priceTitle2;
        TextView priceTitle3;
        ImageView favorite;
        ImageView image;

        public VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            property1 = itemView.findViewById(R.id.txtProperty1);
            property2 = itemView.findViewById(R.id.txtProperty2);
            property3 = itemView.findViewById(R.id.txtProperty3);
            date = itemView.findViewById(R.id.txtDate);
            pictureCount = itemView.findViewById(R.id.txtCamera);
            price1 = itemView.findViewById(R.id.txtPrice1);
            price2 = itemView.findViewById(R.id.txtPrice2);
            price3 = itemView.findViewById(R.id.txtPrice3);
            priceTitle1 = itemView.findViewById(R.id.txtPrice1Title1);
            priceTitle2 = itemView.findViewById(R.id.txtPriceTitle2);
            priceTitle3 = itemView.findViewById(R.id.txtPriceTitle3);
            favorite = itemView.findViewById(R.id.imgFavorite);
            image = itemView.findViewById(R.id.imgPhoto);
        }

        public void setContract(EstatePropertyModel item) {
            for (EstateContractModel m :
                    item.Contracts) {
                if (m.SalePrice != null || m.SalePriceByAgreement) {
                    priceTitle1.setVisibility(View.VISIBLE);
                    price1.setVisibility(View.VISIBLE);
                    priceTitle1.setText("فروش :  ");
                    if (m.SalePrice != null && m.SalePrice != 0)
                        price1.setText(m.SalePrice + "  " + m.UnitPrice);
                    if (m.SalePriceByAgreement)
                        price1.setText(price1.getText().toString().isEmpty() ? "توافقی" : price1.getText().toString() + "|| توافقی");
                } else {
                    priceTitle1.setVisibility(View.GONE);
                    price1.setVisibility(View.GONE);

                }
                if (m.RentPrice != null || m.RentPriceByAgreement) {
                    priceTitle2.setVisibility(View.VISIBLE);
                    price2.setVisibility(View.VISIBLE);
                    priceTitle2.setText("اچاره :  ");
                    if (m.RentPrice != null && m.RentPrice != 0)
                        price2.setText(m.RentPrice + "  " + m.UnitPrice);
                    if (m.RentPriceByAgreement)
                        price2.setText(price2.getText().toString().isEmpty() ? "توافقی" : price2.getText().toString() + "|| توافقی");
                } else {
                    priceTitle2.setVisibility(View.GONE);
                    price2.setVisibility(View.GONE);

                }
                if (m.DepositPrice != null || m.DepositPriceByAgreement) {
                    priceTitle3.setVisibility(View.VISIBLE);
                    price3.setVisibility(View.VISIBLE);
                    priceTitle3.setText("رهن :  ");
                    if (m.DepositPrice != null && m.DepositPrice != 0)
                        price3.setText(m.DepositPrice + "  " + m.UnitPrice);
                    if (m.DepositPriceByAgreement)
                        price3.setText(price3.getText().toString().isEmpty() ? "توافقی" : price3.getText().toString() + "|| توافقی");
                } else {
                    priceTitle3.setVisibility(View.GONE);
                    price3.setVisibility(View.GONE);

                }

            }
//            item.Contracts.
        }

        public void setPictureCount(EstatePropertyModel item) {
            if (item.LinkExtraImageIdsSrc != null && !item.LinkExtraImageIdsSrc.isEmpty()) {
                pictureCount.setText("" + item.LinkExtraImageIdsSrc.size());
                pictureCount.setVisibility(View.VISIBLE);
            } else
                pictureCount.setVisibility(View.GONE);
        }
    }
}
