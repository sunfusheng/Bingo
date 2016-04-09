package com.sun.bingo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sun.bingo.util.DisplayUtil;

/**
 * Created by sunfusheng on 15/7/28.
 */
public class UploadImageView extends ImageView {

    private Paint imagePaint;
    private Paint textPaint;
    private int progress = 0;

    public UploadImageView(Context context) {
        super(context);
        initPainters(context);
    }

    public UploadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPainters(context);
    }

    public UploadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPainters(context);
    }

    protected void initPainters(Context context) {
        imagePaint = new Paint();
        imagePaint.setAntiAlias(true);
        imagePaint.setStyle(Paint.Style.FILL);

        textPaint = new TextPaint();
        textPaint.setColor(Color.parseColor("#FFFFFF"));
        textPaint.setTextSize(DisplayUtil.sp2px(context, 18));
        textPaint.setAntiAlias(true);
        setProgressStart();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (progress < 0) {
            imagePaint.setColor(Color.parseColor("#70000000"));
            canvas.drawRect(0, 0, getWidth(), getHeight(), imagePaint);
        } else if (progress > 100) {
            imagePaint.setColor(Color.parseColor("#00000000"));
            canvas.drawRect(0, 0, getWidth(), getHeight(), imagePaint);
        } else {
            imagePaint.setColor(Color.parseColor("#70000000"));
            canvas.drawRect(0, 0, getWidth(), getHeight() - getHeight() * progress / 100, imagePaint);

            imagePaint.setColor(Color.parseColor("#00000000"));
            canvas.drawRect(0, getHeight() - getHeight() * progress / 100, getWidth(), getHeight(), imagePaint);

            String text = getDrawText();
            if (!TextUtils.isEmpty(text)) {
                float textHeight = textPaint.descent() + textPaint.ascent();
                canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, (getWidth() - textHeight) / 2.0f, textPaint);
            }
        }
    }

    public String getDrawText() {
        if (progress >= 0 && progress <= 100) {
            return progress+"%";
        }
        return null;
    }

    public void setProgressStart() {
        setProgress(-1);
    }

    public void setProgressFinish() {
        setProgress(101);
    }

    public void setProgress(int progress){
        this.progress=progress;
        postInvalidate();
    }

}
