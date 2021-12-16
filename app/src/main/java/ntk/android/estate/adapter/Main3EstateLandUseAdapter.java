package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeUsageModel;
import ntk.android.estate.R;

public class Main3EstateLandUseAdapter extends BaseRecyclerAdapter<EstatePropertyTypeLanduseModel, Main3EstateLandUseAdapter.VH> {
    public Main3EstateLandUseAdapter(List<EstatePropertyTypeLanduseModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.main3_estate_type));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        loadImage(getItem(position).LinkMainImageIdSrc,holder.image);
        holder.title.setText(getItem(position).Title);
    }

    public class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgPhoto);
            title = itemView.findViewById(R.id.title);
        }
    }
}

