package ntk.android.estate.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import es.dmoral.toasty.Toasty;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.estate.EstateCustomerOrderModel;
import ntk.android.base.services.estate.EstateCustomerOrderService;
import ntk.android.base.utill.FontManager;
import ntk.android.base.view.dialog.SweetAlertDialog;
import ntk.android.estate.R;
import ntk.android.estate.activity.EditCustomerOrderActivity;
import ntk.android.estate.activity.EstateListWithOrderIdActivity;

public class EstateCustomerOrderAdapter extends BaseRecyclerAdapter<EstateCustomerOrderModel, EstateCustomerOrderAdapter.VH> {
    public EstateCustomerOrderAdapter(List<EstateCustomerOrderModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_recycler_estate_customer_order));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstateCustomerOrderModel item = getItem(position);
        if (item.Title != null) holder.title.setText(item.Title);
        else holder.title.setText("بدون عنوان");
        holder.see.setOnClickListener(v -> {
            EstateListWithOrderIdActivity.START_NEW(v.getContext(), item);
        });
        holder.edit.setOnClickListener(v -> {
            EditCustomerOrderActivity.NEW(v.getContext(), item);
        });
        holder.delete.setOnClickListener(v -> {
            Context c = v.getContext();
            SweetAlertDialog dialog = new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setTitle("هشدار");
            dialog.setContentText("آیا از حذف مورد " + "\"" + item.Title + "\"" + " اطمینان دارید ؟");
            dialog.setConfirmButton("بلی", d -> {
                ServiceExecute.execute(new EstateCustomerOrderService(c).delete(item.Id)).subscribe(new NtkObserver<ErrorException<EstateCustomerOrderModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<EstateCustomerOrderModel> response) {
                        if (response.IsSuccess) {
                            dialog.dismiss();
                            Toasty.success(c, "سفارش شماحذف گردید").show();
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
        });
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title;
        MaterialButton see;
        MaterialButton edit;
        MaterialButton delete;

        public VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            see = itemView.findViewById(R.id.see);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            Typeface t1 = FontManager.T1_Typeface(itemView.getContext());
            title.setTypeface(t1);
            see.setTypeface(t1);
            edit.setTypeface(t1);
            delete.setTypeface(t1);
        }
    }
}
