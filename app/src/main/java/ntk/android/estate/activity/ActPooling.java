package ntk.android.estate.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.android.estate.R;
import ntk.android.estate.adapter.AdPoolCategory;
import ntk.android.estate.config.ConfigRestHeader;
import ntk.android.estate.config.ConfigStaticValue;
import ntk.android.estate.utill.FontManager;
import ntk.base.api.pooling.interfase.IPooling;
import ntk.base.api.pooling.model.PoolingCategoryResponse;
import ntk.base.api.utill.RetrofitManager;

public class ActPooling extends AppCompatActivity {

    @BindView(R.id.lblTitleActPooling)
    TextView LblTitle;

    @BindView(R.id.recyclerPooling)
    RecyclerView Rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pooling);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        LblTitle.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Rv.setHasFixedSize(true);
        Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        RetrofitManager manager = new RetrofitManager(this);
        IPooling iPooling = manager.getRetrofitUnCached(new ConfigStaticValue(this).GetApiBaseUrl()).create(IPooling.class);
        Observable<PoolingCategoryResponse> call = iPooling.GetCategoryList(new ConfigRestHeader().GetHeaders(this));
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<PoolingCategoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PoolingCategoryResponse poolingCategoryResponse) {
                        if (poolingCategoryResponse.IsSuccess) {
                            AdPoolCategory adapter = new AdPoolCategory(ActPooling.this, poolingCategoryResponse.ListItems);
                            Rv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e){
                        Toasty.warning(ActPooling.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @OnClick(R.id.imgBackActPooling)
    public void ClickBack() {
        finish();
    }
}
