package ntk.android.estate.adapter;

import java.util.ArrayList;
import java.util.List;

class RecordStatusEnumAdapter {

    public static List<String> getList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("فعال");
        list.add("غیر فعال");
        list.add("حذف شده");
        list.add("تصمیم گیری");
        list.add("عدم تایید");
        list.add("آرشیو");
        return list;
    }
}
