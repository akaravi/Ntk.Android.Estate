package ntk.android.estate.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.android.estate.R;
import ntk.android.estate.adapter.AdCommentNews;
import ntk.android.estate.adapter.AdTabNews;
import ntk.android.estate.config.ConfigRestHeader;
import ntk.android.estate.config.ConfigStaticValue;
import ntk.android.estate.event.EvHtmlBodyNews;
import ntk.android.estate.utill.FontManager;
import ntk.base.api.baseModel.Filters;
import ntk.base.api.news.interfase.INews;
import ntk.base.api.news.model.NewsCommentAddRequest;
import ntk.base.api.news.model.NewsCommentListRequest;
import ntk.base.api.news.model.NewsCommentResponse;
import ntk.base.api.news.model.NewsContentFavoriteAddRequest;
import ntk.base.api.news.model.NewsContentFavoriteAddResponse;
import ntk.base.api.news.model.NewsContentFavoriteRemoveRequest;
import ntk.base.api.news.model.NewsContentFavoriteRemoveResponse;
import ntk.base.api.news.entity.NewsContentOtherInfo;
import ntk.base.api.news.model.NewsContentOtherInfoRequest;
import ntk.base.api.news.model.NewsContentOtherInfoResponse;
import ntk.base.api.news.model.NewsContentResponse;
import ntk.base.api.news.model.NewsContentViewRequest;
import ntk.base.api.utill.RetrofitManager;

public class ActDetailNews extends AppCompatActivity {

    @BindView(R.id.progressActDetailNews)
    ProgressBar Progress;

    @BindView(R.id.rowProgressActDetailNews)
    LinearLayout Loading;

    @BindViews({R.id.lblTitleActDetailNews,
            R.id.lblNameCommandActDetailNews,
            R.id.lblKeySeenActDetailNews,
            R.id.lblValueSeenActDetailNews,
            R.id.lblCommentActDetailNews,
            R.id.lblProgressActDetailNews
    })
    List<TextView> Lbls;

    @BindView(R.id.imgHeaderActDetailNews)
    ImageView ImgHeader;

    @BindView(R.id.WebViewActDetailNews)
    WebView webView;

    @BindView(R.id.recyclerTabActDetailNews)
    RecyclerView RvTab;

    @BindView(R.id.recyclerCommentActDetailNews)
    RecyclerView RvComment;

    @BindView(R.id.ratingBarActDetailNews)
    RatingBar Rate;

    @BindView(R.id.PageActDetailNews)
    LinearLayout Page;

