package ntk.android.estate.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstatePropertyAdapter;

public class MainActivity extends BaseActivity {
    FilterModel row1;
    FilterModel row2;
    FilterModel row3;
    FilterModel row4;
    FilterModel row5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getdata(row1,findViewById(R.id.rc1));
        getdata(row2,findViewById(R.id.rc2));
        getdata(row3,findViewById(R.id.rc3));
        getdata(row4,findViewById(R.id.rc4));

    }

    private void getdata(FilterModel req, RecyclerView rc) {
        ServiceExecute.execute(new EstatePropertyService(this).getAll(req))
                .subscribe(new ErrorExceptionObserver<EstatePropertyModel>(switcher::showErrorView) {

                    @Override
                    protected void SuccessResponse(ErrorException<EstatePropertyModel> response) {
                        EstatePropertyAdapter adapter = new EstatePropertyAdapter(MainActivity.this, response.ListItems);
                        rc.setAdapter(adapter);
                        rc.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                    }

                    @Override
                    protected Runnable tryAgainMethod() {
                        return () -> getdata(req, rc);
                    }
                });


    }
}