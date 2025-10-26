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
import ntk.android.base.entitymodel.article.ArticleContentModel;
import ntk.android.base.services.base.CmsApiScoreApi;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.ArticleDetailActivity;

public class ArticleAdapter extends BaseRecyclerAdapter<ArticleContentModel, ArticleAdapter.ViewHolder> {


    private final Context context;

    public ArticleAdapter(Context context, List<ArticleContentModel> arrayList) {
        super(arrayList);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflate(viewGroup,R.layout.row_recycler_article);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ArticleContentModel item = getItem(position);
        holder.LblTitle.setText(item.Title);
        holder.LblDescrption.setText(item.Description);
        holder.LblLike.setText(String.valueOf(item.ViewCount));
        holder.Img.setImageResource(R.drawable.article_place_holder);
        loadImage(item.LinkMainImageIdSrc, holder.Img, holder.Progress);
        double rating = CmsApiScoreApi.CONVERT_TO_RATE(item.ViewCount, item.ScoreSumPercent);
        holder.Rate.setRating((float) rating);
        holder.Root.setOnClickListener(view -> {
            Intent intent = new Intent(context, ArticleDetailActivity.class);
            intent.putExtra(Extras.EXTRA_FIRST_ARG, (item.Id));
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
            LblTitle = view.findViewById(R.id.lblTitleRowRecyclerArticle);
            LblDescrption = view.findViewById(R.id.lblDescriptionRowRecyclerArticle);
            LblLike = view.findViewById(R.id.lblLikeRowRecyclerArticle);
            Img = view.findViewById(R.id.imgRowRecyclerArticle);
            Rate = view.findViewById(R.id.ratingBarRowRecyclerArticle);
            Root = view.findViewById(R.id.rootArticle);
            Progress = view.findViewById(R.id.ProgressRecyclerArticle);
            
            LblTitle.setTypeface(FontManager.T1_Typeface(view.getContext()));
            LblDescrption.setTypeface(FontManager.T1_Typeface(view.getContext()));
            LblLike.setTypeface(FontManager.T1_Typeface(view.getContext()));
            Progress.getIndeterminateDrawable().setColorFilter(view.getContext().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }
    }
}
