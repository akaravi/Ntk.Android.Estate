package ntk.android.estate.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ntk.android.base.entitymodel.base.FilterModel;

public class RowModel {
    @SerializedName("HeaderString")
    public String HeaderString;
    @SerializedName("Items")
    public List<RecyclerItemModel> Items;
    @SerializedName("Filter")
    public FilterModel Filter;

    public static String Id() {
//{"listItems":[{"HeaderString":"املاک قبرص شمالی","Items":[{"Image":"https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png","Title":"واحد دو خواب در قبرس شمالی","Id":"6309be1b25056b4b2b9b9b98"},{"Image":"https://apifile.ir/thumbnails/Estate/30/8c1c39ea46804df4b214375cb54d13e4.304x304.png","Title":"شهرک آپارتمانی و ویلایی در لانگ بیچ","Id":"6301d9137910fcb6e37f1363"},{"Image":"https://apifile.ir/thumbnails/Estate/30/10e6db6347ad45aeb1b916bdee521296.304x304.jpg","Title":"آپارتمان در قبرس فاماگوستا ","Id":"62e5227d2742bfd25a0f3a25"},{"Image":"https://apifile.ir/thumbnails/Estate/30/8c1c39ea46804df4b214375cb54d13e4.304x304.png","Title":"شهرک آپارتمانی و ویلایی در لانگ بیچ","Id":"6301d9137910fcb6e37f1363"},{"Image":"https://apifile.ir/thumbnails/Estate/30/8c1c39ea46804df4b214375cb54d13e4.304x304.png","Title":"شهرک آپارتمانی و ویلایی در لانگ بیچ","Id":"6301d9137910fcb6e37f1363"}]},{"HeaderString":"املاک لاکچری","Filter":{"SortType":2,"SortColumn":"Cratedate","Filters":[{"PropertyName":"eatate",Value:"2222"},{"PropertyName":"eatate",Value:"2222"}]}}]}
        RuntimeJsonModel s = new RuntimeJsonModel();
        s.ListItems = new ArrayList<>();
        {
            RowModel r = new RowModel();
            r.HeaderString = "املاک قبرص شمالی";
            r.Items = new ArrayList<>();
            {
                RecyclerItemModel i = new RecyclerItemModel();
                i.Image = "https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png";
                i.Title = "واحد دو خواب در قبرس شمالی";
                i.Id = "6309be1b25056b4b2b9b9b98";
                r.Items.add(i);
            }
            {
                RecyclerItemModel i = new RecyclerItemModel();
                i.Image = "https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png";
                i.Title = "واحد دو خواب در قبرس شمالی";
                i.Id = "6309be1b25056b4b2b9b9b98";
                r.Items.add(i);
            }
            s.ListItems.add(r);
        }
        {
            RowModel r = new RowModel();
            r.HeaderString = "املاک قبرص شمالی 2";
            r.Items = new ArrayList<>();
            {
                RecyclerItemModel i = new RecyclerItemModel();
                i.Image = "https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png";
                i.Title = "واحد دو خواب در قبرس شمالی";
                i.Id = "6309be1b25056b4b2b9b9b98";
                r.Items.add(i);
            }
            {
                RecyclerItemModel i = new RecyclerItemModel();
                i.Image = "https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png";
                i.Title = "واحد دو خواب در قبرس شمالی";
                i.Id = "6309be1b25056b4b2b9b9b98";
                r.Items.add(i);
            }
            s.ListItems.add(r);
        }
        return  new Gson().toJson(s);
    }
}