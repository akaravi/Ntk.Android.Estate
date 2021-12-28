package ntk.android.estate.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import ntk.android.base.adapter.SortingFilterAdapter;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

public class FilterValuePickerDialog {
    Context context;

    public FilterValuePickerDialog(Context context) {
        this.context = context;
    }

    public void show(){
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(R.layout.search_value_picker_dialog);
            bottomSheetDialog.show();
        }
}
