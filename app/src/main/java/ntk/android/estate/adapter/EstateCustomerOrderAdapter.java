package ntk.android.estate.adapter;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstateCustomerOrderModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
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
        if (item.Title != null)
            holder.title.setText(item.Title);
        else
            holder.title.setText("بدون عنوان");
        holder.see.setOnClickListener(v -> {
            EstateListWithOrderIdActivity.START_NEW(v.getContext(), item);
        });
        holder.edit.setOnClickListener(v -> {
        });
        holder.delete.setOnClickListener(v -> {
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
