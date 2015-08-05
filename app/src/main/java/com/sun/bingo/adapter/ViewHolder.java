package com.sun.bingo.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * @Description: Viewholder的简化
 * @author: sunfusheng
 * @date: 2015-2-4 上午
 */
public class ViewHolder {

    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
