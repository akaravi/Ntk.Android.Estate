package ntk.android.estate.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import java9.util.function.Function;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.article.ArticleCategoryModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class ArticleCategoryAdapter extends BaseRecyclerAdapter<ArticleCategoryModel, ArticleCategoryAdapter.ViewHolder> {
    Function<ArticleCategoryModel, Void> function;

    public ArticleCategoryAdapter(List<ArticleCategoryModel> list, Function<ArticleCategoryModel, Void> func) {
        super(list);
        function = func;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleCategoryAdapter.ViewHolder(inflate(parent, R.layout.common_category));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArticleCategoryModel item = getItem(position);
        holder.tv.setText(item.Title);
        holder.itemView.setOnClickListener(v -> function.apply(item));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.textView);
            tv.setTypeface(FontManager.T1_Typeface(itemView.getContext()));
        }
    }
}
