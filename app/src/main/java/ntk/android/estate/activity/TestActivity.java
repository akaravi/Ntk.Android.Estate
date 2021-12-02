package ntk.android.estate.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

import ntk.android.base.activity.BaseActivity;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstatePropertyAdapter;

public class TestActivity extends NewEstateActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abstraction_list);
        RecyclerView rc = findViewById(R.id.recycler);
        rc.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        EstatePropertyAdapter adapter = new EstatePropertyAdapter(this, Arrays.asList(new EstatePropertyModel(), new EstatePropertyModel()));
        rc.setAdapter(adapter);
    }
}
