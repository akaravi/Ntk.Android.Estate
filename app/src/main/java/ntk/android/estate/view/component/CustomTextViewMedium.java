package ntk.android.estate.view.component;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;


public class CustomTextViewMedium extends AppCompatTextView {
    public CustomTextViewMedium(Context context) {
        super(context);
    }

    public CustomTextViewMedium(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            setTypeface(Utility.getMediumTypeFace());
        }
    }

    public CustomTextViewMedium(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (!isInEditMode()) {
            setTypeface(Utility.getMediumTypeFace());
        }
    }
}
