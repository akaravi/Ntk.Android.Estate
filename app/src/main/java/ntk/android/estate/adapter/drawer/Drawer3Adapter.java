package ntk.android.estate.adapter.drawer;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.List;

import ntk.android.base.dtomodel.theme.DrawerChildThemeDtoModel;
import ntk.android.estate.R;

public class Drawer3Adapter extends DrawerAdapter {
    int color = -1;

    public Drawer3Adapter(Context context, List<DrawerChildThemeDtoModel> children, FlowingDrawer drawer) {
        super(context, children, drawer);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mholder, int position) {
        super.onBindViewHolder(mholder, position);
        if (position > 0) {
            ViewHolder holder = (ViewHolder) mholder;
            if (color == -1)
                color = ContextCompat.getColor(holder.itemView.getContext(), R.color.main3_primary_text);
            holder.title.setTextColor(color);
            holder.icon.setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }
}
