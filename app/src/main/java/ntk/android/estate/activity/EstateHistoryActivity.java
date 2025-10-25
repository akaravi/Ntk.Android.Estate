package ntk.android.estate.activity;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstateActivityTypeModel;
import ntk.android.base.entitymodel.estate.EstatePropertyHistoryModel;
import ntk.android.base.services.estate.EstateActivityTypeService;
import ntk.android.base.services.estate.EstatePropertyHistoryService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstateHistoryAdapter;

public class EstateHistoryActivity extends BaseFilterModelListActivity<EstatePropertyHistoryModel> {
    List<EstateActivityTypeModel> activityTypes = new ArrayList<>();

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new EstateHistoryAdapter(models, activityTypes);
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<EstatePropertyHistoryModel>>> getService() {
        return new EstatePropertyHistoryService(this)::getAll;
    }

    @Override
    protected void onCreated() {
        findViewById(R.id.imgSearch).setVisibility(View.GONE);
        findViewById(R.id.imgSort).setVisibility(View.GONE);
    }

    @Override
    protected boolean callOtherApi() {
        if (activityTypes != null)
            return false;
        else {
            ServiceExecute.execute(new EstateActivityTypeService(this).getAll(new FilterModel())).subscribe(new NtkObserver<ErrorException<EstateActivityTypeModel>>() {
                @Override
                public void onNext(ErrorException<EstateActivityTypeModel> response) {
                    if (response.IsSuccess) {
                        activityTypes.addAll(response.ListItems);
                        RestCall(1);
                    } else
                        switcher.showErrorView(response.ErrorMessage, () -> callOtherApi());
                }

                @Override
                public void onError(Throwable e) {
                    switcher.showErrorView(e.toString(), () -> callOtherApi());
                }
            });
            return true;
        }
    }
}