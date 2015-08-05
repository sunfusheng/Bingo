package com.sun.bingo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.sun.bingo.util.DisplayUtil;

/**
 * Created by sunfusheng on 15/7/28.
 */
public class UploadAvatarView extends CircularImageView {

    private Paint imagePaint;
    private Paint textPaint;
    private int progress = 0;
    private RectF rectF = new RectF();

    public UploadAvatarView(Context context) {
        super(context);
        initPainters(context);
    }

    public UploadAvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPainters(context);
    }

    public UploadAvatarView(Context context, AttributeSet attrs, int defStyle) {
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
        setProgressOver();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        rectF.set(0, 0, MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float yHeight = progress / (float) 100 * getHeight();
        float radius = getWidth() / 2f;
        float angle = (float) (Math.acos((radius - yHeight) / radius) * 180 / Math.PI);
        float startAngle = 90 + angle;
        float sweepAngle = 360 - angle * 2;

        imagePaint.setColor(Color.parseColor("#70000000"));
        canvas.drawArc(rectF, startAngle, sweepAngle, false, imagePaint);

        canvas.save();
        canvas.rotate(180, getWidth() / 2, getHeight() / 2);
        imagePaint.setColor(Color.parseColor("#00000000"));
        canvas.drawArc(rectF, 270 - angle, angle * 2, false, imagePaint);
        canvas.restore();

        String text = getDrawText();
        if (!TextUtils.isEmpty(text)) {
            float textHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, (getWidth() - textHeight) / 2.0f, textPaint);
        }
    }

    public String getDrawText() {
        if (progress >= 0 && progress <= 100) {
            return progress+"%";
        }
        return null;
    }

    public void setProgressOver() {
        setProgress(-1);
    }

    public void setProgress(int progress){
        this.progress=progress;
        postInvalidate();
    };

}
