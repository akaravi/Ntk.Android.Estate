package ntk.android.estate.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.utill.FontManager;
import ntk.android.base.view.NViewUtils;
import ntk.android.estate.R;
import ntk.android.estate.activity.EstateDetailActivity;

public class Main3EstatePropertyAdapter extends BaseRecyclerAdapter<EstatePropertyModel, Main3EstatePropertyAdapter.VH> {
    private int imageSize = -1;
    int width;

    public Main3EstatePropertyAdapter(List<EstatePropertyModel> list) {
        super(list);
        width = ITEM_WIDTH();
        drawable=R.drawable.sweet_error_center_x;
    }

    public static int ITEM_WIDTH() {
        int w = getScreenWidth();
        var width = w / 2 - (w / 15);
        return width;
    }
    public static int IMAGE_WIDTH(Context c) {
        return ITEM_WIDTH() - NViewUtils.dpToPx(c, 8);
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
        EstatePropertyModel item = getItem(position);
        loadImage(item.LinkMainImageIdSrc, holder.image);
        holder.title.setText(item.Title);
        holder.setContract(item);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), EstateDetailActivity.class);
            intent.putExtra(Extras.EXTRA_FIRST_ARG, item.Id);
            intent.putExtra(Extras.EXTRA_SECOND_ARG, item.Title);
            view.getContext().startActivity(intent);
        });
    }

    public class VH extends EstatePropertyAdapter.VH {


        public VH(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void creating() {
            image = itemView.findViewById(R.id.image);
            if (imageSize == -1)
                imageSize = IMAGE_WIDTH(itemView.getContext());
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
            setFont();
        }

        private void setFont() {
            Typeface bold = FontManager.T1_BOLD_Typeface(itemView.getContext());
            Typeface req = FontManager.T1_Typeface(itemView.getContext());
            title.setTypeface(bold);
            price1.setTypeface(req);
            price2.setTypeface(req);
            price3.setTypeface(req);
            priceTitle1.setTypeface(req);
            priceTitle2.setTypeface(req);
            priceTitle3.setTypeface(req);
        }
    }


}
