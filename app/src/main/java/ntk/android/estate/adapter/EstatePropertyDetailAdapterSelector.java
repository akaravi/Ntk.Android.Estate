package ntk.android.estate.adapter;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailGroupModel;
import ntk.android.base.entitymodel.estate.EstatePropertyDetailModel;
import ntk.android.base.utill.FontManager;
import ntk.android.estate.R;

class EstatePropertyDetailAdapterSelector extends BaseRecyclerAdapter<EstatePropertyDetailModel, EstatePropertyDetailAdapterSelector.VH> {
    public EstatePropertyDetailAdapterSelector(EstatePropertyDetailGroupModel item) {
        super(item.PropertyDetails);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).InputDataType;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(parent).CREATE_HOLDER(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bindToView(getItem(position), position);
    }

    public class VH extends RecyclerView.ViewHolder {
        public VH(@NonNull View itemView) {
            super(itemView);
        }

        public VH CREATE_HOLDER(ViewGroup parent, int viewType) {
            if (viewType == 0)//as String
                return new StringVH(inflate(parent, R.layout.row_property_detail_stirng_type));
            if (viewType == 1)//as int
                return new IntegerVH(inflate(parent, R.layout.row_property_detail_stirng_type));
            if (viewType == 2)
                return new BooleanVH(inflate(parent, R.layout.row_property_detail_boolean_type));
            if (viewType == 3)//as float
                return new FloatVH(inflate(parent, R.layout.row_property_detail_stirng_type));
            if (viewType == 4)//as date
                return new StringVH(inflate(parent, R.layout.row_property_detail_date_type));
            else
                return new MultiLineVH(inflate(parent, R.layout.row_property_detail_textarea_type));
        }

        public Context getContext() {
            return itemView.getContext();
        }


        public void bindToView(EstatePropertyDetailModel item, int position) {

        }
    }


    private class StringVH extends VH {
        TextInputEditText editText;
        TextInputLayout inputLayout;

        public StringVH(View itemView) {
            super(itemView);
            inputLayout = itemView.findViewById(R.id.inputLayout);
            editText = itemView.findViewById(R.id.inputEditText);
            editText.setInputType(inputType());
            editText.setTypeface(FontManager.T1_Typeface(getContext()));
        }

        public int inputType() {
            return InputType.TYPE_CLASS_TEXT;
        }

        @Override
        public void bindToView(EstatePropertyDetailModel item, int position) {
            super.bindToView(item, position);
            inputLayout.setHint(item.Title);
        }
    }

    private class MultiLineVH extends StringVH {
        public MultiLineVH(View inflate) {
            super(inflate);
        }

        @Override
        public int inputType() {
            return InputType.TYPE_TEXT_FLAG_MULTI_LINE;
        }
    }


    private class IntegerVH extends StringVH {
        public IntegerVH(View inflate) {
            super(inflate);
        }

        @Override
        public int inputType() {
            return InputType.TYPE_CLASS_NUMBER;
        }
    }

    private class FloatVH extends StringVH {
        public FloatVH(View inflate) {
            super(inflate);
        }

        @Override
        public int inputType() {
            return InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }
    }

    private class BooleanVH extends VH {
        MaterialCheckBox checkBox;
        TextView textView;

        public BooleanVH(View inflate) {
            super(inflate);
            checkBox = inflate.findViewById(R.id.checkBox);
            textView = inflate.findViewById(R.id.txt);
            inflate.findViewById(R.id.linear).setOnClickListener(view -> checkBox.toggle());
        }

        @Override
        public void bindToView(EstatePropertyDetailModel item, int position) {
            super.bindToView(item, position);
            textView.setText(item.Title);
        }
    }
}
