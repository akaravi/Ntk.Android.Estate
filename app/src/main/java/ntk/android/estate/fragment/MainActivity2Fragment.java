package ntk.android.estate.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.gson.Gson;

import ntk.android.base.Extras;
import ntk.android.base.config.ErrorExceptionObserver;
import ntk.android.base.config.GenericErrors;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.estate.EstatePropertyService;
import ntk.android.base.utill.AppUtill;
import ntk.android.estate.R;
import ntk.android.estate.adapter.MainEstatePropertyAdapter;

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
        getData();
    }

    private void getData() {
        if (AppUtill.isNetworkAvailable(getContext())) {
            ServiceExecute.execute(new EstatePropertyService(getContext()).getAll(filter))
                    .subscribe(new ErrorExceptionObserver<EstatePropertyModel>(switcher::showErrorView) {

                        @Override
                        protected void SuccessResponse(ErrorException<EstatePropertyModel> response) {
                            MainEstatePropertyAdapter adapter = new MainEstatePropertyAdapter( response.ListItems);
                            RecyclerView rc = findViewById(R.id.rc);
                            rc.setAdapter(adapter);
                            rc.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                            SnapHelper snapHelper = new PagerSnapHelper();
                            snapHelper.attachToRecyclerView(rc);
                            ViewCompat.setNestedScrollingEnabled(rc, false);
                        }

                        @Override
                        protected Runnable tryAgainMethod() {
                            return () -> getData();
                        }
                    });


        } else {
            new GenericErrors().netError(switcher::showErrorView, () -> getData());

        }
    }
}

