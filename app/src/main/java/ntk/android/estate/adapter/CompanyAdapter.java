package ntk.android.estate.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstateCompanyModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.activity.CompanyDetailActivity;

public class CompanyAdapter extends BaseRecyclerAdapter<EstateCompanyModel, CompanyAdapter.VH> {

    public CompanyAdapter(List<EstateCompanyModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompanyAdapter.VH(inflate(parent, R.layout.row_company));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        EstateCompanyModel item = getItem(position);
        loadImage(item.LinkMainImageIdSrc, holder.img);
        holder.title.setText(item.Title != null ? item.Title : "نا مشخص");
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CompanyDetailActivity.class);
            intent.putExtra(Extras.EXTRA_FIRST_ARG, item.Id);
            v.getContext().startActivity(intent);
        });
    }

    public class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;

        public VH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.lblTitle);
            title.setTypeface(FontManager.T1_Typeface(itemView.getContext()));
        }
    }
}
