package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.estate.R;

class Main3EstatePropertyAdapter extends BaseRecyclerAdapter<EstatePropertyModel, Main3EstatePropertyAdapter.VH> {
    int width;

    public Main3EstatePropertyAdapter(List<EstatePropertyModel> list) {
        super(list);
        int w = getScreenWidth();
        width = w / 2 - (w / 10);
    }

    @NonNull
    @Override
    public Main3EstatePropertyAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(parent, R.layout.row_recycler_main3_estateproperty);
        v.getLayoutParams().width = width;
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Main3EstatePropertyAdapter.VH holder, int position) {

    }

    public class VH extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView favorite;
        TextView title;
        TextView price;
        TextView contract;

        public VH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            favorite = itemView.findViewById(R.id.imgFavorite);
            title = itemView.findViewById(R.id.title);
            contract = itemView.findViewById(R.id.contractTitle);
            price = itemView.findViewById(R.id.price);
        }
    }
}
