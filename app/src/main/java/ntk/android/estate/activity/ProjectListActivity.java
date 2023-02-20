package ntk.android.estate.activity;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyProjectModel;
import ntk.android.base.services.estate.EstatePropertyProjectService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.ProjectAdapter;

public class ProjectListActivity extends BaseFilterModelListActivity<EstatePropertyProjectModel> {
    @Override
    public void afterInit() {
        super.afterInit();
        LblTitle.setText("پروژه ها");
        findViewById(R.id.imgSearch).setVisibility(View.GONE);
        findViewById(R.id.imgSort).setVisibility(View.GONE);
    }   @Override
    public RecyclerView.Adapter createAdapter() {
        return new ProjectAdapter(models);
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<EstatePropertyProjectModel>>> getService() {
        return new EstatePropertyProjectService(this)::getAll;
    }
}
