package ntk.android.estate.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.Arrays;

import ntk.android.base.activity.BaseActivity;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstatePropertyAdapter;
import ntk.android.estate.fragment.SelectProviceDialog;

public class TestActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SelectProviceDialog dialog = SelectProviceDialog.START_DIALOG(
                selectedModel -> {
                    if (selectedModel!=null) {

                    }
                });
        dialog.show(getSupportFragmentManager(), "dialog");
    }
}
