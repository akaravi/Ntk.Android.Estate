package ntk.android.estate.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class EstatePropertyDetailGroupAdapterSelector extends BaseRecyclerAdapter<EstatePropertyDetailGroupModel, EstatePropertyDetailGroupAdapterSelector.VH> {
    FragmentManager fragmentManager;

    public EstatePropertyDetailGroupAdapterSelector(FragmentManager fra, List<EstatePropertyDetailGroupModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.property_detail_group_selector));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstatePropertyDetailGroupModel item = getItem(position);
        holder.title.setText(item.Title);
        holder.rc.setLayoutManager(new GridLayoutManager(holder.getContext(), 2));
        holder.rc.setAdapter(new EstatePropertyDetailAdapterSelector(fragmentManager,item));
        if (position == list.size() - 1) {
            holder.itemSeparator.setVisibility(View.GONE);
            holder.lastItemPadding.setVisibility(View.VISIBLE);
        } else {
            holder.itemSeparator.setVisibility(View.VISIBLE);
            holder.lastItemPadding.setVisibility(View.GONE);
        }
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView title;
        RecyclerView rc;
        View itemSeparator;
        View lastItemPadding;

        public VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTv);
            rc = itemView.findViewById(R.id.estateDetailGroupValueRc);
            itemSeparator = itemView.findViewById(R.id.itemSeparator);
            lastItemPadding = itemView.findViewById(R.id.lastItemPadding);
            Typeface typeface = FontManager.T1_Typeface(itemView.getContext());
            title.setTypeface(typeface);
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }
}
