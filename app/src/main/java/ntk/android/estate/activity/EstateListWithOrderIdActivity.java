package ntk.android.estate.activity;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import java8.util.function.Function;
import ntk.android.base.Extras;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.base.SearchTypeModel;
import ntk.android.base.entitymodel.enums.EnumSortType;
import ntk.android.base.entitymodel.estate.EstateCustomerOrderModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.AndroidEstatePropertyService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstatePropertyAdapter;

public  class EstateListWithOrderIdActivity extends BaseFilterModelListActivity<EstatePropertyModel> {

    String PARTITION_TITLE;
    private String orderId;

    public static void START_NEW(Context context, EstateCustomerOrderModel item) {
        Intent i = new Intent(context, EstateListWithOrderIdActivity.class);
        FilterModel filterModel = new FilterModel();
        filterModel.setSortType(EnumSortType.Descending.index())
                .setSortColumn("Id");
        i.putExtra(Extras.EXTRA_FIRST_ARG, new Gson().toJson(filterModel));
        i.putExtra(Extras.Extra_5_ARG, item.Id);
        context.startActivity(i);
    }

    @Override
    protected void requestOnIntent() {
        orderId=getIntent().getExtras().getString(Extras.Extra_5_ARG);
        super.requestOnIntent();
    }

    @Override
    protected void onCreated() {
        LblTitle.setText(R.string.per_result);
        if (PARTITION_TITLE == null) {
            PARTITION_TITLE = "اتاق";
        }

    }

    @Override
    protected void onDestroy() {
        PARTITION_TITLE = null;
        super.onDestroy();
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<EstatePropertyModel>>> getService() {
        return (new AndroidEstatePropertyService(this,orderId))::allWithCustomerOrderId;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new EstatePropertyAdapter(models);
    }

    @Override
    public void ClickSearch() {
        Intent intent = new Intent(this, SearchEstateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public List<SearchTypeModel> getSortList() {
        ArrayList<SearchTypeModel> objects = new ArrayList<>();
        objects.add(new SearchTypeModel().setSortColumn("CreateDate").setSortType(EnumSortType.Ascending.index()).setDisplayName("جدیدترین"));
        objects.add(new SearchTypeModel().setSortColumn("CreateDate").setSortType(EnumSortType.Descending.index()).setDisplayName("قدیمی ترین"));
        objects.add(new SearchTypeModel().setSortType(EnumSortType.Random.index()).setDisplayName("به صورت تصادفی"));
        objects.add(new SearchTypeModel().setSortColumn("Area").setSortType(EnumSortType.Descending.index()).setDisplayName("بر حسب بیشترین متراژ"));
        objects.add(new SearchTypeModel().setSortColumn("Area").setSortType(EnumSortType.Ascending.index()).setDisplayName("بر حسب کمترین متراژ"));
        objects.add(new SearchTypeModel().setSortColumn("Partition").setSortType(EnumSortType.Descending.index()).setDisplayName("بر حسب بیشترین تعداد " + PARTITION_TITLE));
        objects.add(new SearchTypeModel().setSortColumn("Partition").setSortType(EnumSortType.Ascending.index()).setDisplayName("بر حسب کم ترین تعداد " + PARTITION_TITLE));
        return objects;
    }
}
