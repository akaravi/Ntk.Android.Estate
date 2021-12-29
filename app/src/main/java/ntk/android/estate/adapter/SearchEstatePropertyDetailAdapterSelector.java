package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;

import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
import ntk.android.estate.R;

public class SearchEstatePropertyDetailAdapterSelector extends EstatePropertyDetailAdapterSelector{
    public SearchEstatePropertyDetailAdapterSelector(FragmentManager fragment, EstatePropertyDetailGroupModel item) {
        super(fragment, item);
    }

    @Override
    public VH CREATE_HOLDER(ViewGroup parent, int viewType) {
        if (viewType == 0)//as String
            return new StringSearchVH(inflate(parent, R.layout.row_property_detail_stirng_type), viewType);
        if (viewType == 1)//as int
            return new IntegerSearchVH(inflate(parent, R.layout.row_property_detail_stirng_type), viewType);
        if (viewType == 2)
            return new BooleanSearchVH(inflate(parent, R.layout.row_property_detail_boolean_type), viewType);
        if (viewType == 3)//as float
            return new FloatSearchVH(inflate(parent, R.layout.row_property_detail_stirng_type), viewType);
        if (viewType == 4)//as date
            return new DateSearchVH(inflate(parent, R.layout.row_property_detail_date_type), viewType);
        if (viewType == 5)//as multiLine text
            return new MultiLineVH(inflate(parent, R.layout.row_property_detail_textarea_type), viewType);
        else if (viewType == 11)
            return new MultiChoiceSearchVH(inflate(parent, R.layout.row_property_detail_stirng_type), 11);
        else
            return new SingleChoiceVH(inflate(parent, R.layout.row_property_detail_stirng_type), 12);
    }

    private class StringSearchVH extends VH {
        public StringSearchVH(View inflate, int viewType) {
            super();
        }
    }
}
