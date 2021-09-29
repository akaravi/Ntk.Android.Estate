package ntk.android.estate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.estate.R;
import ntk.android.estate.activity.EstateDetailActivity;

public class EstatePropertyAdapter extends BaseRecyclerAdapter<EstatePropertyModel, EstatePropertyAdapter.VH> {
    public EstatePropertyAdapter(Context c, List<EstatePropertyModel> models) {
        super(models);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_recycler_estateproperty));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstatePropertyModel item = getItem(position);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), EstateDetailActivity.class);
            intent.putExtra(Extras.EXTRA_FIRST_ARG, item.Id);
            view.getContext().startActivity(intent);
        });
        holder.title.setText(item.Title);
        loadImage(item.LinkMainImageIdSrc,holder.image);

    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title;
        TextView property1;
        TextView property2;
        TextView property3;
        TextView date;
        TextView pictureCount;
        TextView price1;
        TextView price2;
        TextView priceTitle1;
        TextView priceTitle2;
        ImageView favorite;
        ImageView image;

        public VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            property1 = itemView.findViewById(R.id.txtProperty1);
            property2 = itemView.findViewById(R.id.txtProperty2);
            property3 = itemView.findViewById(R.id.txtProperty3);
            date = itemView.findViewById(R.id.txtDate);
            pictureCount = itemView.findViewById(R.id.txtCamera);
            price1 = itemView.findViewById(R.id.txtPrice1);
            price2 = itemView.findViewById(R.id.txtPrice2);
            priceTitle1 = itemView.findViewById(R.id.txtPrice1Title1);
            priceTitle2 = itemView.findViewById(R.id.txtPriceTitle2);
            favorite = itemView.findViewById(R.id.imgFavorite);
            image = itemView.findViewById(R.id.imgPhoto);
        }

    }
}
