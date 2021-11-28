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
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class EstatePropertyLandUseAdapterSelector extends BaseRecyclerAdapter<EstatePropertyTypeLanduseModel, EstatePropertyLandUseAdapterSelector.VH> {
    private EstatePropertyTypeLanduseModel selectedItem;
    Consumer<EstatePropertyTypeLanduseModel> caller;

    public EstatePropertyLandUseAdapterSelector(List<EstatePropertyTypeLanduseModel> list,EstatePropertyTypeLanduseModel Item, Consumer<EstatePropertyTypeLanduseModel> selector) {
        super(list);
        caller = selector;
        selectedItem =Item;
        if (selectedItem==null){
            selectedItem=new EstatePropertyTypeLanduseModel();
            selectedItem.Id="";
        }

    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.chip_row_item));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstatePropertyTypeLanduseModel item = getItem(position);
        holder.title.setText(item.Title);
        holder.title.setChecked(selectedItem.Id.equals(item.Id));
        holder.title.setSelected(selectedItem.Id.equals(item.Id));
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
