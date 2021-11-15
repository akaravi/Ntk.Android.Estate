package ntk.android.estate.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ntk.android.base.activity.BaseActivity;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.estate.R;
import ntk.android.estate.fragment.NewEstateFragment1;
import ntk.android.estate.fragment.NewEstateFragment2;
import ntk.android.estate.fragment.NewEstateFragment3;
import ntk.android.estate.fragment.NewEstateFragment4;
import ntk.android.estate.fragment.NewEstateFragment5;

public class NewEstateActivity extends BaseActivity {
    EstatePropertyModel model;
    TextView title;
    private int stepNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_estate);
        model = new EstatePropertyModel();
        title = findViewById(R.id.txtToolbarTitle);
        findViewById(R.id.imgToolbarBack).setOnClickListener(view -> finish());
        findViewById(R.id.backBtn).setOnClickListener(view -> {
            onBackPressed();
        });
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
        });
        showFragment1();
    }

    private void showFragment1() {
        stepNumber = 1;
        title.setText("مشخصات آگهی");
        findViewById(R.id.backBtn).setVisibility(View.GONE);
        NewEstateFragment1 fragment = new NewEstateFragment1();
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                showFragment2();
        });
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    private void showFragment2() {
        stepNumber = 2;
        title.setText("مشخصات ملک");
        findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        NewEstateFragment2 fragment = new NewEstateFragment2();
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                showFragment3();
        });
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    private void showFragment3() {
        stepNumber = 3;
        title.setText("جزئیات و مشخصات");
        findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        NewEstateFragment3 fragment = new NewEstateFragment3();
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                showFragment4();
        });
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    private void showFragment4() {
        stepNumber = 4;
        title.setText("تصاویر ملک"); title.setText("شرایط معامله");
        findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        NewEstateFragment5 fragment = new NewEstateFragment5();
        findViewById(R.id.continueBtn).setOnClickListener(view -> {
            if (fragment.isValidForm())
                showFragment4();
        });
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }

    @Override
    public void onBackPressed() {
        stepNumber--;
        if (stepNumber == 3)
            showFragment3();
        else if (stepNumber == 2)
            showFragment2();
        else if (stepNumber == 1)
            showFragment1();
        else super.onBackPressed();
    }

    public void showErrorView() {
    }

    public void showProgress() {
    }

    public void showContent() {
    }

    public EstatePropertyModel model() {
        return model;
    }
}
