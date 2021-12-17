package ntk.android.estate.activity;

import android.content.Context;

import java.util.List;

import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.base.SearchTypeModel;

class EstateListWithFilterActivity extends EstateListActivity {
    public static void START_NEW(Context context, FilterModel filter) {

    }
    @Override
    public List<SearchTypeModel> getSortList() {
        return null;
    }
}
