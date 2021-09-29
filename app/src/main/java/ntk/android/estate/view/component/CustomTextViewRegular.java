package ntk.android.estate.view.component;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;


public class CustomTextViewRegular extends AppCompatTextView {
    public CustomTextViewRegular(Context context) {
        super(context);
    }

    public CustomTextViewRegular(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            setTypeface(Utility.getRegularTypeFace());
        }
    }

    public CustomTextViewRegular(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (!isInEditMode()) {
            setTypeface(Utility.getRegularTypeFace());
        }
    }
}
