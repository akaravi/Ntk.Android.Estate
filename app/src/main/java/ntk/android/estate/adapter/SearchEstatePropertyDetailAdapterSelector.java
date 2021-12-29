package ntk.android.estate.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;

import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;

public class SearchEstatePropertyDetailAdapterSelector extends EstatePropertyDetailAdapterSelector{
    public SearchEstatePropertyDetailAdapterSelector(FragmentManager fragment, EstatePropertyDetailGroupModel item) {
        super(fragment, item);
    }

    @Override
    public VH CREATE_HOLDER(ViewGroup parent, int viewType) {
        return super.CREATE_HOLDER(parent, viewType);
    }
}
