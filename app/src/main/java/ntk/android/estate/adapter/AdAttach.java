package ntk.android.estate.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.estate.R;
import ntk.android.estate.event.EvRemoveAttach;
import ntk.android.estate.utill.FontManager;

public class AdAttach extends RecyclerView.Adapter<AdAttach.ViewHolder> {

    private List<String> arrayList;
    private Context context;

    public AdAttach(Context context, List<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_attach, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Lbl.setText(arrayList.get(position));
        holder.Img.setOnClickListener(v -> EventBus.getDefault().post(new EvRemoveAttach(position)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lblTitleRecyclerAttach)
        TextView Lbl;

        @BindView(R.id.imgRemoveRecyclerAttach)
        ImageView Img;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Lbl.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        }
    }
}
