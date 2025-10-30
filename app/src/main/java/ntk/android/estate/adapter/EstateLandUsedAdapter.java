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

public class EstateLandUsedAdapter extends BaseRecyclerAdapter<EstatePropertyTypeLanduseModel, EstateLandUsedAdapter.VH> {
    int width;

    public EstateLandUsedAdapter(List<EstatePropertyTypeLanduseModel> list) {
        super(list);
        int w = getScreenWidth();
        width = w/3 - (w / 12);
        drawable=ntk.android.base.R.drawable.sweet_error_center_x;
    }

    @NonNull
    @Override
    public EstateLandUsedAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EstateLandUsedAdapter.VH(inflate(parent, R.layout.row_property_landused));
    }

    @Override
    public void onBindViewHolder(@NonNull EstateLandUsedAdapter.VH holder, int position) {
        EstatePropertyTypeLanduseModel item = getItem(position);
        loadImage(item.LinkMainImageIdSrc, holder.image);
        holder.title.setText(item.Title);
        holder.itemView.setOnClickListener(view -> EstateListActivity.START_NEW(view.getContext(), item));
    }

    public class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgPhoto);
            image.getLayoutParams().width=width;
            image.getLayoutParams().height=width;
            title = itemView.findViewById(R.id.title);
            title.setTypeface(FontManager.T1_Typeface(itemView.getContext()));
        }
    }
}


