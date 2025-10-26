package ntk.android.estate.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.news.NewsContentModel;
import ntk.android.base.services.base.CmsApiScoreApi;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.NewsDetailActivity;


public class NewsAdapter extends BaseRecyclerAdapter<NewsContentModel, NewsAdapter.ViewHolder> {

    private final Context context;

    public NewsAdapter(Context context, List<NewsContentModel> arrayList) {
        super(arrayList);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflate(viewGroup, R.layout.row_recycler_news);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        NewsContentModel item = getItem(position);
        holder.LblTitle.setText(item.Title);
        holder.LblDescrption.setText(item.Description);
        holder.LblLike.setText(String.valueOf(item.ViewCount));
        holder.Img.setImageResource(R.drawable.news_placeholder);
        loadImage(item.LinkMainImageIdSrc, holder.Img, holder.Progress);
        double rating = CmsApiScoreApi.CONVERT_TO_RATE(item.ViewCount, item.ScoreSumPercent);

        holder.Rate.setRating((float) rating);
        holder.Root.setOnClickListener(view -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);

            intent.putExtra(Extras.EXTRA_FIRST_ARG, item.Id);
            context.startActivity(intent);
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView LblTitle;
        TextView LblDescrption;
        TextView LblLike;
        ImageView Img;
        RatingBar Rate;
        CardView Root;
        ProgressBar Progress;


        public ViewHolder(View view) {
            super(view);
            LblTitle = view.findViewById(R.id.lblTitleRowRecyclerNews);
            LblDescrption = view.findViewById(R.id.lblDescriptionRowRecyclerNews);
            LblLike = view.findViewById(R.id.lblLikeRowRecyclerNews);
            Img = view.findViewById(R.id.imgRowRecyclerNews);
            Rate = view.findViewById(R.id.ratingBarRowRecyclerNews);
            Root = view.findViewById(R.id.rootNews);
            Progress = view.findViewById(R.id.ProgressRecyclerNews);

            LblTitle.setTypeface(FontManager.T1_Typeface(context));
            LblDescrption.setTypeface(FontManager.T1_Typeface(context));
            LblLike.setTypeface(FontManager.T1_Typeface(context));
            Progress.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }
    }
}
