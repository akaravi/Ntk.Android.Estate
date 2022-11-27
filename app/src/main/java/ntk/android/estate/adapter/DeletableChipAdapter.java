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
import ntk.android.base.entitymodel.core.CoreLocationModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class DeletableChipAdapter extends BaseRecyclerAdapter<CoreLocationModel, DeletableChipAdapter.VH> {
    Consumer<CoreLocationModel> caller;
    public DeletableChipAdapter(List<CoreLocationModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public DeletableChipAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.deleteabl_chip_row_item));
    }

    @Override
    public void onBindViewHolder(@NonNull DeletableChipAdapter.VH holder, int position) {
        CoreLocationModel item = getItem(position);
        holder.title.setText(item.Title);
        holder.title.setTag(position);

        holder.title.setOnCloseIconClickListener(view -> {
//                if (lastSelected != copyOfLastCheckedPosition) {
//                    notifyItemChanged(copyOfLastCheckedPosition);
//                    notifyItemChanged(lastSelected);
//                } else
//                    ((Chip) view).setChecked(true);
//                caller.accept(item);
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
