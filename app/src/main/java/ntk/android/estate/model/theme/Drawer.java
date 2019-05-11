package ntk.android.estate.model.theme;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Drawer {

    @SerializedName("Type")
    public int Type;

    @SerializedName("DrawerChilds")
    public List<DrawerChild> Child;
}