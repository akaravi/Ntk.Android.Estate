package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import java9.util.stream.StreamSupport;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailValueModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class PropertyDetailGroupsAdapter extends BaseRecyclerAdapter<EstatePropertyDetailGroupModel, PropertyDetailGroupsAdapter.VH> {
    public PropertyDetailGroupsAdapter(List<EstatePropertyDetailGroupModel> list) {
        super(list);
        drawable = R.drawable.sweet_error_center_x;
    }

    public static RecyclerView.Adapter INIT(List<EstatePropertyDetailGroupModel> details, List<EstatePropertyDetailValueModel> values) {
        StreamSupport.stream(details).
                forEach(estatePropertyDetailGroupModel -> {

                    StreamSupport.stream(estatePropertyDetailGroupModel.PropertyDetails)
                            .forEach(estatePropertyDetailModel -> {
                                EstatePropertyDetailValueModel estatePropertyDetailValueModel = StreamSupport.stream(values).filter(valueModel -> valueModel.LinkPropertyDetailId.equals(estatePropertyDetailModel.Id)).findFirst().orElse(null);
                                estatePropertyDetailModel.Value = (estatePropertyDetailValueModel != null ? estatePropertyDetailValueModel.Value : null);
                            });
                });
        return new PropertyDetailGroupsAdapter(details);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_property_detail_group));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstatePropertyDetailGroupModel item = getItem(position);
        holder.title.setText(item.Title);
        holder.rc.setAdapter(new PropertyDetailValueAdapter(item.PropertyDetails));
        holder.rc.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), 2));
        if (position == list.size() - 1)
            holder.line.setVisibility(View.GONE);
        else
            holder.line.setVisibility(View.VISIBLE);
        if (item.PropertyDetails.isEmpty())
            holder.itemView.setVisibility(View.GONE);
        else {
            boolean isNull = true;
            for (EstatePropertyDetailModel detail : item.PropertyDetails) {
                if (detail.Value != null && detail.equals("false")) {
                    isNull = true;
                    break;
                }
            }
            if (isNull)
                holder.itemView.setVisibility(View.VISIBLE);
        }
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView rc;
        View line;

        public VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.propertyGroupTitle);
            title.setTypeface(FontManager.T1_Typeface(itemView.getContext()));
            rc = itemView.findViewById(R.id.detailsValues);
            line = itemView.findViewById(R.id.lineImage);
        }

    }
}
