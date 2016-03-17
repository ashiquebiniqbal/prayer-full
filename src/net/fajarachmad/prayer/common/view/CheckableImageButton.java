package net.fajarachmad.prayer.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import net.fajarachmad.prayer.R;

/**
 * Created by user on 3/8/2016.
 */
public class CheckableImageButton extends ImageButton{

    private boolean checked;
    private Drawable onCheckedDrawable;
    private Drawable onUncheckedDrawable;

    public CheckableImageButton(Context context) {
        super(context);
    }

    public CheckableImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CheckableImageButton);
        try {
            onCheckedDrawable = ta.getDrawable(R.styleable.CheckableImageButton_onCheckedSrc);
            onUncheckedDrawable = ta.getDrawable(R.styleable.CheckableImageButton_onUncheckedSrc);
        } finally {
            ta.recycle();
        }

    }

    public CheckableImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checked) {
                    setImageDrawable(onUncheckedDrawable);
                    checked = false;
                } else {
                    setImageDrawable(onCheckedDrawable);
                    checked = true;
                }
            }
        });
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        if (checked) {
            setImageDrawable(onCheckedDrawable);
        } else {
            setImageDrawable(onUncheckedDrawable);
        }
    }
}
