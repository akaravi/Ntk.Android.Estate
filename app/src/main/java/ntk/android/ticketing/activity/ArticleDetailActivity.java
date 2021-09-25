package ntk.android.estate.activity;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.activity.article.BaseArticleDetail_1_Activity;
import ntk.android.base.entitymodel.article.ArticleCommentModel;
import ntk.android.base.entitymodel.article.ArticleContentOtherInfoModel;
import ntk.android.estate.R;
import ntk.android.estate.adapter.ArticleCommentAdapter;
import ntk.android.estate.adapter.TabArticleAdapter;

public class ArticleDetailActivity extends BaseArticleDetail_1_Activity {
    @Override
    protected void initChild() {
        favoriteDrawableId = R.drawable.ic_fav_full;
        unFavoriteDrawableId = R.drawable.ic_fav;
    }

    @Override
    public RecyclerView.Adapter createCommentAdapter(List<ArticleCommentModel> listItems) {
        return new ArticleCommentAdapter(this, listItems);
    }

    @Override
    protected RecyclerView.Adapter createOtherInfoAdapter(List<ArticleContentOtherInfoModel> info) {
        return new TabArticleAdapter(this, info);
    }
}

