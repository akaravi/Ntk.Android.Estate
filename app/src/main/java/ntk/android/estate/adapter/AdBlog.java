package ntk.android.estate.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.estate.R;
import ntk.android.estate.activity.ActDetailBlog;
import ntk.android.estate.utill.FontManager;
import ntk.base.api.blog.model.BlogContent;
import ntk.base.api.blog.model.BlogContentViewRequest;

public class AdBlog extends RecyclerView.Adapter<AdBlog.ViewHolder> {

    private List<BlogContent> arrayList;
    private Context context;

    public AdBlog(Context context, List<BlogContent> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler_blog, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.LblTitle.setText(arrayList.get(position).Title);
        holder.LblDescrption.setText(arrayList.get(position).description);
        holder.LblLike.setText(String.valueOf(arrayList.get(position).viewCount));
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).build();
        ImageLoader.getInstance().displayImage(arrayList.get(position).imageSrc, holder.Img, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.Progress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.Progress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        if (arrayList.get(position).ScoreSumPercent == 0) {
            holder.Rate.setRating(0);
        } else {
            holder.Rate.setRating((arrayList.get(position).ScoreSumPercent / arrayList.get(position).ScoreSumClick));
        }
        holder.Root.setOnClickListener(view -> {
            Intent intent = new Intent(context, ActDetailBlog.class);
            BlogContentViewRequest request = new BlogContentViewRequest();
            request.Id = arrayList.get(position).Id;
            intent.putExtra("Request", new Gson().toJson(request));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lblTitleRowRecyclerBlog)
        TextView LblTitle;

        @BindView(R.id.lblDescriptionRowRecyclerBlog)
        TextView LblDescrption;

        @BindView(R.id.lblLikeRowRecyclerBlog)
        TextView LblLike;

        @BindView(R.id.imgRowRecyclerBlog)
        ImageView Img;

        @BindView(R.id.ratingBarRowRecyclerBlog)
        RatingBar Rate;

        @BindView(R.id.rootBlog)
        CardView Root;

        @BindView(R.id.ProgressRecyclerBlog)
        ProgressBar Progress;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            LblTitle.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            LblDescrption.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            LblLike.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Progress.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }
    }
}
