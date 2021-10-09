package ntk.android.estate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.smarteist.autoimageslider.sliderItem;

import java.util.ArrayList;
import java.util.List;

import ntk.android.estate.R;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<sliderItem> mSliderItems = new ArrayList();

    public ImageSliderAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<sliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void renewUrls(List<String> sliderItems) {
        this.mSliderItems = new ArrayList<>();
        for (String u :
                sliderItems) {
            sliderItem a = new sliderItem();
            a.setImageUrl(u);
            a.setImageTitle("");
            mSliderItems.add(a);
        }
        notifyDataSetChanged();
    }

    public void addUrl(String url) {
        if (url != null & !url.trim().isEmpty()) {
            sliderItem s = new sliderItem().setImageTitle("").setImageUrl(url);
            this.mSliderItems.add(s);
            notifyDataSetChanged();
        }
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(sliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        sliderItem sliderItem = mSliderItems.get(position);

        viewHolder.textViewDescription.setText("");
        viewHolder.textViewDescription.setTextSize(16);
        viewHolder.textViewDescription.setTextColor(Color.WHITE);
        ImageLoader.getInstance().displayImage(sliderItem.getImageUrl(), viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(v -> {
            //todo call gallery
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}
