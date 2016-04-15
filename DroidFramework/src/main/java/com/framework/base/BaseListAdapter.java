package com.framework.base;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.framework.DroidFramework;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 基础的列表适配器
 * @author: sunfusheng
 * @date: 2015-2-4 上午
 */
public abstract class BaseListAdapter<E> extends BaseAdapter {

    private List<E> mList = new ArrayList<E>();
    protected Context mContext;
    protected LayoutInflater mInflater;

    public BaseListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public BaseListAdapter(Context context, List<E> list) {
        this(context);
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public void clearAll() {
        mList.clear();
    }

    public void setData(List<E> list) {
        clearAll();
        addALL(list);
    }

    public List<E> getData() {
        return mList;
    }

    public void addALL(List<E> list){
        if(list == null || list.size() == 0){
            return ;
        }
        mList.addAll(list);
    }
    public void add(E item){
        mList.add(item);
    }

    @Override
    public E getItem(int position) {
        return (E) mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeEntity(E e){
        mList.remove(e);
    }

    // 给继承TextView的子View设置我们的标题字体
    protected void setTitleTypeface(TextView... views) {
        if (views == null || views.length == 0) return ;
        // 字体初始化
        Typeface titleTf = Typeface.createFromAsset(DroidFramework.getContext().getAssets(), "fonts/FZLTH.TTF");
        for (TextView view:views) {
            view.setTypeface(titleTf);
        }
    }

    // 给继承TextView的子View设置我们的内容字体
    protected void setContentTypeface(TextView... views) {
        if (views == null || views.length == 0) return ;
        // 字体初始化
        Typeface contentTf = Typeface.createFromAsset(DroidFramework.getContext().getAssets(), "fonts/FZLTXH.TTF");
        for (TextView view:views) {
            view.setTypeface(contentTf);
        }
    }

    // 给TextView设置颜色
    public void setTextColor(int colorId, TextView... views) {
        if (views == null || views.length == 0) return ;
        for (TextView view:views) {
            int color = mContext.getResources().getColor(colorId);
            view.setTextColor(color);
        }
    }

    // 给TextView设置粗体
    public void setTextBold(TextView... views) {
        if (views == null || views.length == 0) return ;
        for (TextView view : views) {
            view.getPaint().setFakeBoldText(true);
        }
    }

}
