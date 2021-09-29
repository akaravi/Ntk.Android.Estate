package ntk.android.estate.view.component;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import ntk.android.estate.R;

public class CustomSupportEditText extends AppCompatEditText {
    public static final int DEFAULT_STATUS = 1;
    public static final int DISABLE_ERROR_STATE = 5;
    public static final int DISABLE_STATE = 0;
    public static final int ERROR_STATUS = 3;
    public static final int OK_STATUS = 2;
    public static final int REQUIRE_STATE = 4;

    /* renamed from: d */
    private String f19143d;

    /* renamed from: e */
    private String f19144e;
    public int status;

    public CustomSupportEditText(Context context) {
        super(context);
        if (!isInEditMode()) {
            m15090a();
        }
    }

    /* renamed from: a */
    private void m15090a() {
        setTypeface(Utility.getRegularTypeFace());
    }

    public String getErrorText() {
        return this.f19143d;
    }

    public String getRequiredErrorText() {
        return this.f19144e;
    }

    public int getStatus() {
        return this.status;
    }

    public void init(int i, CustomTextInputLayout customTextInputLayout, String str, String str2) {
        this.f19143d = str;
        this.f19144e = str2;
        setStatus(i);
    }

    public boolean isDisable() {
        int i = this.status;
        return i == 0 || i == 5;
    }

    public void setErrorText(String str) {
        this.f19143d = str;
    }

    public void setRequiredErrorText(String str) {
        this.f19144e = str;
    }

    public void setStatus(int i) {
        this.status = i;
        if (i == 4 || i == 3 || i == 5) {
            setBackground(getResources().getDrawable(R.drawable.curve_white_border_red_bg));
        } else {
            setBackground(getResources().getDrawable(R.drawable.gray_border_white_bg));
        }
    }

    public CustomSupportEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            m15090a();
        }
    }

    public CustomSupportEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (!isInEditMode()) {
            m15090a();
        }
    }
}
