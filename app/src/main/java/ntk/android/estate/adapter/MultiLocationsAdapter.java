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
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class MultiLocationsAdapter extends BaseRecyclerAdapter<String, MultiLocationsAdapter.VH> {
    Consumer<Integer> caller;
    List<Long> linkLocationIds;

    public MultiLocationsAdapter(List<String> locationTitles, List<Long> linkLocationIds, Consumer<Integer> consumer) {
        super(locationTitles);
        this.linkLocationIds = linkLocationIds;
        this.caller = consumer;
    }

    @NonNull
    @Override
    public MultiLocationsAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.deleteabl_chip_row_item));
    }

    @Override
    public void onBindViewHolder(@NonNull MultiLocationsAdapter.VH holder, int position) {
        String item = getItem(position);
        holder.title.setText(item);
        holder.title.setTag(position);

        holder.title.setOnCloseIconClickListener(view -> {

            caller.accept(list.indexOf(item));
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
