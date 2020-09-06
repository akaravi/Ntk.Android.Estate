package ntk.android.estate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import ntk.android.estate.R;
import ntk.android.estate.model.House;
import ntk.android.estate.utill.AppUtill;
import ntk.android.estate.utill.FontManager;

public class AdHouse extends RecyclerView.Adapter<AdHouse.ViewHolder> {

    private List<House> houses;
    private Context context;

    public AdHouse(Context context, List<House> list) {
        this.houses = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler_house, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Lbls.get(0).setText(houses.get(position).Title);
        holder.Lbls.get(1).setText(AppUtill.Convert(houses.get(position).Price) + "تومان");
        holder.Lbls.get(2).setText(houses.get(position).Address);
        holder.Lbls.get(4).setText(String.valueOf(houses.get(position).Rate));
        ImageLoader.getInstance().displayImage(houses.get(position).Photo, holder.Img);
        if ((position % 2) == 0) {
            holder.Overly.setBackgroundResource(R.drawable.gradianet_random_three_recycler);
        } else if ((position % 3) == 0) {
            holder.Overly.setBackgroundResource(R.drawable.gradianet_random_four_recycler);
        } else if (position == 0) {
            holder.Overly.setBackgroundResource(R.drawable.gradianet_random_one_recycler);
        } else {
            holder.Overly.setBackgroundResource(R.drawable.gradianet_random_two_recycler);
        }
    }

    @Override
    public int getItemCount() {
        return houses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.lblTitleRecyclerHouse,
                R.id.lblPriceRecyclerHouse,
                R.id.lblLocationRecyclerHouse,
                R.id.lblRateKeyRecyclerHouse,
                R.id.lblRateValueRecyclerHouse
        })
        List<TextView> Lbls;

        @BindView(R.id.imgRecyclerHouse)
        RoundedImageView Img;

        @BindView(R.id.OverlyRecyclerHouse)
        RelativeLayout Overly;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Lbls.get(0).setTypeface(FontManager.GetTypeface(context, FontManager.IranSansBold));
            Lbls.get(1).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Lbls.get(2).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Lbls.get(3).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Lbls.get(4).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        }
    }
}
