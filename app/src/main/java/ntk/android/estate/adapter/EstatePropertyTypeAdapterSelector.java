package ntk.android.estate.adapter;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

import java8.util.function.Consumer;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeUsageModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class EstatePropertyTypeAdapterSelector extends BaseRecyclerAdapter<EstatePropertyTypeUsageModel, EstatePropertyTypeAdapterSelector.VH> {
    int lastSelected;
    Consumer<EstatePropertyTypeUsageModel> caller;
    boolean Clickable=true;

    public EstatePropertyTypeAdapterSelector(List<EstatePropertyTypeUsageModel> listItems, EstatePropertyTypeUsageModel Item, Consumer<EstatePropertyTypeUsageModel> detailCaller) {
        super(listItems);
        caller = detailCaller;
        lastSelected = -1;
        if (Item != null) {
            lastSelected = listItems.indexOf(Item);
        }
        drawable = R.drawable.sweet_error_center_x;
    }

    public EstatePropertyTypeAdapterSelector(boolean canClick, List<EstatePropertyTypeUsageModel> listItems, EstatePropertyTypeUsageModel Item, Consumer<EstatePropertyTypeUsageModel> detailCaller) {
        super(listItems);
        Clickable = canClick;
        caller = detailCaller;
        lastSelected = -1;
        if (Item != null) {
            lastSelected = listItems.indexOf(Item);
        }
        drawable = R.drawable.sweet_error_center_x;
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
        holder.title.setChecked(position == lastSelected);
        holder.title.setTag(position);
        holder.title.setSelected(position == lastSelected);
        if (Clickable) {
            holder.title.setOnClickListener(view -> {
                int copyOfLastCheckedPosition = lastSelected;
                lastSelected = ((Integer) view.getTag());
                if (lastSelected != copyOfLastCheckedPosition) {
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(lastSelected);
                } else
                    ((Chip) view).setChecked(true);
                caller.accept(item);
            });
        }   else
            holder.title.setEnabled(false);
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
