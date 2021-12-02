package ntk.android.estate.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailValueModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

class EstatePropertyDetailAdapterSelector extends BaseRecyclerAdapter<EstatePropertyDetailValueModel, EstatePropertyDetailAdapterSelector.VH> {
    FragmentManager frag;

    public EstatePropertyDetailAdapterSelector(FragmentManager fragment, EstatePropertyDetailGroupModel item) {
        super(item.PropertyValues);
        frag = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        EstatePropertyDetailValueModel item = list.get(position);
        if (item.PropertyDetail.InputDataType == 0) {
            //is selectable String
            if (item.PropertyDetail.ConfigValueForceUseDefaultValue)
                //is as multiple choose
                if (item.PropertyDetail.ConfigValueMultipleChoice)
                    return 11;
                else//is as single choose;
                    return 12;
            else
                return 0;
        }
        return item.PropertyDetail.InputDataType;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(parent, viewType).CREATE_HOLDER(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bindToView(getItem(position), position);
    }

    public class VH extends RecyclerView.ViewHolder {
        protected int viewType;

        public VH(@NonNull View itemView, int type) {
            super(itemView);
            viewType = type;
        }

        public VH CREATE_HOLDER(ViewGroup parent, int viewType) {
            if (viewType == 0)//as String
                return new StringVH(inflate(parent, R.layout.row_property_detail_stirng_type), viewType);
            if (viewType == 1)//as int
                return new IntegerVH(inflate(parent, R.layout.row_property_detail_stirng_type), viewType);
            if (viewType == 2)
                return new BooleanVH(inflate(parent, R.layout.row_property_detail_boolean_type), viewType);
            if (viewType == 3)//as float
                return new FloatVH(inflate(parent, R.layout.row_property_detail_stirng_type), viewType);
            if (viewType == 4)//as date
                return new DateVH(inflate(parent, R.layout.row_property_detail_date_type), viewType);
            if (viewType==5)//as multiLine text
                return new MultiLineVH(inflate(parent, R.layout.row_property_detail_textarea_type), viewType);
            else if (viewType==11)
                return new MultiChoiceVH(inflate(parent, R.layout.row_property_detail_stirng_type),11);
            else
                return new SingleChoiceVH(inflate(parent, R.layout.row_property_detail_stirng_type),12);
        }

        public Context getContext() {
            return itemView.getContext();
        }

        public void bindToView(EstatePropertyDetailValueModel item, int position) {

        }


    }


     class StringVH extends VH {
        TextInputEditText editText;
        TextInputLayout inputLayout;
        MyCustomEditTextListener textChangeListener;

        public StringVH(View itemView, int viewType) {
            super(itemView, viewType);
            Typeface tf = FontManager.T1_Typeface(getContext());

            inputLayout = itemView.findViewById(R.id.inputLayout);
            editText = itemView.findViewById(R.id.inputEditText);
            editText.setInputType(inputType());
            editText.setTypeface(tf);
            inputLayout.setTypeface(tf);
            if (viewType != 4) {
                textChangeListener = new MyCustomEditTextListener();
                editText.setFocusable(true);
                editText.addTextChangedListener(textChangeListener);
            } else {
                editText.setFocusable(false);
            }

        }

        public int inputType() {
            return InputType.TYPE_CLASS_TEXT;
        }

        @Override
        public void bindToView(EstatePropertyDetailValueModel item, int position) {
            super.bindToView(item, position);
            inputLayout.setHint(item.PropertyDetail.Title);
            if (item.Value != null)
                editText.setText(item.Value);
            textChangeListener.updatePosition(position);
        }
    }
     class SingleChoiceVH extends VH {
        public SingleChoiceVH(View inflate, int type) {
            super(inflate, type);
        }
    }
    private class MultiChoiceVH extends VH {
        public MultiChoiceVH(View inflate, int type) {
            super(inflate, type);
        }
    }


    private class IntegerVH extends StringVH {
        public IntegerVH(View inflate, int type) {
            super(inflate, type);
        }

        @Override
        public int inputType() {
            return InputType.TYPE_CLASS_NUMBER;
        }
    }

    private class FloatVH extends StringVH {
        public FloatVH(View inflate, int type) {
            super(inflate, type);
        }

        @Override
        public int inputType() {
            return InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }
    }

    class MultiLineVH extends StringVH {
        public MultiLineVH(View inflate, int type) {
            super(inflate, type);
        }

        @Override
        public int inputType() {
            return InputType.TYPE_TEXT_FLAG_MULTI_LINE;
        }
    }

    private class DateVH extends StringVH {

        public DateVH(View inflate, int viewType) {
            super(inflate, viewType);
        }

        @Override
        public void bindToView(EstatePropertyDetailValueModel item, int position) {
            inputLayout.setHint(item.PropertyDetail.Title);
            if (item.Value != null)
                editText.setText(item.Value);
            //add clickListener
            editText.setOnClickListener(view -> {
                PersianCalendar persianCalendar = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        (v, year, monthOfYear, dayOfMonth) -> {
                            list.get(position).Value = (year + "/" + monthOfYear + "/" + dayOfMonth);
                            NumberFormat formatter = new DecimalFormat("00");
                            editText.setText(year + "/" + formatter.format(monthOfYear + 1) + "/" + formatter.format(dayOfMonth));
                        },
                        persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay()
                );
                datePickerDialog.show(frag, "Datepickerdialog");
            });
        }
    }

    private class BooleanVH extends VH {
        MaterialCheckBox checkBox;
        TextView textView;

        public BooleanVH(View inflate, int type) {
            super(inflate, type);
            Typeface tf = FontManager.T1_Typeface(getContext());
            checkBox = inflate.findViewById(R.id.checkBox);
            textView = inflate.findViewById(R.id.txt);
            textView.setTypeface(tf);
            inflate.findViewById(R.id.linear).setOnClickListener(view -> checkBox.toggle());
        }

        @Override
        public void bindToView(EstatePropertyDetailValueModel item, int position) {
            super.bindToView(item, position);
            textView.setText(item.PropertyDetail.Title);
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) item.Value = "true";
                else
                    item.Value = "false";
            });
            if (item.Value != null) {
                if (item.Value.equals("true"))
                    checkBox.setChecked(true);
                else
                    checkBox.setChecked(false);
            }
        }
    }

    class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            getItem(position).Value = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }


}
