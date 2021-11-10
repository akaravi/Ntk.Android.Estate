package ntk.android.estate.fragment;

import ntk.android.base.fragment.BaseFragment;
import ntk.android.estate.R;

public class NewEstateFragment3 extends BaseFragment {

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_3);
    }


    public boolean isValidForm() {
        return true;
    }
}