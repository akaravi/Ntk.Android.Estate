package ntk.android.estate.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.functions.Function;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;

public class OtherImageAdapter extends BaseRecyclerAdapter<String, OtherImageAdapter.ViewHolder> {
    List<String> src;
    NewEstateActivity activity;
    public OtherImageAdapter(NewEstateActivity act, List<String> src, List<String> list) {
        super(list);
        this.src = src;
        activity=act;

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

            if (activity.isUploaded()) {
                src.remove(position);
                list.remove(position);
                notifyItemRemoved(position);
            }else    Toasty.info(view.getContext(), "فایل انتخابی قبلی در حال بارگزاری است...", Toasty.LENGTH_LONG).show();
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