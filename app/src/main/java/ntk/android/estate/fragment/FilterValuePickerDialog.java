package ntk.android.estate.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import es.dmoral.toasty.Toasty;
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
    Long from;
    Long to;

    public FilterValuePickerDialog(Context context) {
        this.context = context;
    }

    public FilterValuePickerDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public FilterValuePickerDialog setPreviousValue(FromToClass prev) {
        if (prev != null) {
            if (prev.getFrom() != null)
                from = (long) prev.getFrom();
            if (prev.getTo() != null)
                to = (long) prev.getTo();
        }
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
        TextView titleTv = bottomSheetDialog.findViewById(R.id.valueTitleTv);
        TextInputEditText fromEt = bottomSheetDialog.findViewById(R.id.fromEditText);
        TextInputEditText toEt = bottomSheetDialog.findViewById(R.id.toEditText);
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
        //set prev value
        if (from != null)
            fromEt.setText(String.valueOf(from));
        if (to != null)
            toEt.setText(String.valueOf(to));

        //add click listener for add
        filterBtn.setOnClickListener(view -> {
            FromToClass fromToClass = new FromToClass();
            String fromStr = ((TextInputEditText) bottomSheetDialog.findViewById(R.id.fromEditText)).getText().toString();
            String toStr = ((TextInputEditText) bottomSheetDialog.findViewById(R.id.toEditText)).getText().toString();


            String t = "";
            Long fromLong = 0L;
            Long toLong = 0L;
            if (!fromStr.equals("")) {
                fromLong = Long.valueOf(NumberTextWatcherForThousand.trimCommaOfString(fromStr));
                fromToClass.setFrom(fromLong);
                t += ("از " + fromStr + "  ");
            } else
                fromToClass.setFrom(null);
            if (!toStr.equals("")) {
                toLong = Long.valueOf(NumberTextWatcherForThousand.trimCommaOfString(toStr));
                fromToClass.setTo(toLong);
                t += ("تا " + toStr);
            } else
                fromToClass.setTo(null);
            et.setText(t);
            if (toLong!=0&&fromLong > toLong) {
                Toasty.error(context, "اعداد انتخابی به نادرستی انتخاب شده است").show();
            } else {
                consumer.accept(fromToClass);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }


}
