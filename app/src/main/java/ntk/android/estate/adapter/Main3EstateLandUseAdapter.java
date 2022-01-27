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
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.EstateListActivity;

public class Main3EstateLandUseAdapter extends BaseRecyclerAdapter<EstatePropertyTypeLanduseModel, Main3EstateLandUseAdapter.VH> {
    public Main3EstateLandUseAdapter(List<EstatePropertyTypeLanduseModel> list) {
        super(list); drawable=R.drawable.sweet_error_center_x;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.main3_estate_type));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstatePropertyTypeLanduseModel item = getItem(position);
        loadImage(item.LinkMainImageIdSrc,holder.image);
        holder.title.setText(item.Title);
        holder.itemView.setOnClickListener(view -> EstateListActivity.START_NEW(view.getContext(),item));
    }

    public class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgPhoto);
            title = itemView.findViewById(R.id.title);
            title.setTypeface(FontManager.T1_Typeface(itemView.getContext()));
        }
    }
}

