package ntk.android.estate.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.EstateDetailActivity;
import ntk.android.estate.activity.EstateListActivity;
import ntk.android.estate.activity.MainActivity3;
import ntk.android.estate.models.RecyclerItemModel;

public class Main3EstateSpecialAdapter extends BaseRecyclerAdapter<RecyclerItemModel, Main3EstateSpecialAdapter.VH> {
    public Main3EstateSpecialAdapter(List<RecyclerItemModel> list) {
        super(list);
        drawable = R.drawable.sweet_error_center_x;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.main3_estate_special));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        RecyclerItemModel item = getItem(position);
        loadImage(item.Image, holder.image);
        holder.title.setText(item.Title);
        holder.itemView.setOnClickListener(view -> {
            if (item.Id != null && !item.Id.equals(""))
            {
                //see as estate
                Intent intent = new Intent(view.getContext(), EstateDetailActivity.class);
                intent.putExtra(Extras.EXTRA_FIRST_ARG, item.Id);
                intent.putExtra(Extras.EXTRA_SECOND_ARG, item.Title);
                view.getContext().startActivity(intent);
            }else if (item.Filter!=null){
                Intent intent = new Intent(view.getContext(), EstateListActivity.class);
                intent.putExtra(Extras.EXTRA_FIRST_ARG, new Gson().toJson(item.Filter));
                view.getContext().startActivity(intent);
            }
        });
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

