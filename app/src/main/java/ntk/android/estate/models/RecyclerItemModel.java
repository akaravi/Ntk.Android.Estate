package ntk.android.estate.models;

import com.google.gson.annotations.SerializedName;

import ntk.android.base.entitymodel.base.FilterModel;

public class RecyclerItemModel {
    @SerializedName("Image")
    public String Image;
    @SerializedName("Title")
    public String Title;
    @SerializedName("Id")
    public String Id;
    @SerializedName("Filter")
    public FilterModel Filter;

}
