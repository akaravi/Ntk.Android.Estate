package ntk.android.estate.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.estate.EstatePropertyTypeService;
import ntk.android.estate.R;
import ntk.android.estate.adapter.EstatePropertyTypeAdapter;

public class NewEstateFragment2 extends BaseFragment {

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.fragment_new_estate_2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    private void getData() {
        ServiceExecute.execute(new EstatePropertyTypeService(getContext()).getAll(new FilterModel()))
                .subscribe(new NtkObserver<>() {
                    @Override
                    public void onNext(@NonNull ErrorException<EstatePropertyTypeModel> response) {
                        EstatePropertyTypeAdapter adapter = new EstatePropertyTypeAdapter(response.ListItems);
                        RecyclerView rc = findViewById(R.id.estateTypeRc);
                        rc.setAdapter(adapter);
                        rc.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public boolean isValidForm() {
        return true;
    }
}
