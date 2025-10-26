package ntk.android.estate.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.news.NewsCategoryModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;


public class NewsCategoryContentAdapter extends BaseRecyclerAdapter<NewsCategoryModel, NewsCategoryContentAdapter.ViewHolder> {

    private final Context context;

    public NewsCategoryContentAdapter(Context context, List<NewsCategoryModel> arrayList) {
        super(arrayList);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflate(viewGroup, R.layout.row_recycler_category_content);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        NewsCategoryModel item = getItem(position);
        holder.Title.setText(item.Title);
        loadImage(item.LinkMainImageIdSrc, holder.Img, null);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Title;
        ImageView Img;

        public ViewHolder(View view) {
            super(view);
            Title = view.findViewById(R.id.txt_category_content);
            Img = view.findViewById(R.id.img_category_content);
            Title.setTypeface(FontManager.T1_Typeface(context));
        }
    }
}
