package ntk.android.estate.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import ntk.android.base.entitymodel.estate.EstatePropertyModel;
import ntk.android.estate.R;

public class HorizontalEstatePropertyAdapter extends EstatePropertyAdapter{
    int width;
    public HorizontalEstatePropertyAdapter( List<EstatePropertyModel> models) {
        super( models);
        width=getScreenWidth();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=(inflate(parent, R.layout.row_recycler_estateproperty));
        v.getLayoutParams().width=( width- (width/10));
        return new VH(v);
    }
}
