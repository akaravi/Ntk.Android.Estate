package ntk.android.estate.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import ntk.android.base.Extras;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.estate.R;

public class MainActivity2Fragment extends BaseFragment {
    FilterModel filter;

    public static BaseFragment newInstance(FilterModel filter, int color) {
        MainActivity2Fragment fragment = new MainActivity2Fragment();
        Bundle args = new Bundle();
        args.putString(Extras.EXTRA_FIRST_ARG, new Gson().toJson(filter));
        args.putInt(Extras.EXTRA_SECOND_ARG, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_main_activiy_2);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filter = new Gson().fromJson(getArguments().getString(Extras.EXTRA_FIRST_ARG), FilterModel.class);
        int color = getArguments().getInt(Extras.EXTRA_SECOND_ARG);
        findViewById(R.id.rv).setBackgroundColor(ContextCompat.getColor(getContext(), color));

        }
    }

