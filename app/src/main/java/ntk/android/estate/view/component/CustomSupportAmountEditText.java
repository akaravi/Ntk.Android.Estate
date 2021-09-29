package ntk.android.estate.view.component;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import java.text.DecimalFormat;
import java.text.ParseException;

public class CustomSupportAmountEditText extends CustomSupportEditText {
    /* access modifiers changed from: private */

    /* renamed from: f */
    public Long f19138f = -1L;
    /* access modifiers changed from: private */

    /* renamed from: g */
    public DecimalFormat f19139g;
    /* access modifiers changed from: private */

    /* renamed from: h */
    public OnTextChangeListener f19140h;
    /* access modifiers changed from: private */

    /* renamed from: i */
    public String f19141i;

    public interface OnTextChangeListener {
        void onTextChanged(String str);
    }

    /* renamed from: .CustomSupportAmountEditText$a */
    class C4405a implements TextWatcher {
        C4405a() {
        }

        public void afterTextChanged(Editable editable) {
            CustomSupportAmountEditText.this.removeTextChangedListener(this);
            try {
                CustomSupportAmountEditText.this.setText(CustomSupportAmountEditText.this.f19139g.format(CustomSupportAmountEditText.this.f19139g.parse(editable.toString().replace(String.valueOf(CustomSupportAmountEditText.this.f19139g.getDecimalFormatSymbols().getGroupingSeparator()), ""))));
                CustomSupportAmountEditText.this.setSelection(CustomSupportAmountEditText.this.getText().length());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (CustomSupportAmountEditText.this.getAmount().trim().length() > 0 && CustomSupportAmountEditText.this.f19138f.longValue() != -1 && CustomSupportAmountEditText.this.f19141i != null && CustomSupportAmountEditText.this.f19141i.trim().length() > 0 && Long.parseLong(Utility.convertToEnglishDigits(CustomSupportAmountEditText.this.getAmount())) > CustomSupportAmountEditText.this.f19138f.longValue()) {
                CustomSupportAmountEditText customSupportAmountEditText = CustomSupportAmountEditText.this;
                customSupportAmountEditText.setText(Utility.priceWithSign(String.valueOf(customSupportAmountEditText.f19138f)));
                CustomSupportAmountEditText customSupportAmountEditText2 = CustomSupportAmountEditText.this;
                customSupportAmountEditText2.setSelection(customSupportAmountEditText2.getText().length());
            }
            if (CustomSupportAmountEditText.this.f19140h != null) {
                CustomSupportAmountEditText.this.f19140h.onTextChanged(CustomSupportAmountEditText.this.getAmount());
            }
            CustomSupportAmountEditText.this.addTextChangedListener(this);
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (i == 0 && i2 < i3 && Utility.convertToEnglishDigits(charSequence.toString()).charAt(0) == '0') {
                CustomSupportAmountEditText.this.setText("");
            }
        }
    }

    public CustomSupportAmountEditText(Context context) {
        super(context);
        if (!isInEditMode()) {
            m15086a();
        }
    }

    public String getAmount() {
        String convertToEnglishDigits = Utility.convertToEnglishDigits(getText().toString().trim());
        DecimalFormat decimalFormat = this.f19139g;
        return decimalFormat != null ? convertToEnglishDigits.replace(String.valueOf(decimalFormat.getDecimalFormatSymbols().getGroupingSeparator()), "") : convertToEnglishDigits;
    }

    /* renamed from: a */
    private void m15086a() {
        this.f19139g = new DecimalFormat("###,###,###,###");
        addTextChangedListener(new C4405a());
    }

    public CustomSupportAmountEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            m15086a();
        }
    }

    public CustomSupportAmountEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (!isInEditMode()) {
            m15086a();
        }
    }
}
