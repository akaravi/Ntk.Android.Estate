package ntk.android.estate.view.component;

import static android.graphics.Paint.SUBPIXEL_TEXT_FLAG;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.textfield.TextInputLayout;

public class CustomTextInputLayout extends TextInputLayout {

    public class TypefaceSpan extends MetricAffectingSpan {

        /* renamed from: a */
        private Typeface f19145a;

        public TypefaceSpan(CustomTextInputLayout customTextInputLayout, Typeface typeface) {
            this.f19145a = typeface;
        }

        public void updateDrawState(TextPaint textPaint) {
            textPaint.setTypeface(this.f19145a);
            textPaint.setFlags(textPaint.getFlags() | SUBPIXEL_TEXT_FLAG);
        }

        public void updateMeasureState(TextPaint textPaint) {
            textPaint.setTypeface(this.f19145a);
            textPaint.setFlags(textPaint.getFlags() | SUBPIXEL_TEXT_FLAG);
        }
    }

    public CustomTextInputLayout(Context context) {
        super(context);
        if (!isInEditMode()) {
            m15092d();
        }
    }

    /* renamed from: a */
    private void m15091a(ColorFilter colorFilter) {
        if (getEditText() != null && getEditText().getBackground() != null) {
            getEditText().getBackground().setColorFilter(colorFilter);
        }
    }

    /* renamed from: d */
    private void m15092d() {
        setTypeface(Utility.getRegularTypeFace());
    }

    @Nullable
    private ColorFilter getBackgroundDefaultColorFilter() {
        if (getEditText() == null || getEditText().getBackground() == null) {
            return null;
        }
        return DrawableCompat.getColorFilter(getEditText().getBackground());
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        ColorFilter backgroundDefaultColorFilter = getBackgroundDefaultColorFilter();
        super.drawableStateChanged();
        m15091a(backgroundDefaultColorFilter);
    }

    public void setError(@Nullable CharSequence charSequence) {
        if (charSequence != null) {
            SpannableString spannableString = new SpannableString(charSequence);
            spannableString.setSpan(new TypefaceSpan(this, Utility.getRegularTypeFace()), 0, spannableString.length(), 33);
            super.setError(spannableString);
            m15091a(getBackgroundDefaultColorFilter());
            return;
        }
        super.setError(charSequence);
    }

    public CustomTextInputLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            m15092d();
        }
    }

    public CustomTextInputLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (!isInEditMode()) {
            m15092d();
        }
    }
}
