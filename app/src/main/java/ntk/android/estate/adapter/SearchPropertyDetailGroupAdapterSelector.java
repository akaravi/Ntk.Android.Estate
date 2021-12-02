package ntk.android.estate.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
import ntk.android.estate.R;

public class SearchPropertyDetailGroupAdapterSelector extends BaseRecyclerAdapter<EstatePropertyDetailGroupModel, SearchPropertyDetailGroupAdapterSelector.VH> {
    ViewGroup root;
    private FragmentManager fragmentManager;

    public SearchPropertyDetailGroupAdapterSelector(List<EstatePropertyDetailGroupModel> list, ViewGroup rootView, FragmentManager frag) {
        super(list);
        root = rootView;
        fragmentManager = frag;
    }


    @NonNull
    @Override
    public SearchPropertyDetailGroupAdapterSelector.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchPropertyDetailGroupAdapterSelector.VH(inflate(parent, R.layout.search_property_detail_group_selector));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPropertyDetailGroupAdapterSelector.VH holder, int position) {
        EstatePropertyDetailGroupModel item = getItem(position);
        holder.title.setText(item.Title);
        holder.rc.setLayoutManager(new GridLayoutManager(holder.getContext(), 2));
        holder.rc.setAdapter(new EstatePropertyDetailAdapterSelector(fragmentManager, item));

    }

    public class VH extends RecyclerView.ViewHolder {

        TextView title;
        RecyclerView rc;
        ImageView icon;
        LinearLayout expnader;

        public VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.contractTypeExpandTv);
            rc = itemView.findViewById(R.id.contractsRc);
            icon = itemView.findViewById(R.id.contractTypeExpandIcon);
            expnader = itemView.findViewById(R.id.contractTypeExpander);
            expnader.setOnClickListener(view -> {
                if (rc.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(root);
                    rc.setVisibility(View.VISIBLE);
                    icon.setRotation(0);
                } else if (rc.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(root);
                    rc.setVisibility(View.GONE);
                    icon.setRotation(180);
                }
            });
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }
}