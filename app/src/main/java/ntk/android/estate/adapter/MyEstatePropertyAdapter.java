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

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ntk.android.base.Extras;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstateContractModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.utill.AppUtil;
import ntk.android.base.utill.FontManager;
import ntk.android.base.view.NViewUtils;
import ntk.android.base.view.dialog.SweetAlertDialog;
import ntk.android.estate.R;
import ntk.android.estate.activity.EditEstateActivity;
import ntk.android.estate.activity.EstateDetailActivity;
import ntk.android.estate.activity.EstateHistoryActivity;

public class MyEstatePropertyAdapter extends BaseRecyclerAdapter<EstatePropertyModel, MyEstatePropertyAdapter.VH> {
    Date now;

    public MyEstatePropertyAdapter(List<EstatePropertyModel> models) {
        super(models);
        now = AppUtil.Now();
        drawable = R.drawable.sweet_error_center_x;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_recycler_my_estateproperty));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstatePropertyModel item = getItem(position);
        holder.setContract(item);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), EstateDetailActivity.class);
            intent.putExtra(Extras.EXTRA_FIRST_ARG, item.Id);
            intent.putExtra(Extras.EXTRA_SECOND_ARG, item.Title);
            view.getContext().startActivity(intent);
        });
        holder.delete.setOnClickListener(view -> {
            showDelete(view.getContext(), item);
        });
        holder.history.setOnClickListener(view -> {
            showHistory(view.getContext(), item);
        });
        holder.share.setOnClickListener(view -> {
            showShare(view.getContext(), item);
        });
        holder.edit.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), EditEstateActivity.class);
            i.putExtra(Extras.EXTRA_FIRST_ARG, (item.Id));
            view.getContext().startActivity(i);
        });
        holder.title.setText(item.Title);
        holder.date.setText(AppUtil.DateDifference(item.CreatedDate, now));
        loadImage(item.LinkMainImageIdSrc, holder.image);
        holder.setPictureCount(item);
        holder.setProperties(item);
        holder.location.setText(item.LinkLocationIdTitle);
        holder.locationParent.setText(item.LinkLocationIdParentTitle);
        if (item.IsFavorite)
            holder.favorite.setImageResource(R.drawable.ic_fav_full);
        else
            holder.favorite.setImageResource(R.drawable.ic_fav);
    }

    private void showShare(Context context, EstatePropertyModel item) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String message = item.Title;
        shareIntent.putExtra(Intent.EXTRA_TEXT, message + "\n\n\n" + context.getString(ntk.android.base.R.string.app_name) + "\n" + context.getString(R.string.per_view_link) + "\n" + item.UrlViewContent);
        shareIntent.setType("text/txt");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, context.getString(ntk.android.base.R.string.per_share_to)));
    }

    private void showHistory(Context context, EstatePropertyModel item) {
        Intent i = new Intent(context, EstateHistoryActivity.class);
        FilterModel f = new FilterModel();
        f.addFilter(new FilterDataModel().setPropertyName("LinkPropertyId").setStringValue(item.Id));
        i.putExtra(Extras.EXTRA_FIRST_ARG, new Gson().toJson(f));
        context.startActivity(i);
    }

    private void showDelete(Context c, EstatePropertyModel item) {
        SweetAlertDialog dialog = new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE);
        dialog.setTitle("هشدار");
        dialog.setContentText("آیا از حذف مورد " + "\"" + item.Title + "\"" + " اطمینان دارید ؟");
        dialog.setConfirmButton("بلی", d -> {
            ServiceExecute.execute(new EstatePropertyService(c).delete(item.Id)).subscribe(new NtkObserver<ErrorException<EstatePropertyModel>>() {
                @Override
                public void onNext(@NonNull ErrorException<EstatePropertyModel> response) {
                    if (response.IsSuccess) {
                        dialog.dismiss();
                        Toasty.success(c, "ملک شماحذف گردید").show();
                        int pos = list.indexOf(item);
                        list.remove(item);
                        notifyItemRemoved(pos);
                    } else {
                        Toasty.error(c, "هنگام حذف خطا رخ داد مجددا تلاش نمایید" + "\n+" +
                                response.ErrorMessage).show();
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Toasty.error(c, " خطا رخ داد مجددا تلاش نمایید").show();

                }
            });
        });
        dialog.setCancelButton("خیر", SweetAlertDialog::dismiss);
        dialog.show();
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView title;
        TextView location;
        TextView locationParent;
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
        MaterialButton delete;
        MaterialButton edit;
        MaterialButton share;
        MaterialButton history;

        public VH(@NonNull View itemView) {
            super(itemView);
            creating();
        }

        protected void creating() {
            title = itemView.findViewById(R.id.txtTitle);
            location = itemView.findViewById(R.id.txtArea);
            locationParent = itemView.findViewById(R.id.txtAreaParent);
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
            delete = itemView.findViewById(R.id.deleteEstate);
            edit = itemView.findViewById(R.id.editEstate);
            share = itemView.findViewById(R.id.share);
            history = itemView.findViewById(R.id.history);

            setFont();
        }

        private void setFont() {
            Typeface bold = FontManager.T1_BOLD_Typeface(itemView.getContext());
            Typeface req = FontManager.T1_Typeface(itemView.getContext());
            title.setTypeface(bold);
            property1.setTypeface(req);
            property1.setTypeface(req);
            property3.setTypeface(req);
            location.setTypeface(req);
            locationParent.setTypeface(req);
            date.setTypeface(req);
            pictureCount.setTypeface(bold);
            price1.setTypeface(req);
            price2.setTypeface(req);
            price3.setTypeface(req);
            priceTitle1.setTypeface(req);
            priceTitle2.setTypeface(req);
            priceTitle3.setTypeface(req);
            delete.setTypeface(req);
            edit.setTypeface(req);
            share.setTypeface(req);
            history.setTypeface(req);
        }

        public void setContract(EstatePropertyModel item) {
            for (EstateContractModel m :
                    item.Contracts) {

                if (m.ContractType.HasSalePrice) {
                    priceTitle1.setText(m.ContractType.TitleML + " :");
                    if (m.SalePrice != null || m.SalePriceByAgreement) {
                        priceTitle1.setVisibility(View.VISIBLE);
                        price1.setVisibility(View.VISIBLE);
                        String price = "";
                        if (m.SalePrice != null && m.SalePrice != 0)
                            price = NViewUtils.PriceFormat(m.SalePrice) + "  " + m.UnitSalePrice;
                        if (m.SalePriceByAgreement)
                            price = (price.isEmpty() ? "توافقی" : price + "||" + " توافقی");
                        price1.setText(price);
                    } else {
                        priceTitle1.setVisibility(View.VISIBLE);
                        priceTitle1.setText("جهت :" + m.ContractType.TitleML);
                    }
                } else {
                    priceTitle1.setVisibility(View.GONE);
                    price1.setVisibility(View.GONE);

                }
                if (m.ContractType.HasDepositPrice) {
                    priceTitle2.setText(m.ContractType.TitleML + " :");
                    if (m.DepositPrice != null || m.DepositPriceByAgreement) {
                        priceTitle2.setVisibility(View.VISIBLE);
                        price2.setVisibility(View.VISIBLE);
                        String price = "";
                        if (m.DepositPrice != null && m.DepositPrice != 0)
                            price = (NViewUtils.PriceFormat(m.DepositPrice) + "  " + m.UnitSalePrice);
                        if (m.DepositPriceByAgreement)
                            price = (price.isEmpty() ? "توافقی" : price + "||" + " توافقی");
                        price2.setText(price);
                    } else {
                        priceTitle2.setVisibility(View.VISIBLE);
                        priceTitle2.setText("جهت :" + m.ContractType.TitleML);
                    }
                } else {
                    priceTitle2.setVisibility(View.GONE);
                    price2.setVisibility(View.GONE);

                }
                if (m.ContractType.HasRentPrice) {
                    priceTitle3.setText(m.ContractType.TitleML + " :");
                    if (m.RentPrice != null || m.RentPriceByAgreement) {
                        priceTitle3.setVisibility(View.VISIBLE);
                        price3.setVisibility(View.VISIBLE);
                        String price = "";
                        if (m.RentPrice != null && m.RentPrice != 0)
                            price = (NViewUtils.PriceFormat(m.RentPrice) + "  " + m.UnitSalePrice);
                        if (m.RentPriceByAgreement)
                            price = (price.isEmpty() ? "توافقی" : price + "||" + " توافقی");
                        price3.setText(price);
                    } else {
                        priceTitle3.setVisibility(View.VISIBLE);
                        priceTitle3.setText("جهت :" + m.ContractType.TitleML);
                    }
                } else {
                    priceTitle3.setVisibility(View.GONE);
                    price3.setVisibility(View.GONE);
                }

            }
        }

        public void setPictureCount(EstatePropertyModel item) {
            if (item.LinkExtraImageIdsSrc != null && !item.LinkExtraImageIdsSrc.isEmpty()) {
                pictureCount.setText("" + item.LinkExtraImageIdsSrc.size());
                pictureCount.setVisibility(View.VISIBLE);
            } else
                pictureCount.setVisibility(View.GONE);
        }

        public void setProperties(EstatePropertyModel item) {
            if (item.PropertyTypeLanduse != null)
                property1.setText(item.PropertyTypeLanduse.Title);
            else
                property1.setText("");
            if (item.PropertyTypeLanduse == null
                    || item.PropertyTypeLanduse.TitlePartition.equals("")
                    || item.PropertyTypeLanduse.TitlePartition.equals("---"))
                property2.setVisibility(View.GONE);
            else {
                property2.setText(item.PropertyTypeLanduse.TitlePartition + " : " + item.Partition);
                property2.setVisibility(View.VISIBLE);
            }
            if (item.Area != 0) {
                property3.setVisibility(View.VISIBLE);
                property3.setText(item.Area + " متر ");
            } else {
                property3.setVisibility(View.GONE);

            }
        }
    }
}
