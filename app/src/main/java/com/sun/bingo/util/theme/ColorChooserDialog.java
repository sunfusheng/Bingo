package com.sun.bingo.util.theme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sun.bingo.R;

/**
 * Created by sunfusheng on 15/8/10.
 */
public class ColorChooserDialog extends DialogFragment implements View.OnClickListener {


    private Callback mCallback;
    private int[] mColors;

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            Integer index = (Integer) v.getTag();
            mCallback.onColorSelection(index, mColors[index], Selector.shiftColor(mColors[index]));
            dismiss();
        }
    }

    public interface Callback {
        void onColorSelection(int index, int color, int darker);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.menu_change_theme)
                .autoDismiss(false)
                .customView(R.layout.dialog_color_chooser, false)
                .build();

        final TypedArray ta = getActivity().getResources().obtainTypedArray(R.array.colors);
        mColors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++)
            mColors[i] = ta.getColor(i, 0);
        ta.recycle();
        final GridLayout list = (GridLayout) dialog.getCustomView().findViewById(R.id.grid);
        final int preselect = getArguments().getInt("preselect", -1);

        for (int i = 0; i < list.getChildCount(); i++) {
            FrameLayout child = (FrameLayout) list.getChildAt(i);
            child.setTag(i);
            child.setOnClickListener(this);
            child.getChildAt(0).setVisibility(preselect == i ? View.VISIBLE : View.GONE);

            Drawable selector = Selector.createOvalShapeSelector(mColors[i]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int[][] states = new int[][]{
                        new int[]{-android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_pressed}
                };
                int[] colors = new int[]{
                        Selector.shiftColor(mColors[i]),
                        mColors[i]
                };
                ColorStateList rippleColors = new ColorStateList(states, colors);
                setBackgroundCompat(child, new RippleDrawable(rippleColors, selector, null));
            } else {
                setBackgroundCompat(child, selector);
            }
        }
        return dialog;
    }

    @SuppressLint("NewApi")
    private void setBackgroundCompat(View view, Drawable d) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(d);
        } else {
            view.setBackground(d);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callback))
            throw new RuntimeException("The Activity must implement Callback to be used by ColorChooserDialog.");
        mCallback = (Callback) activity;
    }

    public void show(AppCompatActivity context, int preselect) {
        Bundle args = new Bundle();
        args.putInt("preselect", preselect);
        setArguments(args);
        show(context.getSupportFragmentManager(), "COLOR_SELECTOR");
    }
}
