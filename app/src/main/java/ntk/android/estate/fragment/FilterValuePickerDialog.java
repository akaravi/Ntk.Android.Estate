package ntk.android.estate.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java9.util.function.Consumer;
import ntk.android.base.appclass.FromToClass;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;
import ntk.android.estate.view.NumberTextWatcherForThousand;

public class FilterValuePickerDialog {
    Context context;
    String title;
    Consumer consumer;
    TextInputEditText et;

    public FilterValuePickerDialog(Context context) {
        this.context = context;
    }

    public FilterValuePickerDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public FilterValuePickerDialog setCallBack(Consumer<FromToClass> consumer) {
        this.consumer = consumer;
        return this;
    }

    public FilterValuePickerDialog setLable(TextInputEditText view) {
        this.et = view;
        return this;
    }

    public void show() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.search_value_picker_dialog);
        Typeface t1 = FontManager.T1_Typeface(context);
        TextView titleTv = (TextView) bottomSheetDialog.findViewById(R.id.valueTitleTv);
        TextInputEditText fromEt = (TextInputEditText) bottomSheetDialog.findViewById(R.id.fromEditText);
        TextInputEditText toEt = (TextInputEditText) bottomSheetDialog.findViewById(R.id.toEditText);
        MaterialButton filterBtn = bottomSheetDialog.findViewById(R.id.setFilterBtn);
        //set font
        titleTv.setTypeface(t1);
        filterBtn.setTypeface(t1);
        ((TextInputLayout) bottomSheetDialog.findViewById(R.id.toTextInput)).setTypeface(t1);
        ((TextInputLayout) bottomSheetDialog.findViewById(R.id.fromTextInput)).setTypeface(t1);
        fromEt.setTypeface(t1);
        toEt.setTypeface(t1);
        //add separator
        fromEt.addTextChangedListener(new NumberTextWatcherForThousand(fromEt));
        toEt.addTextChangedListener(new NumberTextWatcherForThousand(toEt));
        titleTv.setText(title);
        //add click listener for add
        filterBtn.setOnClickListener(view -> {
            FromToClass fromToClass = new FromToClass();
            String fromStr = ((TextInputEditText) bottomSheetDialog.findViewById(R.id.fromEditText)).getText().toString();
            String toStr = ((TextInputEditText) bottomSheetDialog.findViewById(R.id.toEditText)).getText().toString();


            String t = "";

                if (!fromStr.equals("")) {
                    fromToClass.setFrom(Long.getLong(NumberTextWatcherForThousand.trimCommaOfString(fromStr)));
                    t += ("از " + fromStr+"  ");
                }
                if (!toStr.equals("")) {
                    fromToClass.setTo(Long.getLong(NumberTextWatcherForThousand.trimCommaOfString(toStr)));
                    t +=("تا " + toStr);
                }
            et.setText(t);

            consumer.accept(fromToClass);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }


}
