package ntk.android.estate.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailValueModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class PropertyDetailValueAdapter extends BaseRecyclerAdapter<EstatePropertyDetailModel, PropertyDetailValueAdapter.VH> {
    public PropertyDetailValueAdapter(List<EstatePropertyDetailModel> list) {
        super(list); drawable=ntk.android.base.R.drawable.sweet_error_center_x;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_property_detail_value));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstatePropertyDetailModel item = getItem(position);
        holder.title.setText(item.Title);
        String iconFont = item.IconFont;
        holder.icon.setText("{" + iconFont.replace(iconFont.substring(0,iconFont.indexOf("-")+1), "faw-") + "}");
        if (item.IconColor != null)
            holder.icon.setTextColor(Color.parseColor(item.IconColor));
        if (item.Value.equalsIgnoreCase("true")) {
            holder.valueFont.setVisibility(View.VISIBLE);
            holder.value.setVisibility(View.GONE);
            holder.valueFont.setText("{faw-check-square}");
        } else if (item.Value.equalsIgnoreCase("false")) {
            holder.valueFont.setVisibility(View.VISIBLE);
            holder.value.setVisibility(View.GONE);
            holder.valueFont.setText("{faw-window-close}");
        } else {
            holder.valueFont.setVisibility(View.GONE);
            holder.value.setVisibility(View.VISIBLE);
            holder.value.setText(item.Value);
        }
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title;
        TextView value;
        TextView valueFont;
        TextView icon;

        public VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            value = itemView.findViewById(R.id.valueString);
            valueFont = itemView.findViewById(R.id.valueFont);
            icon = itemView.findViewById(R.id.fontIcon);
            title.setTypeface(FontManager.T1_Typeface(itemView.getContext()));
            value.setTypeface(FontManager.T1_Typeface(itemView.getContext()));
        }

    }

}
