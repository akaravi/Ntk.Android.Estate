package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstateContractModel;
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
    }

    public class VH extends EstateContractAdapter.VH {
        View delete;

        public VH(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
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
