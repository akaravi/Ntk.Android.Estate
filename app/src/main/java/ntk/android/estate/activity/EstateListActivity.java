package ntk.android.estate.activity;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.Extras;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.base.SearchTypeModel;
import ntk.android.base.entitymodel.enums.EnumSortType;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeLanduseModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstatePropertyAdapter;

public class EstateListActivity extends BaseFilterModelListActivity<EstatePropertyModel> {

    static String PARTITION_TITLE;

    public static void START_NEW(Context context, EstatePropertyTypeLanduseModel item) {
        Intent i = new Intent(context, EstateListActivity.class);
        FilterModel filterModel = new FilterModel();
        filterModel.setSortType(EnumSortType.Descending.index())
                .setSortColumn("CreateDate")
                .addFilter(new FilterDataModel().setPropertyName("LinkPropertyTypeLanduseId").setStringValue(item.Id));
        i.putExtra(Extras.EXTRA_FIRST_ARG, new Gson().toJson(filterModel));
        PARTITION_TITLE = item.TitlePartition;
        context.startActivity(i);
    }


    @Override
    protected void onCreated() {
        LblTitle.setText(R.string.per_estate);
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
        return (new EstatePropertyService(this))::getAll;
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
