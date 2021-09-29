package ntk.android.estate.view.component;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;


public class CustomCheckBoxRegular extends AppCompatCheckBox {
    public CustomCheckBoxRegular(Context context) {
        super(context);
    }

    public CustomCheckBoxRegular(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            setTypeface(Utility.getRegularTypeFace());
        }
    }

    public CustomCheckBoxRegular(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (!isInEditMode()) {
            setTypeface(Utility.getRegularTypeFace());
        }
    }
}
