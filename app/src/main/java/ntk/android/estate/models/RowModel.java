package ntk.android.estate.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ntk.android.base.entitymodel.base.FilterModel;

public class RowModel {
    @SerializedName("HeaderString")
    String HeaderString;
    @SerializedName("RecyclerviewItems")
    List<RecyclerItemModel> RecyclerviewItems;
    @SerializedName("Filter")
    FilterModel Filter;
}
