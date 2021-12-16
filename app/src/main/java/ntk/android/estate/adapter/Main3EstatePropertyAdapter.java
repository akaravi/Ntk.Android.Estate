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
import ntk.android.base.view.NViewUtils;
import ntk.android.estate.R;

public class Main3EstatePropertyAdapter extends BaseRecyclerAdapter<EstatePropertyModel, Main3EstatePropertyAdapter.VH> {
    private int imageSize = -1;
    int width;

    public Main3EstatePropertyAdapter(List<EstatePropertyModel> list) {
        super(list);
        int w = getScreenWidth();
        width = w / 2 - (w / 15);
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
        loadImage(getItem(position).LinkMainImageIdSrc, holder.image);
        holder.title.setText(getItem(position).Title);
        holder.setContract(getItem(position));
    }

    public class VH extends EstatePropertyAdapter.VH{


        public VH(@NonNull View itemView) {
            super(itemView);


        }

        @Override
        protected void creating() {
            image = itemView.findViewById(R.id.image);
            if (imageSize == -1)
                imageSize = width - NViewUtils.dpToPx(itemView.getContext(), 8);
            image.getLayoutParams().width = imageSize;
            image.getLayoutParams().height = imageSize;
            favorite = itemView.findViewById(R.id.imgFavorite);
            title = itemView.findViewById(R.id.title);
            price1 = itemView.findViewById(R.id.txtPrice1);
            price2 = itemView.findViewById(R.id.txtPrice2);
            price3 = itemView.findViewById(R.id.txtPrice3);
            priceTitle1 = itemView.findViewById(R.id.txtPrice1Title1);
            priceTitle2 = itemView.findViewById(R.id.txtPriceTitle2);
            priceTitle3 = itemView.findViewById(R.id.txtPriceTitle3);
        }
    }
}
