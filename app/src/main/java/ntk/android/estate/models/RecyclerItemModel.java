package ntk.android.estate.models;

import com.google.gson.annotations.SerializedName;

import ntk.android.base.entitymodel.base.FilterModel;

public class RecyclerItemModel {
    @SerializedName("Image")
    String Image;
    @SerializedName("Title")
    String Title;
    @SerializedName("Id")
    String Id;
    @SerializedName("Filter")
    FilterModel Filter;

}
