package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstateCompanyModel;
import ntk.android.base.entitymodel.estate.EstatePropertyProjectModel;
import ntk.android.estate.R;

public class ProjectAdapter extends BaseRecyclerAdapter<EstatePropertyProjectModel, ProjectAdapter.VH> {
    public ProjectAdapter(List<EstatePropertyProjectModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_project));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstatePropertyProjectModel item = getItem(position);
        loadImage(item.LinkMainImageIdSrc, holder.img);
        holder.title.setText(item.Title != null ? item.Title : "نا مشخص");

    }
    public class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;

        public VH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.lblTitle);
        }
    }

}
