package ntk.android.estate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import ntk.android.base.Extras;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.article.ArticleContentModel;
import ntk.android.base.entitymodel.news.NewsContentModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.ArticleDetailActivity;
import ntk.android.estate.activity.MainActivity3;
import ntk.android.estate.activity.NewsDetailActivity;

public class Main3ArticleAdapter extends BaseRecyclerAdapter<ArticleContentModel, Main3ArticleAdapter.ViewHolder> {

    private final Context context;
    private final int width;

    public Main3ArticleAdapter(Context context, List<ArticleContentModel> list) {
        super(list);
        this.context = context;
        drawable = R.drawable.news_placeholder;
        width = ITEM_WIDTH();

    }

    public static int ITEM_WIDTH() {
        int w = getScreenWidth();
        var width = w - (w / 8);
        return width;
    }

    @Override
    public Main3ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflate(viewGroup, R.layout.row_recycler_main3_article);
        view.getLayoutParams().width = width;
        return new Main3ArticleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Main3ArticleAdapter.ViewHolder holder, final int position) {

        loadImage(list.get(position).LinkMainImageIdSrc, holder.Img);
        holder.Lbl.setText(list.get(position).Title);
        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, ArticleDetailActivity.class)
                .putExtra(Extras.EXTRA_FIRST_ARG, list.get(position).Id)));

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Img;

        TextView Lbl;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Img = view.findViewById(R.id.ImageRecyclerImage);
            Lbl = view.findViewById(R.id.LblRecyclerImage);
            Lbl.setTypeface(FontManager.T1_Typeface(context));
        }
    }
}