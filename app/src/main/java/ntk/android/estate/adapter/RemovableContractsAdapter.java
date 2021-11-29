package ntk.android.estate.adapter;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstateContractModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class RemovableContractsAdapter extends BaseRecyclerAdapter<EstateContractModel, RemovableContractsAdapter.VH> {
    public RemovableContractsAdapter(List<EstateContractModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_property_contract_edit));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.setData(getItem(position));
        if (position==list.size()-1)
            holder.padding.setVisibility(View.VISIBLE);
        else
            holder.padding.setVisibility(View.GONE);
    }

    public class VH extends EstateContractAdapter.VH {
        MaterialButton delete;
        View padding;
        public VH(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            Typeface t1 = FontManager.T1_Typeface(itemView.getContext());
            delete.setTypeface(t1);
            padding=itemView.findViewById(R.id.paddingView);
//            Context c=itemView.getContext();
//            itemView.setBackgroundColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(c,R.color.colorAccent), 100));
        }

        @Override
        public void setData(EstateContractModel item) {
            super.setData(item);
            delete.setOnClickListener(view -> {
                list.remove(item);
                notifyDataSetChanged();
            });
        }
    }
}
