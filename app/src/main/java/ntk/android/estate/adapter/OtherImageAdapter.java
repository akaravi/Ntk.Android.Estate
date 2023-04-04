package ntk.android.estate.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewEstateActivity;

public class OtherImageAdapter extends BaseRecyclerAdapter<String, OtherImageAdapter.ViewHolder> {
    List<String> Ids;
    NewEstateActivity activity;

    public OtherImageAdapter(NewEstateActivity act, List<String> src, List<String> list) {
        super(list);
        this.Ids = src;
        activity = act;

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
                //remove id from ids uploaded before
                ArrayList<String> split = new ArrayList<String>(Arrays.asList(activity.model().LinkFileIds.split(",")));
                split.remove(Ids.get(position));
                activity.model().LinkFileIds = String.join(",", split);
                //remove src form srs before uploaded
                if (activity.model().LinkFileIdsSrc != null)
                    activity.model().LinkFileIdsSrc.remove(list.get(position));
                Ids.remove(position);
                list.remove(position);
                notifyItemRemoved(position);
            } else
                Toasty.info(view.getContext(), "فایل انتخابی قبلی در حال بارگزاری است...", Toasty.LENGTH_LONG).show();
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public MaterialButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.selectedImageView);
            delete = itemView.findViewById(R.id.deleteImage);
            delete.setTypeface(FontManager.T1_Typeface(itemView.getContext()));
        }
    }
}