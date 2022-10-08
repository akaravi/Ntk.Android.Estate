package ntk.android.estate.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ntk.android.base.entitymodel.base.FilterModel;

public class RowModel {
    @SerializedName("HeaderString")
    String HeaderString;
    @SerializedName("Items")
    List<RecyclerItemModel> Items;

}
