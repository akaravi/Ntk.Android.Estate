package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
import ntk.android.estate.R;

public class EstatePropertyDetailGroupAdapterSelector extends BaseRecyclerAdapter<EstatePropertyDetailGroupModel, EstatePropertyDetailGroupAdapterSelector.VH> {
    public EstatePropertyDetailGroupAdapterSelector(List<EstatePropertyDetailGroupModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.property_detail_group_selector));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstatePropertyDetailGroupModel item = getItem(position);
        holder.title.setText(item.Title);
//        holder.title.setChecked(selectedItem.equals(item));
//        holder.title.setSelected(selectedItem.equals(item));
    }

    public class VH extends RecyclerView.ViewHolder {

        Chip title;

        public VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.chip);
        }
    }
}
