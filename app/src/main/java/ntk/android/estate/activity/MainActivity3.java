package ntk.android.estate.activity;

//import static ntk.android.estate.utils.MapUtility.convertSizeThumbnailImage;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.Extras;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.dtomodel.theme.DrawerChildThemeDtoModel;
import ntk.android.base.entitymodel.article.ArticleContentModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.enums.EnumClauseType;
import ntk.android.base.entitymodel.enums.EnumSearchType;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.entitymodel.news.NewsContentModel;
import ntk.android.base.services.article.ArticleContentService;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.services.estate.EstatePropertyTypeLanduseService;
import ntk.android.base.services.news.NewsContentService;
import ntk.android.base.utill.FontManager;
import ntk.android.base.utill.imageCompressor;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.base.view.NViewUtils;
import ntk.android.estate.R;
import ntk.android.estate.adapter.Main3ArticleAdapter;
import ntk.android.estate.adapter.Main3EstateLandUseAdapter;
import ntk.android.estate.adapter.Main3EstatePropertyAdapter;
import ntk.android.estate.adapter.Main3EstateSpecialAdapter;
import ntk.android.estate.adapter.Main3NewsAdapter;
import ntk.android.estate.adapter.drawer.Drawer3Adapter;
import ntk.android.estate.adapter.drawer.DrawerAdapter;
import ntk.android.estate.models.RowModel;
import ntk.android.estate.models.RuntimeJsonModel;

