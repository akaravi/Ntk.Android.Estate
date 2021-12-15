package ntk.android.estate.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import ntk.android.base.activity.abstraction.AbstractMainActivity;
import ntk.android.base.entitymodel.base.FilterModel;

abstract class BaseMainActivity extends AbstractMainActivity {
    FilterModel row1;
    FilterModel row2;
    FilterModel row3;
    FilterModel row4;
    FilterModel row5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generateFilterModel();
    }

    private void generateFilterModel() {
        row1=new FilterModel();
        row2=new FilterModel();
        row3=new FilterModel();
        row4=new FilterModel();
        row5=new FilterModel();
    }
}
