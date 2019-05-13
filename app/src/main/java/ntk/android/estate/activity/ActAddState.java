package ntk.android.estate.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.EditText;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import ntk.android.estate.R;
import ntk.android.estate.utill.FontManager;

public class ActAddState extends AppCompatActivity {

    @BindViews({R.id.TxtTitleActAddState,
            R.id.TxtDescriptionActAddState,
            R.id.TxtBuildingAreaActAddState,
            R.id.TxtPriceActAddState,
            R.id.TxtNumberRoomActAddState,
            R.id.TxtYearOfConstructionActAddState,
            R.id.TxtDepositActAddState,
            R.id.TxtRentActAddState})
    List<EditText> Txts;

    @BindViews({R.id.SpinnerTypePriceActAddState,
            R.id.SpinnerTypeStateActAddState,
            R.id.SpinnerLocationActAddState,
            R.id.SpinnerTypeOrderActAddState})
    List<AppCompatSpinner> Spinners;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_state);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Txts.get(0).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txts.get(1).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txts.get(2).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txts.get(3).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txts.get(4).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txts.get(5).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txts.get(6).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txts.get(7).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
    }
}
