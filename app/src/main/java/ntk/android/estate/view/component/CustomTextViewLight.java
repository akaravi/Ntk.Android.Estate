package ntk.android.estate.view.component;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextViewLight extends AppCompatTextView {
    public CustomTextViewLight(Context context) {
        super(context);
    }

    public CustomTextViewLight(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            setTypeface(Utility.getLightTypeFace());
        }
    }

    public CustomTextViewLight(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (!isInEditMode()) {
            setTypeface(Utility.getLightTypeFace());
        }
    }
}
