package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;

class PropertyDetailGroupsAdapter extends BaseRecyclerAdapter<EstatePropertyDetailGroupModel, PropertyDetailGroupsAdapter.VH> {
    public PropertyDetailGroupsAdapter(ArrayList<EstatePropertyDetailGroupModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

    }

    public class VH extends RecyclerView.ViewHolder {
        public VH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
