package ntk.android.estate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.activity.abstraction.AbstractMainActivity;
import ntk.android.base.activity.common.NotificationsActivity;
import ntk.android.base.activity.poling.PolingDetailActivity;
import ntk.android.base.activity.estate.FaqActivity;
import ntk.android.base.activity.estate.TicketListActivity;
import ntk.android.base.activity.estate.TicketSearchActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.news.NewsContentModel;
import ntk.android.base.services.news.NewsContentService;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.adapter.CoreImageAdapter;

public class MainActivity_1 extends AbstractMainActivity {

    @BindViews({R.id.news,
            R.id.pooling,
            R.id.invite,
            R.id.feedback,
            R.id.question,
            R.id.intro,
            R.id.article,
            R.id.aboutUs,
            R.id.support,
            R.id.message,
            R.id.search})
    List<TextView> lbl;

    @BindViews({R.id.newsBtn,
            R.id.poolingBtn,
            R.id.searchBtn,
            R.id.inviteBtn,
            R.id.feedbackBtn,
            R.id.questionBtn,
            R.id.introBtn,
            R.id.articleBtn,
            R.id.aboutUsBtn,
            R.id.supportBtn,
            R.id.messageBtn})
    List<LinearLayout> btn;

    @BindView(R.id.bannerLayout)
    LinearLayout layout;

    @BindView(R.id.SliderActMain)
    RecyclerView Slider;

    @BindView(R.id.RefreshMain)
    SwipeRefreshLayout Refresh;

    private long lastPressedTime;
    private static final int PERIOD = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDirectContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setAnimation();
        for (int i = 0; i < lbl.size(); i++) {
            lbl.get(i).setTypeface(FontManager.T2_Typeface(this));
        }
        Refresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorAccent);

        Refresh.setOnRefreshListener(() -> {
//         todo   CheckUpdate(res.Item);
//            setAnimation();
//            Refresh.setRefreshing(false);
        });
        HandelSlider();
    }

    private void setAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setInterpolator(new BounceInterpolator());
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        for (int i = 0; i < btn.size(); i++) {
            btn.get(i).startAnimation(scaleAnimation);
        }
        layout.startAnimation(alphaAnimation);
    }


    private void HandelSlider() {

        FilterModel request = new FilterModel();
        request.RowPerPage = 5;
        request.CurrentPageNumber = 1;
        new NewsContentService(this).getAll(request).
                observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new NtkObserver<ErrorException<NewsContentModel>>() {

                    @Override
                    public void onNext(ErrorException<NewsContentModel> newsContentResponse) {
                        if (newsContentResponse.IsSuccess) {
                            if (newsContentResponse.ListItems.size() > 0) {
                                findViewById(R.id.linear).setVisibility(View.VISIBLE);
                                findViewById(R.id.linear).setBackground(null);
                                SnapHelper snapHelper = new PagerSnapHelper();
                                CoreImageAdapter adapter = new CoreImageAdapter(MainActivity_1.this, newsContentResponse.ListItems);
                                Slider.setHasFixedSize(true);
                                LinearLayoutManager manager = new LinearLayoutManager(MainActivity_1.this, LinearLayoutManager.HORIZONTAL, true);
                                Slider.setLayoutManager(manager);
                                Slider.setAdapter(adapter);
                                snapHelper.attachToRecyclerView(Slider);
                                adapter.notifyDataSetChanged();
                            } else
                                findViewById(R.id.linear).setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    @OnClick(R.id.supportBtn)
    public void onSupportClick() {
        this.startActivity(new Intent(this, TicketListActivity.class));
    }

    @OnClick(R.id.searchBtn)
    public void onSearchClick() {
        this.startActivity(new Intent(this, TicketSearchActivity.class));
    }

    @OnClick(R.id.messageBtn)
    public void onInboxClick() {
        this.startActivity(new Intent(this, NotificationsActivity.class));
    }

    @OnClick(R.id.newsBtn)
    public void onNewsClick() {
        this.startActivity(new Intent(this, NewsListActivity.class));
    }

    @OnClick(R.id.feedbackBtn)
    public void onFeedBackClick() {
        onFeedbackClick();
    }

    @OnClick(R.id.poolingBtn)
    public void onPoolingClick() {
        this.startActivity(new Intent(this, PolingDetailActivity.class));
    }

    @OnClick(R.id.inviteBtn)
    public void onInviteClick() {
        onInviteMethod();
    }

    @OnClick(R.id.questionBtn)
    public void onQuestionClick() {
        this.startActivity(new Intent(this, FaqActivity.class));
    }

    @OnClick(R.id.articleBtn)
    public void onArticleClick() {
        this.startActivity(new Intent(this, ArticleListActivity.class));
    }

    @OnClick(R.id.aboutUsBtn)
    public void onAboutUsClick() {
        this.startActivity(new Intent(this, AboutUsActivity.class));
    }

    @OnClick(R.id.introBtn)
    public void onIntroClick() {
        onMainIntro();
    }
}