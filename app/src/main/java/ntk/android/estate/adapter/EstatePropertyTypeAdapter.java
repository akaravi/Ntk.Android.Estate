package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyTypeModel;
import ntk.android.estate.R;

public class EstatePropertyTypeAdapter extends BaseRecyclerAdapter<EstatePropertyTypeModel, EstatePropertyTypeAdapter.VH> {
    public EstatePropertyTypeAdapter(List<EstatePropertyTypeModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public EstatePropertyTypeAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent,R.layout.row_property_type_row));
    }

    @Override
    public void onBindViewHolder(@NonNull EstatePropertyTypeAdapter.VH holder, int position) {

    }

    public class VH extends RecyclerView.ViewHolder {
        public VH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
