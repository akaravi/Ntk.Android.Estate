package ntk.android.estate.adapter;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

import java9.util.function.Consumer;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstateContractModel;
import ntk.android.base.entitymodel.estate.EstateContractTypeModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class EstateContractAdapterSelector extends BaseRecyclerAdapter<EstateContractTypeModel, EstateContractAdapterSelector.VH> {
    private EstateContractTypeModel selectedItem;
    private Consumer<EstateContractTypeModel> caller;

    public EstateContractAdapterSelector(List<EstateContractTypeModel> list, Consumer<EstateContractTypeModel> selector) {
        super(list);
        selectedItem = new EstateContractTypeModel();
        caller = selector;
    }


    @NonNull
    @Override
    public EstateContractAdapterSelector.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EstateContractAdapterSelector.VH(inflate(parent, R.layout.chip_row_item));
    }

    @Override
    public void onBindViewHolder(@NonNull EstateContractAdapterSelector.VH holder, final int position) {
        EstateContractTypeModel item = getItem(position);
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
            Typeface typeface = FontManager.T1_Typeface(itemView.getContext());
            title.setTypeface(typeface);
        }

    }
}
