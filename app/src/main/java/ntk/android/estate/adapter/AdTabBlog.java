package ntk.android.estate.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.balysv.materialripple.MaterialRippleLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.estate.R;
import ntk.android.estate.event.EvHtmlBodyBlog;
import ntk.android.estate.utill.FontManager;
import ntk.base.api.blog.entity.BlogContentOtherInfo;

public class AdTabBlog extends RecyclerView.Adapter<AdTabBlog.ViewHolder> {

    private List<BlogContentOtherInfo> arrayList;
    private Context context;

    public AdTabBlog(Context context, List<BlogContentOtherInfo> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler_tab, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Btn.setText(arrayList.get(position).Title);
        if (arrayList.get(position).TypeId == 0) {
            EventBus.getDefault().post(new EvHtmlBodyBlog(arrayList.get(position).HtmlBody));
        }
        holder.Ripple.setOnClickListener(v -> EventBus.getDefault().post(new EvHtmlBodyBlog(arrayList.get(position).HtmlBody)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.BtnRecyclerTab)
        Button Btn;

        @BindView(R.id.RippleBtnRecyclerTab)
        MaterialRippleLayout Ripple;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Btn.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        }
    }
}
