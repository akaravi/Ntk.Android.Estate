package ntk.android.estate.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.android.estate.R;
import ntk.android.estate.config.ConfigRestHeader;
import ntk.android.estate.config.ConfigStaticValue;
import ntk.android.estate.utill.AppUtill;
import ntk.android.estate.utill.FontManager;
import ntk.base.api.blog.interfase.IBlog;
import ntk.base.api.blog.entity.BlogComment;
import ntk.base.api.blog.model.BlogCommentResponse;
import ntk.base.api.blog.model.BlogCommentViewRequest;
import ntk.base.api.utill.NTKClientAction;
import ntk.base.api.utill.RetrofitManager;

public class AdCommentBlog extends RecyclerView.Adapter<AdCommentBlog.ViewHolder> {

    private List<BlogComment> arrayList;
    private Context context;

    public AdCommentBlog(Context context, List<BlogComment> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler_comment, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Lbls.get(0).setText(arrayList.get(position).Writer);
        if (arrayList.get(position).CreatedDate != null) {
            holder.Lbls.get(1).setText(AppUtill.GregorianToPersian(arrayList.get(position).CreatedDate));
        } else {
            holder.Lbls.get(1).setText("");
        }
        holder.Lbls.get(2).setText(String.valueOf(arrayList.get(position).SumDisLikeClick));
        holder.Lbls.get(3).setText(String.valueOf(arrayList.get(position).SumLikeClick));
        holder.Lbls.get(4).setText(String.valueOf(arrayList.get(position).Comment));

        holder.ImgLike.setOnClickListener(v -> {
            BlogCommentViewRequest request = new BlogCommentViewRequest();
            request.Id = arrayList.get(position).Id;
            request.ActionClientOrder = NTKClientAction.LikeClientAction;
            RetrofitManager retro = new RetrofitManager(context);
            IBlog iNews = retro.getRetrofitUnCached(new ConfigStaticValue(context).GetApiBaseUrl()).create(IBlog.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(context);
            Observable<BlogCommentResponse> call = iNews.GetCommentView(headers, request);
            call.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<BlogCommentResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BlogCommentResponse model) {
                            if (model.IsSuccess) {
                                arrayList.get(position).SumLikeClick = arrayList.get(position).SumLikeClick + 1;
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toasty.warning(context, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                        }

                        @Override
                        public void onComplete() {
                            Toasty.warning(context, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                        }
                    });
        });

        holder.ImgDisLike.setOnClickListener(v -> {
            BlogCommentViewRequest request = new BlogCommentViewRequest();
            request.Id = arrayList.get(position).Id;
            request.ActionClientOrder = NTKClientAction.DisLikeClientAction;
            RetrofitManager retro = new RetrofitManager(context);
            IBlog iNews = retro.getRetrofitUnCached(new ConfigStaticValue(context).GetApiBaseUrl()).create(IBlog.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(context);
            Observable<BlogCommentResponse> call = iNews.GetCommentView(headers, request);
            call.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<BlogCommentResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BlogCommentResponse model) {
                            if (model.IsSuccess) {
                                arrayList.get(position).SumDisLikeClick = arrayList.get(position).SumDisLikeClick - 1;
                                notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.lblUserNameRecyclerComment,
                R.id.lblDateRecyclerComment,
                R.id.lblDesLikeRecyclerComment,
                R.id.lblLikeRecyclerComment,
                R.id.lblContentRecyclerComment
        })
        List<TextView> Lbls;

        @BindView(R.id.imgDisLikeRecyclerComment)
        ImageView ImgDisLike;

        @BindView(R.id.imgLikeRecyclerComment)
        ImageView ImgLike;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Lbls.get(0).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Lbls.get(1).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Lbls.get(2).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Lbls.get(3).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Lbls.get(4).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        }
    }
}