public class MainActivity3 extends BaseMainActivity {
    RecyclerView Slider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Slider = findViewById(R.id.rcNews);
        //show drawer
        List<DrawerChildThemeDtoModel> menus = DrawerAdapter.createDrawerItems(updateInfo.allowDirectShareApp, isLogin());
        RecyclerView drawerRecycler = findViewById(R.id.RecyclerDrawer);
        Drawer3Adapter adapter = new Drawer3Adapter(this, menus, findViewById(R.id.floaingDrawer));
        drawerRecycler.setAdapter(adapter);
        drawerRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        init();
        //get news
        HandelSlider();
        //get landUsed property
        getLandUsedProperty();
        //get rows
        getData(row1, findViewById(R.id.row1));
        getData(row2, findViewById(R.id.row2));
        getData(row3, findViewById(R.id.row3));
        //get runTime Json
        getRuntimeJson();
        //get articles
        getArticles();

    }

    private void getRuntimeJson() {
        String config = Preferences.with(this).appVariableInfo().applicationAppModel().ConfigRuntimeSiteJsonValues;
        List<View> viewRow = Arrays.asList(findViewById(R.id.includeRow1), findViewById(R.id.includedRow2));
        if (config != null && !config.equals("") && !config.equals("null")) {
            try {
                List<RowModel> dataRow = (new Gson().fromJson(config, RuntimeJsonModel.class)).ListItems;

                for (int i = 0; i < dataRow.size() && i < 2; i++) {
                    RowModel model = dataRow.get(i);
                    View view = viewRow.get(i);
                    view.setVisibility(View.VISIBLE);
                    //set header
                    if (!model.HeaderString.equals("")) {
                        ((TextView) view.findViewById(R.id.title)).setText(model.HeaderString);
                    } else
                        ((TextView) view.findViewById(R.id.title)).setVisibility(View.INVISIBLE);
                    //set seeMore
                    if (model.Filter != null && !model.Filter.equals("")) {
                        //add clickListener
                        (view.findViewById(R.id.seeMore)).setOnClickListener(v -> {
                            Intent intent = new Intent(MainActivity3.this, EstateListActivity.class);
                            intent.putExtra(Extras.EXTRA_FIRST_ARG, new Gson().toJson(model.Filter));
                            startActivity(intent);
                        });
                        //load data
                        getData(model.Filter, view);
                    } else {
                        (view.findViewById(R.id.seeMore)).setVisibility(View.INVISIBLE);
                        if (model.Items != null && model.Items.size() > 0) {
                            //show custom dataRow
                            RecyclerView rc = view.findViewById(R.id.rc);
                            rc.setLayoutManager(new LinearLayoutManager(MainActivity3.this, RecyclerView.HORIZONTAL, false));
                            rc.setAdapter(new Main3EstateSpecialAdapter(model.Items));
                            ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.shimmer_rc);
                            shimmerFrameLayout.stopShimmerAnimation();
                            shimmerFrameLayout.setVisibility(View.GONE);
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("RUNTIME_CONFIG_JSON", config);
                Log.d("RUNTIME_CONFIG_JSON", e.toString());
                viewRow.get(0).setVisibility(View.GONE);
                viewRow.get(1).setVisibility(View.GONE);
            }
        }
    }

    private void init() {
        TextView seeMore = findViewById(R.id.seeMore);
        //see landUse list on new activity on click
        seeMore.setOnClickListener(view -> startActivity(new Intent(MainActivity3.this, LandUsedListActivity.class)));
        //click on humberger
        findViewById(R.id.img_menu).setOnClickListener(v -> ((FlowingDrawer) findViewById(R.id.floaingDrawer)).openMenu(true));
        //click share
        if (!updateInfo.allowDirectShareApp) {
            findViewById(R.id.shareQrCode).setVisibility(View.GONE);
        } else {
            findViewById(R.id.shareQrCode).setOnClickListener(v -> onInviteMethod());
        }
        //search fab
        findViewById(R.id.fabSearch).setOnClickListener(view -> startActivity(new Intent(this, SearchEstateActivity.class)));

        //click search
        findViewById(R.id.searchBtn).setOnClickListener(view -> Search());
        ((EditText) findViewById(R.id.searchEt)).setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Search();
                return true;
            }
            return false;
        });
        TextView rowTitle1 = findViewById(R.id.row1).findViewById(R.id.title);
        TextView rowSeeMore1 = findViewById(R.id.row1).findViewById(R.id.seeMore);
        TextView rowTitle2 = findViewById(R.id.row2).findViewById(R.id.title);
        TextView rowSeeMore2 = findViewById(R.id.row2).findViewById(R.id.seeMore);
        TextView rowTitle3 = findViewById(R.id.row3).findViewById(R.id.title);
        TextView rowSeeMore3 = findViewById(R.id.row3).findViewById(R.id.seeMore);
        TextView rowTitle4 = findViewById(R.id.row4).findViewById(R.id.title);
        TextView rowSeeMore4 = findViewById(R.id.row4).findViewById(R.id.seeMore);
        //special list
        String title1 = "جدیدترین ";
        String title2 = "پیشنهادی ";
        String title3 = "اجاره روزانه ";
        String title4 = "آخرین مقالات";
        rowTitle1.setText(title1);
        rowTitle2.setText(title2);
        rowTitle3.setText(title3);
        rowTitle4.setText(title4);
        rowSeeMore1.setOnClickListener(view -> EstateListWithFilterActivity.START_NEW(MainActivity3.this, row1, title1));
        rowSeeMore2.setOnClickListener(view -> EstateListWithFilterActivity.START_NEW(MainActivity3.this, row2, title2));
        rowSeeMore3.setOnClickListener(view -> EstateListWithFilterActivity.START_NEW(MainActivity3.this, row3, title3));
        rowSeeMore4.setOnClickListener(view -> startActivity(new Intent(MainActivity3.this, ArticleListActivity.class)));

        //set font
        Typeface t1 = FontManager.T1_Typeface(this);
        ((EditText) findViewById(R.id.searchEt)).setTypeface(t1);
        ((TextView) findViewById(R.id.title1)).setTypeface(t1);
        ((ExtendedFloatingActionButton) findViewById(R.id.fabAdd)).setTypeface(t1);

        seeMore.setTypeface(t1);
        rowTitle1.setTypeface(t1);
        rowSeeMore1.setTypeface(t1);
        rowTitle2.setTypeface(t1);
        rowSeeMore2.setTypeface(t1);
        rowTitle3.setTypeface(t1);
        rowSeeMore3.setTypeface(t1);
        rowTitle4.setTypeface(t1);
        rowSeeMore4.setTypeface(t1);
        //show shimmer
        findViewById(R.id.shimmer_news1).getLayoutParams().width = Main3NewsAdapter.ITEM_WIDTH();
        findViewById(R.id.shimmer_news2).getLayoutParams().width = Main3NewsAdapter.ITEM_WIDTH();
        ((ShimmerFrameLayout) findViewById(R.id.news_shimmer)).startShimmerAnimation();
        ((ShimmerFrameLayout) findViewById(R.id.landUsed_shimmer)).startShimmerAnimation();
        initStatePropertyShimmer(findViewById(R.id.row1));
        initStatePropertyShimmer(findViewById(R.id.row2));
        initStatePropertyShimmer(findViewById(R.id.row3));

        //add fab
        findViewById(R.id.fabAdd).setOnClickListener(view ->
                showBallon(view));
        MaterialButton projectBtn = findViewById(R.id.projects);
        MaterialButton constructorBtn = findViewById(R.id.constructors);
        projectBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity3.this, ProjectListActivity.class));
        });
        constructorBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity3.this, CompanyListActivity.class));
        });
        projectBtn.setTypeface(t1);
        constructorBtn.setTypeface(t1);
    }

    private void showBallon(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Balloon balloon = new Balloon.Builder(this)
                    .setLayout(R.layout.sub_new_buttons)
                    .setArrowSize(10)
                    .setArrowOrientation(ArrowOrientation.TOP)
                    .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                    .setArrowPosition(0.5f)
                    .setWidthRatio(1f)
                    .setIsVisibleOverlay(true)
                    .setHeight(BalloonSizeSpec.WRAP)
                    .setTextSize(15f)
                    .setCornerRadius(4f)
                    .setAlpha(0.9f)
                    .setMarginLeft(64)
                    .setMarginRight(64)
                    .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .setBalloonAnimation(BalloonAnimation.CIRCULAR)
                    .build();
            balloon.showAlignTop(view, 1000);
            balloon.getContentView().findViewById(R.id.new_estate).setOnClickListener(v -> {
                balloon.dismiss();
                NewEstateActivity.START_ACTIVITY(MainActivity3.this);
            });
            balloon.getContentView().findViewById(R.id.new_order).setOnClickListener(v -> {
                balloon.dismiss();
                NewCustomerOrderActivity.START_ACTIVITY(MainActivity3.this);
            });
        } else {
            View popupView = getLayoutInflater().inflate(R.layout.sub_new_buttons, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            int[] size = new int[2];
            findViewById(R.id.popUpIndicatorView).getLocationOnScreen(size);
            popupWindow.showAtLocation(findViewById(R.id.popUpIndicatorView), Gravity.NO_GRAVITY, size[0], size[1]);
            popupWindow.getContentView().findViewById(R.id.new_estate).setOnClickListener(v -> {
                popupWindow.dismiss();
                NewEstateActivity.START_ACTIVITY(MainActivity3.this);
            });
            popupWindow.getContentView().findViewById(R.id.new_order).setOnClickListener(v -> {
                popupWindow.dismiss();
                NewCustomerOrderActivity.START_ACTIVITY(MainActivity3.this);
            });
        }
    }

    private void Search() {
        String text = ((EditText) findViewById(R.id.searchEt)).getText().toString();
        if (text.trim().equals("")) Toasty.info(this, "عنوان مورد نظر خود را وارد کنید").show();
        else {
            FilterModel filterModel = new FilterModel();
            filterModel.addFilter(new FilterDataModel().setPropertyName("Title").setSearchType(EnumSearchType.Contains).setClauseType(EnumClauseType.Or.index()).setStringValue(text));
            filterModel.addFilter(new FilterDataModel().setPropertyName("Description").setSearchType(EnumSearchType.Contains).setClauseType(EnumClauseType.Or.index()).setStringValue(text));
            EstateListWithFilterActivity.START_NEW(MainActivity3.this, filterModel, "موارد یافت شده");
        }

    }

    private void initStatePropertyShimmer(View v) {
        int w = Main3EstatePropertyAdapter.ITEM_WIDTH();
        v.findViewById(R.id.shimmer_property1).getLayoutParams().width = w;
        {
            View image = v.findViewById(R.id.shimmer_property1).findViewById(R.id.image);
            image.getLayoutParams().width = w;
            image.getLayoutParams().height = w;
        }
        v.findViewById(R.id.shimmer_property2).getLayoutParams().width = w;
        {
            View image = v.findViewById(R.id.shimmer_property2).findViewById(R.id.image);
            image.getLayoutParams().width = w;
            image.getLayoutParams().height = w;
        }
        ((ShimmerFrameLayout) v.findViewById(R.id.shimmer_rc)).startShimmerAnimation();
    }

    private void getData(FilterModel filter, View view) {
        ServiceExecute.execute(new EstatePropertyService(this).getAll(filter)).subscribe(new ErrorExceptionObserver<EstatePropertyModel>(switcher::showErrorView) {

            @Override
            protected void SuccessResponse(ErrorException<EstatePropertyModel> response) {
                RecyclerView rc = view.findViewById(R.id.rc);

                if (response.IsSuccess)
                    //image optimize
                    for (EstatePropertyModel itemL : response.ListItems) {
                        itemL.LinkMainImageIdSrc = imageCompressor.convertSizeThumbnailImage(itemL.LinkMainImageIdSrc, 300, 300);
                        if (itemL.LinkFileIdsSrc != null && itemL.LinkFileIdsSrc.size() > 0)
                            for (int i = 0; i < itemL.LinkFileIdsSrc.size(); i++) {
                                itemL.LinkFileIdsSrc.set(i, imageCompressor.convertSizeThumbnailImage(itemL.LinkFileIdsSrc.get(i), 300, 300));
                            }
                        if (itemL.LinkExtraImageIdsSrc != null && itemL.LinkExtraImageIdsSrc.size() > 0)
                            for (int i = 0; i < itemL.LinkExtraImageIdsSrc.size(); i++) {
                                itemL.LinkExtraImageIdsSrc.set(i, imageCompressor.convertSizeThumbnailImage(itemL.LinkExtraImageIdsSrc.get(i), 300, 300));
                            }
                    }
                if (response.ListItems.size() > 0) {
                    rc.setAdapter(new Main3EstatePropertyAdapter(response.ListItems));
                    rc.setLayoutManager(new LinearLayoutManager(MainActivity3.this, RecyclerView.HORIZONTAL, false));
                    ViewCompat.setNestedScrollingEnabled(rc, false);
                    ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.shimmer_rc);
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                } else view.setVisibility(View.GONE);
            }

            @Override
            protected Runnable tryAgainMethod() {
                return () -> getData(filter, view);
            }
        });


    }


    private void getLandUsedProperty() {
        ServiceExecute.execute(new EstatePropertyTypeLanduseService(this).getAll(new FilterModel().setRowPerPage(100))).subscribe(new NtkObserver<>() {
            @Override
            public void onNext(@NonNull ErrorException<EstatePropertyTypeLanduseModel> response) {

                Main3EstateLandUseAdapter adapter = new Main3EstateLandUseAdapter(response.ListItems);
                RecyclerView rc = findViewById(R.id.landUseAdapter);
                rc.setAdapter(adapter);
                ShimmerFrameLayout shimmer = findViewById(R.id.landUsed_shimmer);
                shimmer.stopShimmerAnimation();
                shimmer.setVisibility(View.GONE);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }
        });
    }

    private void HandelSlider() {

        FilterModel request = new FilterModel();
        request.RowPerPage = 5;
        request.SortColumn = "Id";
        request.CurrentPageNumber = 1;
        new NewsContentService(this).getAll(request).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new NtkObserver<ErrorException<NewsContentModel>>() {

            @Override
            public void onNext(ErrorException<NewsContentModel> newsContentResponse) {
                ShimmerFrameLayout shimmerLayout = findViewById(R.id.news_shimmer);
                shimmerLayout.stopShimmerAnimation();
                shimmerLayout.setVisibility(View.GONE);
                if (newsContentResponse.IsSuccess) {
                    if (newsContentResponse.ListItems.size() > 0) {
                        //image Optimaze
                        for (NewsContentModel itemL : newsContentResponse.ListItems) {
                            itemL.LinkMainImageIdSrc = imageCompressor.convertSizeThumbnailImage(itemL.LinkMainImageIdSrc, 300, 300);
                            if (itemL.LinkFileIdsSrc != null && itemL.LinkFileIdsSrc.size() > 0)
                                for (int i = 0; i < itemL.LinkFileIdsSrc.size(); i++) {
                                    itemL.LinkFileIdsSrc.set(i, imageCompressor.convertSizeThumbnailImage(itemL.LinkFileIdsSrc.get(i), 300, 300));
                                }
                        }
                        //image Optimaze
                        SnapHelper snapHelper = new PagerSnapHelper();
                        Main3NewsAdapter adapter = new Main3NewsAdapter(MainActivity3.this, newsContentResponse.ListItems);
                        LinearLayoutManager manager = new LinearLayoutManager(MainActivity3.this, LinearLayoutManager.HORIZONTAL, false);
                        Slider.setLayoutManager(manager);
                        Slider.setAdapter(adapter);
                        snapHelper.attachToRecyclerView(Slider);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    private void getArticles() {
        ServiceExecute.execute(new ArticleContentService(this).getAll(row6)).subscribe(new ErrorExceptionObserver<ArticleContentModel>(switcher::showErrorView) {

            @Override
            protected void SuccessResponse(ErrorException<ArticleContentModel> response) {
                View view = findViewById(R.id.row4);
                RecyclerView rc = view.findViewById(R.id.row4).findViewById(R.id.rc);
                //hide shimmer
                ViewCompat.setNestedScrollingEnabled(rc, false);
                ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.shimmer_rc);
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                if (response.IsSuccess)
                    //image optimize
                    if (response.ListItems.size() > 0) {
                        //image Optimaze
                        for (ArticleContentModel itemL : response.ListItems) {
                            itemL.LinkMainImageIdSrc = imageCompressor.convertSizeThumbnailImage(itemL.LinkMainImageIdSrc, 300, 300);
                            if (itemL.LinkFileIdsSrc != null && itemL.LinkFileIdsSrc.size() > 0)
                                for (int i = 0; i < itemL.LinkFileIdsSrc.size(); i++) {
                                    itemL.LinkFileIdsSrc.set(i, imageCompressor.convertSizeThumbnailImage(itemL.LinkFileIdsSrc.get(i), 300, 300));
                                }
                        }
                        SnapHelper snapHelper = new PagerSnapHelper();
                        Main3ArticleAdapter adapter = new Main3ArticleAdapter(MainActivity3.this, response.ListItems);
                        rc.setAdapter(adapter);
                        rc.setLayoutManager(new LinearLayoutManager(MainActivity3.this, RecyclerView.HORIZONTAL, false));
                        rc.setOnFlingListener(null);
                        snapHelper.attachToRecyclerView(rc);
                    }
            }

            @Override
            protected Runnable tryAgainMethod() {
                return () -> getArticles();
            }
        });


    }

}
