package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.base.view.NViewUtils;
import ntk.android.estate.R;

public class EstatePropertiesInDetailAdapter extends EstatePropertyAdapter{
    int width;
    int margin=-1;
    public EstatePropertiesInDetailAdapter(List<EstatePropertyModel> models) {
        super(models);
        width=getScreenWidth();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(parent, R.layout.row_recycler_estateproperty);
        if (margin==-1)
            margin= NViewUtils.dpToPx(v.getContext(),50);
        v.getLayoutParams().width=width-margin;
        return new VH(v);
    }
}
