package ntk.android.estate.adapter;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import java9.util.stream.StreamSupport;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstateActivityTypeModel;
import ntk.android.base.entitymodel.estate.EstatePropertyHistoryModel;
import ntk.android.base.utill.AppUtil;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class EstateHistoryAdapter extends BaseRecyclerAdapter<EstatePropertyHistoryModel, EstateHistoryAdapter.VH> {
    List<EstateActivityTypeModel> activityTypes;

    public EstateHistoryAdapter(List<EstatePropertyHistoryModel> list, List<EstateActivityTypeModel> activityTypes) {
        super(list);
        this.activityTypes = activityTypes;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_recycler_estate_history));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstatePropertyHistoryModel item = getItem(position);
        holder.title.setText(item.Title);
        holder.activityType.setText(activityType(item.LinkActivityTypeId));
        holder.description.setText(item.Description);
        if (item.AppointmentDateFrom == null || item.AppointmentDateFrom.equals("")) {
            holder.tvFrom.setVisibility(View.GONE);
            holder.fromDate.setVisibility(View.GONE);
        } else {
            holder.tvFrom.setVisibility(View.VISIBLE);
            holder.fromDate.setVisibility(View.VISIBLE);
            holder.fromDate.setText(AppUtil.GregorianToPersian(item.AppointmentDateFrom));
        }
        if (item.AppointmentDateTo == null || item.AppointmentDateTo.equals("")) {
            holder.tvTo.setVisibility(View.GONE);
            holder.toDate.setVisibility(View.GONE);
        } else {
            holder.tvTo.setVisibility(View.VISIBLE);
            holder.toDate.setVisibility(View.VISIBLE);
            holder.toDate.setText(AppUtil.GregorianToPersian(item.AppointmentDateTo));
        }

    }

    private String activityType(String linkActivityTypeId) {
        EstateActivityTypeModel estateActivityTypeModel = StreamSupport.stream(activityTypes).filter(activityTypeModel -> activityTypeModel.Id.equals(linkActivityTypeId)).findFirst().orElse(null);
        return estateActivityTypeModel != null ? estateActivityTypeModel.TitleML : "نامشخص";
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title;
        TextView activityType;
        TextView toDate;
        TextView fromDate;
        TextView description;
        TextView tvFrom;
        TextView tvTo;

        public VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.TitleTv);
            activityType = itemView.findViewById(R.id.ActivityTypeTv);
            toDate = itemView.findViewById(R.id.toDateTv);
            fromDate = itemView.findViewById(R.id.fromDateTv);
            description = itemView.findViewById(R.id.DescriptionTv);
            tvFrom = itemView.findViewById(R.id.fromTv);
            tvTo = itemView.findViewById(R.id.toTv);
            Typeface t = FontManager.T1_Typeface(itemView.getContext());
            title.setTypeface(t);
            activityType.setTypeface(t);
            toDate.setTypeface(t);
            fromDate.setTypeface(t);
            description.setTypeface(t);
            tvFrom.setTypeface(t);
            tvTo.setTypeface(t);
        }
    }
}
