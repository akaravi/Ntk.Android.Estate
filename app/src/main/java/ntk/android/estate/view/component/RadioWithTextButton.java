package ntk.android.estate.view.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class RadioWithTextButton extends View {

    /* renamed from: a */
    private Paint f18227a;

    /* renamed from: b */
    private Paint f18228b;

    /* renamed from: c */
    private Paint f18229c;

    /* renamed from: d */
    private String f18230d = null;

    /* renamed from: e */
    private float f18231e;

    /* renamed from: f */
    private Drawable f18232f = null;

    /* renamed from: g */
    private Rect f18233g = null;

    /* renamed from: h */
    private Typeface f18234h = Typeface.createFromAsset(getContext().getAssets(), "fonts/IRAN-Sans-Regular.ttf");

    /* renamed from: i */
    private final Rect f18235i = new Rect();

    public RadioWithTextButton(Context context) {
        super(context);
        m14543a();
    }

    /* renamed from: a */
    private void m14543a() {
        this.f18229c = new Paint(1);
        this.f18227a = new Paint(1);
        this.f18228b = new Paint(1);
        this.f18229c.setFakeBoldText(true);
        if (!isInEditMode()) {
            this.f18229c.setTypeface(this.f18234h);
        }
    }

    private Rect getCenterRect() {
        if (this.f18233g == null) {
            Rect rect = new Rect(0, 0, getWidth(), getHeight());
            int width = getWidth() / 4;
            float f = (float) width;
            this.f18233g = new Rect((int) (rect.exactCenterX() - f), (int) (rect.exactCenterY() - f), getWidth() - width, getHeight() - width);
        }
        return this.f18233g;
    }

    public void drawTextCentred(Canvas canvas, Paint paint, String str, float f, float f2) {
        m14544a(paint, str, this.f18231e);
        paint.getTextBounds(str, 0, str.length(), this.f18235i);
        canvas.drawText(str, f - this.f18235i.exactCenterX(), f2 - this.f18235i.exactCenterY(), paint);
    }

    public boolean isChecked() {
        return (this.f18230d == null && this.f18232f == null) ? false : true;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        this.f18227a.setStrokeWidth((float) (width / 18));
        if (isChecked()) {
            float f = (float) (width / 2);
            float f2 = (float) (height / 2);
            int i = width / 3;
            canvas.drawCircle(f, f2, (float) i, this.f18228b);
            String str = this.f18230d;
            if (str != null) {
                this.f18231e = (float) ((i * 2) - 20);
                drawTextCentred(canvas, this.f18229c, str, f, f2);
                return;
            }
            Drawable drawable = this.f18232f;
            if (drawable != null) {
                drawable.setBounds(getCenterRect());
                this.f18232f.draw(canvas);
                return;
            }
            return;
        }
        this.f18227a.setStyle(Paint.Style.STROKE);
        canvas.drawCircle((float) (width / 2), (float) (height / 2), (float) (width / 3), this.f18227a);
    }

    public void setCircleColor(int i) {
        Paint paint = this.f18228b;
        if (paint != null) {
            paint.setColor(i);
        }
    }

    public void setDrawable(Drawable drawable) {
        this.f18230d = null;
        this.f18232f = drawable;
        invalidate();
    }

    public void setStrokeColor(int i) {
        Paint paint = this.f18227a;
        if (paint != null) {
            paint.setColor(i);
        }
    }

    public void setText(String str) {
        this.f18232f = null;
        this.f18230d = str;
        invalidate();
    }

    public void setTextColor(int i) {
        Paint paint = this.f18229c;
        if (paint != null) {
            paint.setColor(i);
        }
    }

    public void unselect() {
        this.f18230d = null;
        this.f18232f = null;
        invalidate();
    }

    /* renamed from: a */
    private static void m14544a(Paint paint, String str, float f) {
        paint.setTextSize(40.0f);
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        if (((float) rect.width()) > f) {
            paint.setTextSize((f / ((float) rect.width())) * 40.0f);
        }
    }

    public RadioWithTextButton(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        m14543a();
    }

    public RadioWithTextButton(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        m14543a();
    }
}
