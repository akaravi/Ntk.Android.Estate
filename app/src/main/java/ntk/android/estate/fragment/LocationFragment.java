package ntk.android.estate.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ntk.android.base.fragment.BaseFragment;
import ntk.android.estate.R;

class LocationFragment extends BaseFragment {
    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_location);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
