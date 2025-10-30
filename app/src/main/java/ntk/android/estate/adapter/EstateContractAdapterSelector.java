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
import ntk.android.base.entitymodel.estate.EstateContractTypeModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class EstateContractAdapterSelector extends BaseRecyclerAdapter<EstateContractTypeModel, EstateContractAdapterSelector.VH> {
    int lastSelected;
    private final Consumer<EstateContractTypeModel> caller;

    public EstateContractAdapterSelector(List<EstateContractTypeModel> list, Consumer<EstateContractTypeModel> selector) {
        super(list);
        lastSelected = -1;
        caller = selector;
        drawable=ntk.android.base.R.drawable.sweet_error_center_x;
    }
 public EstateContractAdapterSelector(int selected,List<EstateContractTypeModel> list, Consumer<EstateContractTypeModel> selector) {
        super(list);
        lastSelected = selected;
        caller = selector;
        drawable=ntk.android.base.R.drawable.sweet_error_center_x;
    }


    @NonNull
    @Override
    public EstateContractAdapterSelector.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EstateContractAdapterSelector.VH(inflate(parent, ntk.android.base.R.layout.chip_row_item));
    }

    @Override
    public void onBindViewHolder(@NonNull EstateContractAdapterSelector.VH holder, int position) {
        EstateContractTypeModel item = getItem(position);
        holder.title.setText(item.TitleML);
        holder.title.setChecked(position == lastSelected);
        holder.title.setTag(position);
        holder.title.setSelected(position == lastSelected);
        holder.title.setOnClickListener(view -> {
            int copyOfLastCheckedPosition = lastSelected;
            lastSelected = ((Integer) view.getTag());
            if (lastSelected != copyOfLastCheckedPosition) {
                notifyItemChanged(copyOfLastCheckedPosition);
                notifyItemChanged(lastSelected);
            }else
                ((Chip) view).setChecked(true);
            caller.accept(item);

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
