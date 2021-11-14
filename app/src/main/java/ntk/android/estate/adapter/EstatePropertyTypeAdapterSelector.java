package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

import java9.util.function.Consumer;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeUsageModel;
import ntk.android.estate.R;

public class EstatePropertyTypeAdapterSelector extends BaseRecyclerAdapter<EstatePropertyTypeUsageModel, EstatePropertyTypeAdapterSelector.VH> {
    private EstatePropertyTypeUsageModel selectedItem = new EstatePropertyTypeUsageModel();
    Consumer<EstatePropertyTypeUsageModel> caller;


    public EstatePropertyTypeAdapterSelector(List<EstatePropertyTypeUsageModel> listItems, Consumer<EstatePropertyTypeUsageModel> detailCaller) {
        super(listItems);
        caller = detailCaller;
    }


    @NonNull
    @Override
    public EstatePropertyTypeAdapterSelector.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.chip_row_item));
    }

    @Override
    public void onBindViewHolder(@NonNull EstatePropertyTypeAdapterSelector.VH holder, final int position) {
        EstatePropertyTypeUsageModel item = getItem(position);
        holder.title.setText(item.Title);
        holder.title.setChecked(selectedItem.equals(item));
        holder.title.setSelected(selectedItem.equals(item));
        holder.title.setOnClickListener(view -> {
            notifyItemChanged(list().indexOf(item));
            selectedItem = item;
            holder.title.setChecked(true);
            caller.accept(item);
            notifyDataSetChanged();
        });
    }

    public class VH extends RecyclerView.ViewHolder {
        Chip title;

        public VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.chip);
        }

    }
}
