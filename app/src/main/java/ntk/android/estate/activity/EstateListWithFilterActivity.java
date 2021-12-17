package ntk.android.estate.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.gson.Gson;

import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.base.SearchTypeModel;
import ntk.android.estate.R;

public class EstateListWithFilterActivity extends EstateListActivity {
    public static void START_NEW(Context context, FilterModel filter,String title) {
        Intent i = new Intent(context, EstateListWithFilterActivity.class);
        i.putExtra(Extras.EXTRA_FIRST_ARG, new Gson().toJson(filter));
        i.putExtra(Extras.EXTRA_SECOND_ARG, title);
        context.startActivity(i);
    }
    @Override
    protected void onCreated() {
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getString(Extras.EXTRA_SECOND_ARG) != null)
            LblTitle.setText(getIntent().getExtras().getString(Extras.EXTRA_SECOND_ARG));
        findViewById(ntk.android.base.R.id.imgSearch).setVisibility(View.GONE);
    }

    @Override
    public List<SearchTypeModel> getSortList() {
        return null;
    }
}
