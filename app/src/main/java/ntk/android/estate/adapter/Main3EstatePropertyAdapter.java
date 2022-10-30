package ntk.android.estate.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstateContractModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.utill.FontManager;
import ntk.android.base.view.NViewUtils;
import ntk.android.estate.R;
import ntk.android.estate.activity.EstateDetailActivity;

public class Main3EstatePropertyAdapter extends BaseRecyclerAdapter<EstatePropertyModel, Main3EstatePropertyAdapter.VH> {
    private int imageSize = -1;
    int width;

    public Main3EstatePropertyAdapter(List<EstatePropertyModel> list) {
        super(list);
        width = ITEM_WIDTH();
        drawable = R.drawable.sweet_error_center_x;
    }

    public static int ITEM_WIDTH() {
        int w = getScreenWidth();
        var width = w / 2 - (w / 15);
        return width;
    }

    public static int IMAGE_WIDTH(Context c) {
        return ITEM_WIDTH() - NViewUtils.dpToPx(c, 16);
    }

    @NonNull
    @Override
    public Main3EstatePropertyAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(parent, R.layout.row_recycler_main3_estateproperty);
        v.getLayoutParams().width = width;
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Main3EstatePropertyAdapter.VH holder, int position) {
        EstatePropertyModel item = getItem(position);
        loadImage(item.LinkMainImageIdSrc, holder.image);
        holder.title.setText(item.Title);
        holder.setContract(item);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), EstateDetailActivity.class);
            intent.putExtra(Extras.EXTRA_FIRST_ARG, item.Id);
            intent.putExtra(Extras.EXTRA_SECOND_ARG, item.Title);
            view.getContext().startActivity(intent);
        });
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView title;
        TextView price1;
        TextView price2;
        TextView priceTitle1;
        ImageView favorite;
        ImageView image;

        public VH(@NonNull View itemView) {
            super(itemView);
            creating();
        }

        protected void creating() {
            image = itemView.findViewById(R.id.image);
            if (imageSize == -1)
                imageSize = IMAGE_WIDTH(itemView.getContext());
            image.getLayoutParams().width = imageSize;
            image.getLayoutParams().height = imageSize;
            favorite = itemView.findViewById(R.id.imgFavorite);
            title = itemView.findViewById(R.id.title);
            price1 = itemView.findViewById(R.id.txtPrice1);
            price2 = itemView.findViewById(R.id.txtPrice2);
            priceTitle1 = itemView.findViewById(R.id.txtPrice1Title1);
            setFont();
        }

        private void setFont() {
            Typeface bold = FontManager.T1_BOLD_Typeface(itemView.getContext());
            Typeface req = FontManager.T1_Typeface(itemView.getContext());
            title.setTypeface(bold);
            price1.setTypeface(req);
            price2.setTypeface(req);
            priceTitle1.setTypeface(req);
        }


        public void setContract(EstatePropertyModel item) {
            price1.setVisibility(View.VISIBLE);
            price2.setVisibility(View.VISIBLE);

            for (EstateContractModel m :
                    item.Contracts) {

                priceTitle1.setText(m.ContractType.TitleML + " :");
                //for rahn ejare
                if (m.ContractType.HasDepositPrice && m.ContractType.HasRentPrice) {
                    String price = "";
                    if (m.DepositPrice != null || m.DepositPriceByAgreement) {
                        if (m.DepositPrice != null && m.DepositPrice != 0)
                            price = (NViewUtils.PriceFormat(m.DepositPrice) + "  " + m.UnitSalePrice);
                        if (m.DepositPriceByAgreement)
                            price = (price.isEmpty() ? "توافقی" : price + "||" + " توافقی");
                        price1.setText(price);
                    } else price1.setText("وارد نشده");
                    price = "";
                    if (m.RentPrice != null || m.RentPriceByAgreement) {
                        if (m.RentPrice != null && m.RentPrice != 0)
                            price = (NViewUtils.PriceFormat(m.RentPrice) + "  " + m.UnitSalePrice);
                        if (m.RentPriceByAgreement)
                            price = (price.isEmpty() ? "توافقی" : price + "||" + " توافقی");
                        price2.setText(price);
                    } else {
                        price2.setText("وارد نشده");
                    }
                } else if (m.ContractType.HasPeriodPrice) {
                    String price = "";
                    if (m.PeriodPrice != null || m.PeriodPriceByAgreement) {
                        if (m.PeriodPrice != null && m.PeriodPrice != 0)
                            price = (NViewUtils.PriceFormat(m.PeriodPrice) + "  " +m.UnitSalePrice);
                        if (m.DepositPriceByAgreement)
                            price = (price.isEmpty() ? "توافقی" : price + "||" + " توافقی");
                        price1.setText(price);
                    } else price1.setText("وارد نشده");
                } else {
                    price2.setVisibility(View.INVISIBLE);
                    if (m.ContractType.HasDepositPrice) {
                        String price = "";
                        if (m.DepositPrice != null || m.DepositPriceByAgreement) {
                            if (m.DepositPrice != null && m.DepositPrice != 0)
                                price = (NViewUtils.PriceFormat(m.DepositPrice) + "  " + m.UnitSalePrice);
                            if (m.DepositPriceByAgreement)
                                price = (price.isEmpty() ? "توافقی" : price + "||" + " توافقی");
                            price1.setText(price);
                        } else price1.setText("وارد نشده");
                    }
                    if (m.ContractType.HasRentPrice) {
                        String price = "";
                        if (m.RentPrice != null || m.RentPriceByAgreement) {
                            if (m.RentPrice != null && m.RentPrice != 0)
                                price = (NViewUtils.PriceFormat(m.RentPrice) + "  " + m.UnitSalePrice);
                            if (m.RentPriceByAgreement)
                                price = (price.isEmpty() ? "توافقی" : price + "||" + " توافقی");
                            price1.setText(price);
                        } else price1.setText("وارد نشده");
                    }
                    if (m.ContractType.HasSalePrice) {
                        String price = "";
                        if (m.SalePrice != null || m.SalePriceByAgreement) {
                            if (m.SalePrice != null && m.SalePrice != 0)
                                price = (NViewUtils.PriceFormat(m.SalePrice) + "  " + m.UnitSalePrice);
                            if (m.SalePriceByAgreement)
                                price = (price.isEmpty() ? "توافقی" : price + "||" + " توافقی");
                            price1.setText(price);
                        } else price1.setText("وارد نشده");
                    }
                }
            }
        }


    }
}