    private String RequestStr;
    private NewsContentResponse model;
    private NewsContentOtherInfoResponse Info;
    private NewsContentViewRequest Request;
    private ConfigStaticValue configStaticValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail_news);
        ButterKnife.bind(this);
        configStaticValue = new ConfigStaticValue(this);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        for (TextView tv : Lbls) {
            tv.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        }
        Progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        webView.getSettings().setJavaScriptEnabled(true);
        RvTab.setHasFixedSize(true);
        RvTab.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        RequestStr = getIntent().getExtras().getString("Request");
        Request = new Gson().fromJson(RequestStr, NewsContentViewRequest.class);
        HandelDataContent(Request);
        Loading.setVisibility(View.VISIBLE);

        RvComment.setHasFixedSize(true);
        RvComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Rate.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            NewsContentViewRequest request = new NewsContentViewRequest();
            request.Id = Request.Id;
            request.ActionClientOrder = 55;
            if (rating == 0.5) {
                request.ScorePercent = 10;
            }
            if (rating == 1) {
                request.ScorePercent = 20;
            }
            if (rating == 1.5) {
                request.ScorePercent = 30;
            }
            if (rating == 2) {
                request.ScorePercent = 40;
            }
            if (rating == 2.5) {
                request.ScorePercent = 50;
            }
            if (rating == 3) {
                request.ScorePercent = 60;
            }
            if (rating == 3.5) {
                request.ScorePercent = 70;
            }
            if (rating == 4) {
                request.ScorePercent = 80;
            }
            if (rating == 4.5) {
                request.ScorePercent = 90;
            }
            if (rating == 5) {
                request.ScorePercent = 100;
            }
            RetrofitManager manager = new RetrofitManager(ActDetailNews.this);
            INews iNews = manager.getRetrofitUnCached(new ConfigStaticValue(ActDetailNews.this).GetApiBaseUrl()).create(INews.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(ActDetailNews.this);

            Observable<NewsContentResponse> Call = iNews.GetContentView(headers, request);
            Call.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<NewsContentResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(NewsContentResponse ContentResponse) {
                            if (ContentResponse.IsSuccess) {
                            } else {
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        });
    }


    private void HandelDataContent(NewsContentViewRequest request) {

        RetrofitManager retro = new RetrofitManager(this);
        INews iNews = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(INews.class);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

        Observable<NewsContentResponse> call = iNews.GetContentView(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NewsContentResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewsContentResponse ContentResponse) {
                        model = ContentResponse;
                        SetData(model);
                        if (Request.Id > 0) {
                            HandelDataContentOtherInfo(Request.Id);
                            HandelDataComment(Request.Id);
                        }
                        Loading.setVisibility(View.GONE);
                        Page.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Log", e.getMessage());
                        Loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void HandelDataComment(long ContentId) {
        List<Filters> filters = new ArrayList<>();
        NewsCommentListRequest Request = new NewsCommentListRequest();
        Filters f = new Filters();
        f.PropertyName = "LinkContentId";
        f.IntValue1 = ContentId;
        filters.add(f);
        Request.filters = filters;
        RetrofitManager retro = new RetrofitManager(this);
        INews iNews = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(INews.class);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
        Observable<NewsCommentResponse> call = iNews.GetCommentList(headers, Request);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsCommentResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewsCommentResponse model) {
                        if (model.IsSuccess) {
                            AdCommentNews adapter = new AdCommentNews(ActDetailNews.this, model.ListItems);
                            RvComment.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.warning(ActDetailNews.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void HandelDataContentOtherInfo(long ContentId) {

        List<Filters> filters = new ArrayList<>();
        NewsContentOtherInfoRequest Request = new NewsContentOtherInfoRequest();
        Filters f = new Filters();
        f.PropertyName = "LinkContentId";
        f.IntValue1 = ContentId;
        filters.add(f);
        Request.filters = filters;
        RetrofitManager retro = new RetrofitManager(this);
        INews iNews = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(INews.class);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);


        Observable<NewsContentOtherInfoResponse> call = iNews.GetContentOtherInfoList(headers, Request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<NewsContentOtherInfoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewsContentOtherInfoResponse ContentOtherInfoResponse) {
                        SetDataOtherinfo(ContentOtherInfoResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.warning(ActDetailNews.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void SetDataOtherinfo(NewsContentOtherInfoResponse model) {
        Info = model;
        if (model.ListItems == null || model.ListItems.size() == 0) {
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p.weight = 3;
            return;
        }
        List<NewsContentOtherInfo> Info = new ArrayList<>();

        for (NewsContentOtherInfo ai : model.ListItems) {
            switch (ai.TypeId) {
                case 21:
                    Lbls.get(7).setText(ai.Title);
                    ai.HtmlBody = ai.HtmlBody.replace("<p>", "");
                    ai.HtmlBody = ai.HtmlBody.replace("</p>", "");
                    Lbls.get(6).setText(Html.fromHtml(ai.HtmlBody));
                    break;
                case 22:
                    Lbls.get(9).setText(ai.Title);
                    ai.HtmlBody = ai.HtmlBody.replace("<p>", "");
                    ai.HtmlBody = ai.HtmlBody.replace("</p>", "");
                    Lbls.get(8).setText(Html.fromHtml(ai.HtmlBody));
                    break;
                case 23:
                    Lbls.get(11).setText(ai.Title);
                    ai.HtmlBody = ai.HtmlBody.replace("<p>", "");
                    ai.HtmlBody = ai.HtmlBody.replace("</p>", "");
                    Lbls.get(10).setText(Html.fromHtml(ai.HtmlBody));
                    break;
                default:
                    Info.add(ai);
                    break;
            }
        }
        AdTabNews adapter = new AdTabNews(ActDetailNews.this, Info);
        RvTab.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void SetData(NewsContentResponse model) {
        ImageLoader.getInstance().displayImage(model.Item.imageSrc, ImgHeader);
        Lbls.get(0).setText(model.Item.Title);
        Lbls.get(1).setText(model.Item.Title);
        Lbls.get(3).setText(String.valueOf(model.Item.viewCount));
        if (model.Item.Favorited) {
            ((ImageView) findViewById(R.id.imgHeartActDetailNews)).setImageResource(R.drawable.ic_fav_full);
        }
    }

    @OnClick(R.id.imgBackActDetailNews)
    public void ClickBack() {
        finish();
    }

    @Subscribe
    public void EventHtmlBody(EvHtmlBodyNews event) {
        webView.loadDataWithBaseURL("", event.GetMessage(), "text/html", "UTF-8", "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.imgCommentActDetailNews)
    public void ClickCommentAdd() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_comment_add);

        TextView Lbl = dialog.findViewById(R.id.lblTitleDialogAddComment);
        Lbl.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

        EditText Txt[] = new EditText[2];

        Txt[0] = dialog.findViewById(R.id.txtNameDialogAddComment);
        Txt[0].setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

        Txt[1] = dialog.findViewById(R.id.txtContentDialogAddComment);
        Txt[1].setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

        Button Btn = dialog.findViewById(R.id.btnSubmitDialogCommentAdd);
        Btn.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

        Btn.setOnClickListener(v -> {
            if (Txt[0].getText().toString().isEmpty()) {
                Toast.makeText(ActDetailNews.this, "لطفا مقادیر را وارد نمایید", Toast.LENGTH_SHORT).show();
            } else {
                if (Txt[1].getText().toString().isEmpty()) {
                    Toast.makeText(ActDetailNews.this, "لطفا مقادیر را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    NewsCommentAddRequest add = new NewsCommentAddRequest();
                    add.Writer = Txt[0].getText().toString();
                    add.Comment = Txt[1].getText().toString();
                    add.LinkContentId = Request.Id;
                    RetrofitManager retro = new RetrofitManager(this);
                    INews iNews = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(INews.class);
                    Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);


                    Observable<NewsCommentResponse> call = iNews.SetComment(headers, add);
                    call.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<NewsCommentResponse>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(NewsCommentResponse e) {
                                    if (e.IsSuccess) {
                                        HandelDataComment(Request.Id);
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toasty.warning(ActDetailNews.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
            }
        });

        dialog.show();

    }

    @OnClick(R.id.imgFavActDetailNews)
    public void ClickFav() {
        if (model.Item.Favorited){
            Fav();
        }else{
            UnFav();
        }
    }

    private void Fav() {
        RetrofitManager retro = new RetrofitManager(this);
        INews iNews = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(INews.class);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

        NewsContentFavoriteAddRequest add = new NewsContentFavoriteAddRequest();
        add.Id = model.Item.Id;

        Observable<NewsContentFavoriteAddResponse> Call = iNews.SetContentFavoriteAdd(headers, add);
        Call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsContentFavoriteAddResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewsContentFavoriteAddResponse e) {
                        if (e.IsSuccess) {
                            model.Item.Favorited = !model.Item.Favorited;
                            if (model.Item.Favorited) {
                                ((ImageView) findViewById(R.id.imgHeartActDetailNews)).setImageResource(R.drawable.ic_fav_full);
                            } else {
                                ((ImageView) findViewById(R.id.imgHeartActDetailNews)).setImageResource(R.drawable.ic_fav);
                            }
                        } else {
                            Toasty.error(ActDetailNews.this, e.ErrorMessage, Toast.LENGTH_LONG, true).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.warning(ActDetailNews.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void UnFav() {
        RetrofitManager retro = new RetrofitManager(this);
        INews iNews = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(INews.class);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

        NewsContentFavoriteRemoveRequest add = new NewsContentFavoriteRemoveRequest();
        add.Id = model.Item.Id;

        Observable<NewsContentFavoriteRemoveResponse> Call = iNews.SetContentFavoriteRemove(headers, add);
        Call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsContentFavoriteRemoveResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewsContentFavoriteRemoveResponse e) {
                        if (e.IsSuccess) {
                            model.Item.Favorited = !model.Item.Favorited;
                            if (model.Item.Favorited) {
                                ((ImageView) findViewById(R.id.imgHeartActDetailNews)).setImageResource(R.drawable.ic_fav_full);
                            } else {
                                ((ImageView) findViewById(R.id.imgHeartActDetailNews)).setImageResource(R.drawable.ic_fav);
                            }
                        } else {
                            Toasty.error(ActDetailNews.this, e.ErrorMessage, Toast.LENGTH_LONG, true).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.warning(ActDetailNews.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
