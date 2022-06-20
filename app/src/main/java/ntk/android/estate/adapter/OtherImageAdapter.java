package ntk.android.estate.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.estate.R;

public class OtherImageAdapter extends BaseRecyclerAdapter<String, OtherImageAdapter.ViewHolder> {

    List<String> src;

    public OtherImageAdapter(List<String> src, List<String> list) {
        super(list);
        this.src = src;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent, R.layout.item_other_image));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(getItem(position), holder.image);
        holder.delete.setOnClickListener(view -> {
            src.remove(position);
            list.remove(position);
            notifyItemRemoved(position);
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public View delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.selectedImageView);
            delete = itemView.findViewById(R.id.deleteImage);
        }
    }
}