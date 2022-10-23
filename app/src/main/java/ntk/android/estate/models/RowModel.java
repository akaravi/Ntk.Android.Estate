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
        return "{\"ListItems\":[{\"HeaderString\":\"املاکقبرصشمالی\",\"Items\":[{\"Id\":\"6309be1b25056b4b2b9b9b98\",\"Image\":\"https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png\",\"Title\":\"واحددوخوابدرقبرسشمالی\"},{\"Id\":\"6309be1b25056b4b2b9b9b98\",\"Image\":\"https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png\",\"Title\":\"واحددوخوابدرقبرسشمالی\"}]},{\"HeaderString\":\"املاکقبرصشمالی2\",\"Items\":[{\"Id\":\"6309be1b25056b4b2b9b9b98\",\"Image\":\"https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png\",\"Title\":\"واحددوخوابدرقبرسشمالی\"},{\"Id\":\"6309be1b25056b4b2b9b9b98\",\"Image\":\"https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png\",\"Title\":\"واحددوخوابدرقبرسشمالی\"}]}]}";
//        RuntimeJsonModel s = new RuntimeJsonModel();
//        s.ListItems = new ArrayList<>();
//        {
//            RowModel r = new RowModel();
//            r.HeaderString = "املاک قبرص شمالی";
//            r.Items = new ArrayList<>();
//            {
//                RecyclerItemModel i = new RecyclerItemModel();
//                i.Image = "https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png";
//                i.Title = "واحد دو خواب در قبرس شمالی";
//                i.Id = "6309be1b25056b4b2b9b9b98";
//                r.Items.add(i);
//            }
//            {
//                RecyclerItemModel i = new RecyclerItemModel();
//                i.Image = "https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png";
//                i.Title = "واحد دو خواب در قبرس شمالی";
//                i.Id = "6309be1b25056b4b2b9b9b98";
//                r.Items.add(i);
//            }
//            s.ListItems.add(r);
//        }
//        {
//            RowModel r = new RowModel();
//            r.HeaderString = "املاک قبرص شمالی 2";
//            r.Items = new ArrayList<>();
//            {
//                RecyclerItemModel i = new RecyclerItemModel();
//                i.Image = "https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png";
//                i.Title = "واحد دو خواب در قبرس شمالی";
//                i.Id = "6309be1b25056b4b2b9b9b98";
//                r.Items.add(i);
//            }
//            {
//                RecyclerItemModel i = new RecyclerItemModel();
//                i.Image = "https://apifile.ir/thumbnails/Estate/30/26c10a5e2b9440d69c85e11687d9e577.304x304.png";
//                i.Title = "واحد دو خواب در قبرس شمالی";
//                i.Id = "6309be1b25056b4b2b9b9b98";
//                r.Items.add(i);
//            }
//            s.ListItems.add(r);
//        }
//        return new Gson().toJson(s);
    }
}