package ntk.android.estate.view.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class GalleryTextViewMedium extends AppCompatTextView {

    /* renamed from: e */
    private Typeface f18181e = Typeface.createFromAsset(getContext().getAssets(), "fonts/IRAN-Sans-Medium.ttf");

    public GalleryTextViewMedium(Context context) {
        super(context);
    }

    public GalleryTextViewMedium(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            setTypeface(this.f18181e);
        }
    }

    public GalleryTextViewMedium(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (!isInEditMode()) {
            setTypeface(this.f18181e);
        }
    }
}
