package ntk.android.estate.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import java.util.List;

import ntk.android.base.entitymodel.estate.EstateContractModel;

public class EditEstateContractAdapter extends EstateConstractAdapter{
    public EditEstateContractAdapter(List<EstateContractModel> list) {
        super(list);
    }

    class EditVH extends EstateConstractAdapter.VC {
        View delete;
        public EditVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
